/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.kntu.os.mem;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        
        threadPool.submit(() -> {
            // TODO
            System.out.println("Total Used Space: " + BusyFrames.size()*2 + " Bytes");
            System.out.println("Total Free Space: " + FreeFrames.size()*2 + " Bytes");
            for (int i = 0; i < PageTables.size(); i++) {
                System.out.println("Process " + (i+1) + " Used Space: " + 
                        PageTables.get(i).getNumberOfActivePages()*2 + " Bytes");
                System.out.println("Process " + (i+1) + " Page Faults: " + 
                        PageTables.get(i).getNumberOfPageFaults() + " Bytes");
            }
        });
        
        for (int i = 0; i < Util.getNextRandom(10); i++){
            VProcess process = new VProcess(this, (i + 1));
            PageTable table = new PageTable((i + 1));
            PageTables.add(table);
            process.start();
        }
    }

    public void allocate(int pid, VirtualAddress address, int size) throws MemoryFullException{
        threadPool.submit(() -> {
            
        });
    }

    public void deAllocate(int pid, VirtualAddress address, int size) throws AccessViolationException, PageFaultException{
        threadPool.submit(() -> {
            
        });
    }

    public void read(int pid, VirtualAddress address, int size) throws AccessViolationException, PageFaultException{
        threadPool.submit(() -> {
            
        });
    }

    public void write(int pid, VirtualAddress address, int size) throws AccessViolationException, PageFaultException{
        threadPool.submit(() -> {
            
        });
    }


    public void processFinished(int pid){
        threadPool.submit(() -> {
            
        });
    }
    
}
