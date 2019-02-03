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
import java.util.concurrent.locks.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OS {
    public static final int MACHINE_MEM_BOUND = (int) (1L << 20);

    // in bytes
    public static final int[] ALLOCATION_SIZES = new int[]
            {
                    1, 2, 4, 8, 16, 32, 64, 128
            };
    public static final ExecutorService threadPool = Executors.newFixedThreadPool(5);

    private ArrayList<Integer> FreeFrames;
    private ArrayList<Integer> BusyFrames;
    private ArrayList<PageTable> PageTables;
    private int[] Memory;
    ReentrantLock FreeFramesLock;
    ReentrantLock BusyFramesLock;
    ReadWriteLock MemoryLock;
            
    public OS(){
        FreeFramesLock = new ReentrantLock();
        BusyFramesLock = new ReentrantLock();
        MemoryLock = new ReentrantReadWriteLock();
    }

    public void doStartup(){
        //todo: you may start data structures here like lists, paging system ...
        FreeFrames = new ArrayList<>();
        BusyFrames = new ArrayList<>();
        for (int i = 0; i < 1024; i++) {
            FreeFrames.add(i);
        }
        Memory = new int[MACHINE_MEM_BOUND];
        PageTables = new ArrayList<>();
        int j;
        for (j = 0; j < Util.getNextRandom(10); j++){
            VProcess process = new VProcess(this, (j + 1));
            PageTable table = new PageTable(process);
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

    public void allocate(int pid, VirtualAddress address, int size){
        threadPool.submit(() -> {
            try {
                PageTables.get(pid).addressValidationTest(address);
            } catch (PageFaultException ex) {
                System.err.println(ex.getMessage());
            }
            
            FreeFramesLock.lock();
            BusyFramesLock.lock();
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
                    PageTables.get(pid).allocate(address, frameAddr);
                    BusyFrames.add(frameAddr);
                }
            } catch (MemoryFullException | PageFaultException ex) {
                System.err.println(ex.getMessage());
            } finally {
                FreeFramesLock.unlock();
                BusyFramesLock.unlock();
            }
        });
    }

    public void deAllocate(int pid, VirtualAddress address, int size){
        threadPool.submit(() -> {
            try {
                PageTables.get(pid).addressValidationTest(address);
            } catch (PageFaultException ex) {
                System.err.println(ex.getMessage());
            }
            FreeFramesLock.lock();
            BusyFramesLock.lock();
            try {
                int frames = 1;
                if(address.getPageOffset() + size >= 1024){
                    frames++;
                }
                
                int frameAddr;
                for (int i = 0; i < frames; i++) {
                    frameAddr = PageTables.get(pid).deAllocate(address);
                    BusyFrames.remove(frameAddr);
                    FreeFrames.add(frameAddr);
                }
            } catch (PageFaultException ex) {
                System.err.println(ex.getMessage());
            } finally {
                FreeFramesLock.unlock();
                BusyFramesLock.unlock();
            }
        });
    }

    public void read(int pid, VirtualAddress address, int size){
        threadPool.submit(() -> {
            int physicalAddress;
            try {
                physicalAddress = PageTables.get(pid).translateAddress(address);
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

    public void write(int pid, VirtualAddress address, int size){
        threadPool.submit(() -> {
            int physicalAddress;
            try {
                physicalAddress = PageTables.get(pid).translateAddress(address);
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


    public void processFinished(int pid){
        threadPool.submit(() -> {
            FreeFramesLock.lock();
            BusyFramesLock.lock();
            try {
                int frameAddr;
                for (int i = 0; i < (int) (1L<<6); i++) {
                    frameAddr = PageTables.get(pid).deAllocate(i);
                    BusyFrames.remove(frameAddr);
                    FreeFrames.add(frameAddr);
                }
            } finally {
                FreeFramesLock.unlock();
                BusyFramesLock.unlock();
            }
        });
    }
    
}
