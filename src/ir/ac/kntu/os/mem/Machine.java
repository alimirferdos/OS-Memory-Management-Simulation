/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.kntu.os.mem;

public class Machine {
    public static void main(String[] args){
        //OS os = new OS();
        //IPT_OS os = new IPT_OS();
        ConcurrentOS os = new ConcurrentOS();

        os.doStartup();
    }
}
