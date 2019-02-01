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

public class OS {
    public static final int MACHINE_MEM_BOUND = (int) (2L << 20);

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
    ReadWriteLock FreeFramesLock = new ReentrantReadWriteLock();
    ReadWriteLock BusyFramesLock = new ReentrantReadWriteLock();
    ReadWriteLock MemoryLock = new ReentrantReadWriteLock();
            
    public OS(){

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
        
        for (int i = 0; i < Util.getNextRandom(10); i++){
            VProcess process = new VProcess(this, (i + 1));
            PageTable table = new PageTable((i + 1));
            PageTables.add(table);
            process.start();
        }
        
        threadPool.submit(() -> {
            do {
                System.out.println("------------------");
                System.out.println(new Timestamp(System.currentTimeMillis()));
                System.out.println("Total Used Space: " + 
                        BusyFrames.size() * (int) (1L << 10) + " Bytes");
                System.out.println("Total Free Space: " + 
                        FreeFrames.size() * (int) (1L << 10) + " Bytes");
                for (int i = 0; i < PageTables.size(); i++) {
                    System.out.println("Process " + (i+1) + " Used Space: " + 
                            PageTables.get(i).getNumberOfActivePages() * 
                                    (int) (1L << 10) + " Bytes");
                    System.out.println("Process " + (i+1) + " Page Faults: " + 
                            PageTables.get(i).getNumberOfPageFaults() + " Bytes");
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {}
            } while(!PageTables.isEmpty());
        });
    }

    public void allocate(int pid, VirtualAddress address, int size) throws MemoryFullException{
        threadPool.submit(() -> {
           
        });
    }

    public void deAllocate(int pid, VirtualAddress address, int size) throws AccessViolationException, PageFaultException{
        threadPool.submit(() -> {
            int physicalAddress = PageTables.get(pid).translateAddress(address);
            
        });
    }

    public void read(int pid, VirtualAddress address, int size) throws AccessViolationException, PageFaultException{
        threadPool.submit(() -> {
            int physicalAddress = PageTables.get(pid).translateAddress(address);
            try{
                MemoryLock.readLock().lock();
                int output;
                for (int i = 0; i < size; i++) {
                    output = Memory[physicalAddress];
                }
            } finally {
                MemoryLock.readLock().unlock();
            }
        });
    }

    public void write(int pid, VirtualAddress address, int size) throws AccessViolationException, PageFaultException{
        threadPool.submit(() -> {
            int physicalAddress = PageTables.get(pid).translateAddress(address);
            try{
                MemoryLock.writeLock().lock();
                int output;
                for (int i = 0; i < size; i++) {
                    Memory[physicalAddress] = 1;
                }
            } finally {
                MemoryLock.writeLock().unlock();
            }
            
        });
    }


    public void processFinished(int pid){
        threadPool.submit(() -> {
            
        });
    }
    
}
