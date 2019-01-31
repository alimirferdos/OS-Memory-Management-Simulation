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

    public Page() { this.isActive = false; }

    public boolean isActive() { return isActive; }
    
    public void setActive(boolean active) { isActive = active; }
}
