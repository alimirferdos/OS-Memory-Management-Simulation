/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.kntu.os.mem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Ali
 */
public class InvertedPageTable implements IPageTable{
    private int numberOfPageFaults[];
    private int numberOfActivePages[];
    private int numberOfProcesses;
    
    private HashMap<Integer, Page> table;
    
    public InvertedPageTable() {
        table = new HashMap<>(1024);
        for(int i = 0; i < 1024; i++){
            table.put(i, new Page(-1, -1));
            table.get(i).setAddress(i);
        }
        numberOfProcesses = 0;
    }
    
    public int getNumberOfProcesses() { return numberOfProcesses; }

    public void setNumberOfProcesses(int num) {
        this.numberOfProcesses = num;
        this.numberOfActivePages = new int[num];
        this.numberOfPageFaults = new int[num];
    }

    //public int getPid() { return process.pid; }

    @Override
    public int getNumberOfPageFaults() { 
        int num = 0;
        for (int i = 0; i < numberOfProcesses; i++) {
            num += numberOfPageFaults[i];
        }
        return num; 
    }

    @Override
    public int getNumberOfActivePages() { 
        int num = 0;
        for (int i = 0; i < numberOfProcesses; i++) {
            num += numberOfActivePages[i];
        }
        return num; 
    }
    
    public int getNumberOfPageFaults(int i) { return numberOfPageFaults[i]; }

    public int getNumberOfActivePages(int i) { return numberOfActivePages[i]; }
    
    @Override
    public boolean isFull(){
        return getNumberOfActivePages() == 1024;
    }
    
    @Override
    public int translateAddress(VirtualAddress address, int pid) throws PageFaultException, AccessViolationException{
        addressValidationTest(address, pid);
        for (Page p : table.values()) {
            if(p.getPid() == pid && p.getPageNumber() == address.getPageNo()){
                if (p.isActive()) {
                    return (p.getAddress() << 10)+ address.getPageOffset();
                }
                break;
            }
        }
        numberOfPageFaults[pid]++;
        throw new AccessViolationException("process_" + pid + " wanted to access a not activated page.");
    }

    @Override
    public void allocate(VirtualAddress address, int frame, int pid) throws PageFaultException, AccessViolationException{
        Page p = table.get(frame);
        if(p.getPid() != pid && p.isActive()){
            throw new AccessViolationException("process_" + pid + " had an access violation.");
        }
        p.setActive(true);
        p.setPageNumber(address.getPageNo());
        p.setPid(pid);
        p.setAddress(frame);
        numberOfActivePages[pid]++;
    }
    
    @Override
    public int deAllocate(VirtualAddress address, int pid) throws PageFaultException{
        for (Page p : table.values()) {
            if(p.getPid() == pid && p.getPageNumber() == address.getPageNo()){
                p.setActive(false);
                if(numberOfActivePages[pid] > 0){
                    numberOfActivePages[pid]--;
                }
                return p.getAddress();
            }
        }
        throw new PageFaultException("process_" + pid + " wanted to access a wrong address.");
    }
    
    @Override
    public ArrayList<Integer> deAllocateAll(int pid){
        ArrayList<Integer> frames = new ArrayList<>();
        for (Page p : table.values()) {
            if(p.getPid() == pid){
                p.setActive(false);
                if(numberOfActivePages[pid] > 0){
                    numberOfActivePages[pid]--;
                }
                frames.add(p.getAddress());
            }
        }
        return frames;
    }
    
    @Override
    public void addressValidationTest(VirtualAddress address, int pid) throws PageFaultException{
        int pageAddr = address.getPageNo();
        int addrOffset = address.getPageOffset();
        if(pageAddr >= 128 || addrOffset >= 2048){
            numberOfPageFaults[pid]++;
            throw new PageFaultException("process_" + pid + " wanted to access a wrong address.");
        }
    }

    boolean isEmpty() {
        return numberOfProcesses == 0;
    }
    
}
