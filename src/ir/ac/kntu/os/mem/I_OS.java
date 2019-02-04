/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.kntu.os.mem;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author Ali
 */
public interface I_OS {

    public void doStartup();
    
    public void allocate(int pid, VirtualAddress address, int size);

    public void deAllocate(int pid, VirtualAddress address, int size);

    public void read(int pid, VirtualAddress address, int size);

    public void write(int pid, VirtualAddress address, int size);

    public void processFinished(int pid);
}
