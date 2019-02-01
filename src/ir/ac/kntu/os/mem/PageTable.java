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
    private final int pid;
    private int numberOfPageFaults;
    private int numberOfActivePages;
    private Page table[];
    
    public PageTable(int pid) {
        table = new Page[(int) (1L<<6)];
        for(int i = 0; i < table.length; i++){
           table[i] = new Page(); 
        }
        numberOfPageFaults = 0;
        numberOfActivePages = 0;
        this.pid = pid;
    }

    public int getPid() { return pid; }

    public int getNumberOfPageFaults() { return numberOfPageFaults; }

    public int getNumberOfActivePages() { return numberOfActivePages; }
    
    public boolean isFull(){
        return numberOfActivePages == (int) (1L<<6);
    }
    
    public int translateAddress(VirtualAddress address) throws AccessViolationException, PageFaultException{
        Page accessed = table[address.getPageNo()];
        if(accessed.isActive()){
            return (accessed.getAddress() << 10)+ address.getPageOffset();
        }
        else{
            numberOfPageFaults++;
            throw new PageFaultException();
        }
    }
}
