/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.kntu.os.mem;

import java.util.ArrayList;

/**
 *
 * @author Ali
 */
public class LayeredPageTable implements IPageTable{
    private final VProcess process;
    private int numberOfPageFaults;
    private int numberOfActivePages;
    private Page table[][];
    
    public LayeredPageTable(VProcess process) {
        table = new Page[16][16];
        for(int i = 0; i < 16; i++){
            for (int j = 0; j < 16; j++) {
                table[i][j] = new Page(); 
            }
        }
        numberOfPageFaults = 0;
        numberOfActivePages = 0;
        this.process = process;
    }

    //public int getPid() { return process.pid; }

    @Override
    public int getNumberOfPageFaults() { return numberOfPageFaults; }

    @Override
    public int getNumberOfActivePages() { return numberOfActivePages; }
    
    @Override
    public boolean isFull(){
        return numberOfActivePages == 128;
    }
    
    @Override
    public int translateAddress(VirtualAddress address) throws PageFaultException, AccessViolationException{
        addressValidationTest(address);
        Page accessed = table[address.getFirstLayer()][address.getSecondLayer()];
        if(accessed.isActive()){
            return (accessed.getAddress() << 10)+ address.getPageOffset();
        }
        else{
            numberOfPageFaults++;
            throw new AccessViolationException(process.getName()+ " wanted to access a not activated page.");
        }
    }

    @Override
    public void allocate(VirtualAddress address, int frame) throws PageFaultException{
        Page p = table[address.getFirstLayer()][address.getSecondLayer()];
        p.setActive(true);
        p.setAddress(frame);
        numberOfActivePages++;
    }
    
    @Override
    public int deAllocate(VirtualAddress address) throws PageFaultException{
        Page p = table[address.getFirstLayer()][address.getSecondLayer()];
        p.setActive(false);
        if(numberOfActivePages > 0){
            numberOfActivePages--;
        }
        return p.getAddress();
    }
    
    @Override
    public ArrayList<Integer> deAllocateAll(){
        ArrayList<Integer> frames = new ArrayList<>();
        for(int i = 0; i < 16; i++){
            for (int j = 0; j < 16; j++) {
                Page p = table[i][j];
                p.setActive(false);
                frames.add(p.getAddress());
            }
        }
        return frames;
    }
    
    @Override
    public void addressValidationTest(VirtualAddress address) throws PageFaultException{
        int firstLayerPageAddr = address.getFirstLayer();
        int secondLayerPageAddr = address.getSecondLayer();
        int addrOffset = address.getPageOffset();
        if(firstLayerPageAddr >= 16 || secondLayerPageAddr >= 16 || addrOffset >= 2048){
            numberOfPageFaults++;
            throw new PageFaultException(process.getName() + " wanted to access a wrong address.");
        }
    }
    
}
