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

    public OS(){

    }

    public void doStartup(){
        //todo: you may start data structures here like lists, paging system ...
        ArrayList<Integer> FreeFrames = new ArrayList<>();
        ArrayList<Integer> BusyFrames = new ArrayList<>();
        for (int i = 0; i < 1024; i++) {
            FreeFrames.add(i);
        }
        
        for (int i = 0; i < Util.getNextRandom(10); i++){
            VProcess process = new VProcess(this, (i + 1));
            process.start();
        }
    }

    public void allocate(int pid, VirtualAddress address, int size){
        threadPool.submit(new Runnable(){
            @Override
            public void run(){

            }
        });
    }

    public void deAllocate(int pid, VirtualAddress address, int size){
        threadPool.submit(new Runnable(){
            @Override
            public void run(){

            }
        });
    }

    public void read(int pid, VirtualAddress address, int size){
        threadPool.submit(new Runnable(){
            @Override
            public void run(){

            }
        });
    }

    public void write(int pid, VirtualAddress address, int size){
        threadPool.submit(new Runnable(){
            @Override
            public void run(){

            }
        });
    }


    public void processFinished(int pid){
        threadPool.submit(new Runnable(){
            @Override
            public void run(){

            }
        });
    }
}
