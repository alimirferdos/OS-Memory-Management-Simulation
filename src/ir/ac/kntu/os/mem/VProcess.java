/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.kntu.os.mem;

public class VProcess extends Thread{
    public static final int ADDRESS_SPACE_BOUND = (int) (2L << 16);

    private final OS os;
    private final int pid;


    private int startAddress;
    private LocalityAwareRandomAddressGenerator addressGenerator;

    public VProcess(OS os, int pid){
        this.os = os;
        this.pid = pid;
        this.setName("process_"+pid);

        this.startAddress = Util.getNextRandom(ADDRESS_SPACE_BOUND);
        this.addressGenerator = new LocalityAwareRandomAddressGenerator(startAddress);
    }

    @Override
    public void run(){
        final int iterationCount = Util.getNextRandom(100);

        for (int i = 0; i < iterationCount; i++){
            int accessAddress = addressGenerator.getNextAddress();
            VirtualAddress virtualAddress = new VirtualAddress(accessAddress);

            // ToDo: call random methods
        }

        // Process exiting, do cleanup
        os.processFinished(pid);
    }
}
