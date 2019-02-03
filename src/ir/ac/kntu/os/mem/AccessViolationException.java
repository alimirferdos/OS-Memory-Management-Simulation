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
public class AccessViolationException extends Exception{
    AccessViolationException(String msg){
        super(msg);
    }
}
