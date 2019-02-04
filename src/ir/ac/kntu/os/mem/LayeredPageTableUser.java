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
public class LayeredPageTableUser implements IPageTable{
    LayeredPageTable page_table;
    
    LayeredPageTableUser(VProcess process){
        page_table = new LayeredPageTable(process);
    }
    
    @Override
    public int getNumberOfPageFaults() {
        return page_table.getNumberOfPageFaults();
    }

    @Override
    public int getNumberOfActivePages() {
        return page_table.getNumberOfActivePages();
    }

    @Override
    public boolean isFull() {
        return page_table.isFull();
    }

    @Override
    public int translateAddress(VirtualAddress address, int pid) throws PageFaultException, AccessViolationException {
        return page_table.translateAddress(address);
    }

    @Override
    public void allocate(VirtualAddress address, int frame, int pid) throws PageFaultException, AccessViolationException {
        page_table.allocate(address, frame);
    }

    @Override
    public int deAllocate(VirtualAddress address, int pid) throws PageFaultException {
        return page_table.deAllocate(address);
    }

    @Override
    public ArrayList<Integer> deAllocateAll(int pid) {
        return page_table.deAllocateAll();
    }

    @Override
    public void addressValidationTest(VirtualAddress address, int pid) throws PageFaultException {
        page_table.addressValidationTest(address);
    }
    
}
