/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.kntu.os.mem;

import java.util.logging.Level;
import java.util.logging.Logger;

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
            switch(Util.getNextRandom(4)){
                case 0:
            {
                try {
                    os.allocate(pid, virtualAddress, i);
                } catch (MemoryFullException | PageFaultException ex) {
                    Logger.getLogger(VProcess.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                    break;
                case 1:
            {
                try {
                    os.deAllocate(pid, virtualAddress, i);
                } catch (PageFaultException ex) {
                    Logger.getLogger(VProcess.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                    break;
                case 2:
            {
                try {
                    os.read(pid, virtualAddress, i);
                } catch (AccessViolationException ex) {
                    os.allocate(pid, virtualAddress, i);
                    Logger.getLogger(VProcess.class.getName()).log(Level.SEVERE, null, ex);
                } catch (PageFaultException ex) {
                    Logger.getLogger(VProcess.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                    break;
                case 3:
            {
                try {
                    os.write(pid, virtualAddress, i);
                } catch (AccessViolationException ex) {
                    os.allocate(pid, virtualAddress, i);
                    Logger.getLogger(VProcess.class.getName()).log(Level.SEVERE, null, ex);
                } catch (PageFaultException ex) {
                    Logger.getLogger(VProcess.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                    break;
            }
            try {
                VProcess.sleep(Util.getNextRandom(1000));
            } catch (InterruptedException ex) {}
        }

        // Process exiting, do cleanup
        os.processFinished(pid);
    }
}
