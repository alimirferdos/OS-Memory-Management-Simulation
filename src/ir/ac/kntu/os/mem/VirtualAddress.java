/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.kntu.os.mem;

public class VirtualAddress {
    private final int address;

    public VirtualAddress(int address){
        this.address = address & 0x0000FFFF;
    }


    public int getPageNo(){
        return (address & 0x03FF) >> 10;
    }

    public int getPageOffset(){
        return address & 0x03FF;
    }
}
