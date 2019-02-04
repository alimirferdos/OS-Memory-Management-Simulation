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
        System.out.println(this.getName() + " will have " + iterationCount + " instructions.");
        
        for (int i = 0; i < iterationCount; i++){
            int accessAddress = addressGenerator.getNextAddress();
            VirtualAddress virtualAddress = new VirtualAddress(accessAddress);
            switch(Util.getNextRandom(4)){
                case 0:
                    os.allocate(pid, virtualAddress, OS.ALLOCATION_SIZES[Util.getNextRandom(8)]);
                    break;
                case 1:
                    os.deAllocate(pid, virtualAddress, OS.ALLOCATION_SIZES[Util.getNextRandom(8)]);
                    break;
                case 2:
                    os.read(pid, virtualAddress, OS.ALLOCATION_SIZES[Util.getNextRandom(8)]);
                    break;
                case 3:
                    os.write(pid, virtualAddress, OS.ALLOCATION_SIZES[Util.getNextRandom(8)]);
                    break;
            }
            try {
                VProcess.sleep(Util.getNextRandom(1000));
            } catch (InterruptedException ex) {
                System.err.println(ex.getMessage());
            }
        }

        // Process exiting, do cleanup
        os.processFinished(pid);
    }
}
