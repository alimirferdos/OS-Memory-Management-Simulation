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
public class Page {
    private boolean isActive;
    private int address;
    private int pid;
    private int pageNumber;

    public int getPageNumber() { return pageNumber; }

    public void setPageNumber(int pageNumber) { this.pageNumber = pageNumber; }

    public int getPid() { return pid; }

    public void setPid(int pid) { this.pid = pid;}

    public int getAddress() { return address; }

    public void setAddress(int address) { this.address = address; }

    public Page(int pid, int pageNumber) { 
        this.isActive = false;
        this.pid = pid;
        this.pageNumber = pageNumber;
    }

    public boolean isActive() { return isActive; }
    
    public void setActive(boolean active) { isActive = active; }

}
