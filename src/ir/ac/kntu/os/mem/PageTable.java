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
        table = new Page[(int) (1L<<7)];
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
        return numberOfActivePages == (int) (1L<<7);
    }
    
    public int translateAddress(VirtualAddress address) throws PageFaultException{
        int pageAddr = address.getPageNo();
        int addrOffset = address.getPageOffset();
        if(pageAddr >= (int) (1L<<7) || addrOffset >= (int) (1L<<11)){
            numberOfPageFaults++;
            throw new PageFaultException("Process " + process.getId() +
                    " wanted to access a wrong address.");
        }
        Page accessed = table[pageAddr];
        if(accessed.isActive()){
            return (accessed.getAddress() << 10)+ addrOffset;
        }
        else{
            numberOfPageFaults++;
            throw new PageFaultException("Process " + process.getId() +
                    " wanted to access a not activated page.", true);
        }
    }

    public void allocate(VirtualAddress address, int frame) throws PageFaultException{
        Page p = table[address.getPageNo()];
        p.setActive(true);
        p.setAddress(frame);
        numberOfActivePages++;
    }
    
    public int deAllocate(VirtualAddress address) throws PageFaultException{
        Page p = table[address.getPageNo()];
        p.setActive(false);
        numberOfActivePages--;
        return p.getAddress();
    }
    
    public int deAllocate(int page){
        Page p = table[page];
        p.setActive(false);
        return p.getAddress();
    }
}
