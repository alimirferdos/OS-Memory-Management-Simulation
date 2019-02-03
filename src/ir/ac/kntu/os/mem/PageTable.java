/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.kntu.os.mem;

/**
 *
 * @author Ali
 */
public class PageTable {
    private final VProcess process;
    private int numberOfPageFaults;
    private int numberOfActivePages;
    private Page table[];
    
    public PageTable(VProcess process) {
        table = new Page[(int) (1L<<6)];
        for(int i = 0; i < table.length; i++){
           table[i] = new Page(); 
        }
        numberOfPageFaults = 0;
        numberOfActivePages = 0;
        this.process = process;
    }

    //public int getPid() { return process.pid; }

    public int getNumberOfPageFaults() { return numberOfPageFaults; }

    public int getNumberOfActivePages() { return numberOfActivePages; }
    
    public boolean isFull(){
        return numberOfActivePages == (int) (1L<<6);
    }
    
    public int translateAddress(VirtualAddress address) throws PageFaultException{
        Page accessed = table[address.getPageNo()];
        if(accessed.isActive()){
            return (accessed.getAddress() << 10)+ address.getPageOffset();
        }
        else{
            numberOfPageFaults++;
            throw new PageFaultException();
        }
    }

    public void allocate(VirtualAddress address, int frame) throws PageFaultException{
        Page p = table[address.getPageNo()];
        p.setActive(true);
        p.setAddress(frame);
    }
    
    public int deAllocate(VirtualAddress address) throws PageFaultException{
        Page p = table[address.getPageNo()];
        p.setActive(false);
        return p.getAddress();
    }
}
