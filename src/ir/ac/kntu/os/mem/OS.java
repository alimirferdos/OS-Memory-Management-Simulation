/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.kntu.os.mem;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private ArrayList<VProcess> Processes;
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
        Processes = new ArrayList<>();
        
        threadPool.submit(() -> {
            System.out.println("Total Used Space: " + BusyFrames.size()*2 + " Bytes");
            System.out.println("Total Free Space: " + FreeFrames.size()*2 + " Bytes");
            for (int i = 0; i < Processes.size(); i++) {
                // TODO
                System.out.println("Process " + (i+1) + " Used Space: " + Processes.get(i) + " Bytes");
                System.out.println("Process " + (i+1) + " Page Faults: " + Processes.get(i) + " Bytes");
            }
        });
        
        for (int i = 0; i < Util.getNextRandom(10); i++){
            VProcess process = new VProcess(this, (i + 1));
            process.start();
            Processes.add(process);
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
