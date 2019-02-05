/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.kntu.os.mem;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.sql.Timestamp;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.*;

public class ConcurrentOS implements I_OS{
    public static final int MACHINE_MEM_BOUND = (int) (1L << 20);

    // in bytes
    public static final int[] ALLOCATION_SIZES = new int[]
            {
                    1, 2, 4, 8, 16, 32, 64, 128
            };
    public static final ExecutorService threadPool = Executors.newFixedThreadPool(5);

    private CopyOnWriteArrayList<Integer> FreeFrames; 
    private CopyOnWriteArrayList<Integer> BusyFrames;
    private int[] Memory;
    ReadWriteLock MemoryLock;
    
    //private ArrayList<PageTableUser> PageTables;
    private ArrayList<LayeredPageTableUser> PageTables;
            
    public ConcurrentOS(){
        MemoryLock = new ReentrantReadWriteLock();
    }

    @Override
    public void doStartup(){
        //todo: you may start data structures here like lists, paging system ...
        FreeFrames = new CopyOnWriteArrayList<>();
        BusyFrames = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 1024; i++) {
            FreeFrames.add(i);
        }
        Memory = new int[MACHINE_MEM_BOUND];
        PageTables = new ArrayList<>();
        int j;
        for (j = 0; j < Util.getNextRandom(10); j++){
            VProcess process = new VProcess(this, (j + 1));
            //PageTable table = new PageTable(process);
            LayeredPageTableUser table = new LayeredPageTableUser(process);
            PageTables.add(table);
            process.start();
        }
        System.out.println("Checking " + j + " processes.");
        
        threadPool.submit(() -> {
            do {
                System.out.println("------------------");
                System.out.println(new Timestamp(System.currentTimeMillis()));
                System.out.println("Total Used Space: " + 
                        BusyFrames.size() * 1024 + " Bytes");
                System.out.println("Total Free Space: " + 
                        FreeFrames.size() * 1024 + " Bytes");
                for (int i = 0; i < PageTables.size(); i++) {
                    System.out.println("Process " + (i+1) + " Used Space: " + 
                            PageTables.get(i).getNumberOfActivePages() * 
                                    1024 + " Bytes");
                    System.out.println("Process " + (i+1) + " Page Faults: " + 
                            PageTables.get(i).getNumberOfPageFaults() + " Bytes");
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {}
            } while(!PageTables.isEmpty());
        });
    }

    @Override
    public void allocate(int pid, VirtualAddress address, int size){
        threadPool.submit(() -> {
            try {
                PageTables.get(pid).addressValidationTest(address, pid);
            } catch (PageFaultException ex) {
                System.err.println(ex.getMessage());
            }
            
            try {
                int frames = 1;
                if(address.getPageOffset() + size >= 1024){
                    frames++;
                }
                if(FreeFrames.size() < frames){
                    throw new MemoryFullException("process_" + pid + 
                            " wanted to allocate " + size + 
                            " Bytes but the memory is currently full!");
                }
                
                int frameAddr;
                for (int i = 0; i < frames; i++) {
                    frameAddr = FreeFrames.remove(0);
                    PageTables.get(pid).allocate(address, frameAddr, pid);
                    BusyFrames.add(frameAddr);
                }
            } catch (MemoryFullException | PageFaultException | AccessViolationException ex) {
                System.err.println(ex.getMessage());
            }
        });
    }

    @Override
    public void deAllocate(int pid, VirtualAddress address, int size){
        threadPool.submit(() -> {
            try {
                PageTables.get(pid).addressValidationTest(address, pid);
            } catch (PageFaultException ex) {
                System.err.println(ex.getMessage());
            }
            try {
                int frames = 1;
                if(address.getPageOffset() + size >= 1024){
                    frames++;
                }
                
                int frameAddr;
                for (int i = 0; i < frames; i++) {
                    frameAddr = PageTables.get(pid).deAllocate(address, pid);
                    BusyFrames.remove(frameAddr);
                    FreeFrames.add(frameAddr);
                }
            } catch (PageFaultException ex) {
                System.err.println(ex.getMessage());
            }
        });
    }

    @Override
    public void read(int pid, VirtualAddress address, int size){
        threadPool.submit(() -> {
            int physicalAddress;
            try {
                physicalAddress = PageTables.get(pid).translateAddress(address, pid);
                try{
                    MemoryLock.readLock().lock();
                    int output;
                    for (int i = 0; i < size; i++) {
                        output = Memory[physicalAddress];
                    }
                } finally {
                    MemoryLock.readLock().unlock();
                }
            } catch (PageFaultException ex) {
                System.err.println(ex.getMessage());
            } catch (AccessViolationException ex) {
                allocate(pid, address, OS.ALLOCATION_SIZES[Util.getNextRandom(8)]);
                System.err.println(ex.getMessage());
            }
        });
    }

    @Override
    public void write(int pid, VirtualAddress address, int size){
        threadPool.submit(() -> {
            int physicalAddress;
            try {
                physicalAddress = PageTables.get(pid).translateAddress(address, pid);
                try{
                    MemoryLock.writeLock().lock();
                    for (int i = 0; i < size; i++) {
                        Memory[physicalAddress] = 1;
                    }
                } finally {
                    MemoryLock.writeLock().unlock();
                }
            } catch (PageFaultException ex) {
                System.err.println(ex.getMessage());
            } catch (AccessViolationException ex) {
                allocate(pid, address, OS.ALLOCATION_SIZES[Util.getNextRandom(8)]);
                System.err.println(ex.getMessage());
            }
        });
    }

    @Override
    public void processFinished(int pid){
        threadPool.submit(() -> {
            ArrayList<Integer> frameAddresses = PageTables.get(pid).deAllocateAll(pid);
            for (Integer f : frameAddresses) {
                BusyFrames.remove(f);
                FreeFrames.add(f);
            }            
        });
    }
    
}
