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
public interface IPageTable {
    public int getNumberOfPageFaults();

    public int getNumberOfActivePages();
    
    public boolean isFull();
    
    public int translateAddress(VirtualAddress address, int pid) throws PageFaultException, AccessViolationException;

    public void allocate(VirtualAddress address, int frame, int pid) throws PageFaultException, AccessViolationException;
    
    public int deAllocate(VirtualAddress address, int pid) throws PageFaultException;
    
    public ArrayList<Integer> deAllocateAll(int pid);
    
    public void addressValidationTest(VirtualAddress address, int pid) throws PageFaultException;
}
