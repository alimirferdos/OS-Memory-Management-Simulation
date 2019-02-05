/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.kntu.os.mem;

import java.util.*;

/**
 *
 * @author Ali
 */
public class TLBCache{
    private HashMap<Integer, Page> hashmap;
    Deque<Integer> dq;
    int LRU_SIZE;
    
    public TLBCache(int size) {
        LRU_SIZE = size;
        dq = new LinkedList<>(); 
        hashmap = new HashMap<>(); 
    }

    public Page getNode(int key) throws PageFaultException {
        if (hashmap.containsKey(key)) {
            // Key Already Exist, just update the index
            dq.remove(key);
            dq.push(key);
            return hashmap.get(key);
        }
        throw new PageFaultException("Page Not Found in Cache!");
    }

    public void putNode(int key, Page value) {
        if(!hashmap.containsKey(key)) {
            if(dq.size() == LRU_SIZE) { 
                int last = dq.removeLast(); 
                hashmap.remove(last); 
            } 
        } 
        else { 
            dq.remove(key); 
        } 
        dq.push(key); 
        hashmap.put(key, value);
    }
    /*
    public static void main(String[] args) throws java.lang.Exception {
            // your code goes here
            LRUCache lrucache = new LRUCache();
            lrucache.putNode(1, 1);
            lrucache.putNode(10, 15);
            lrucache.putNode(15, 10);
            lrucache.putNode(10, 16);
            lrucache.putNode(12, 15);
            lrucache.putNode(18, 10);
            lrucache.putNode(13, 16);

            System.out.println(lrucache.getNode(1));
            System.out.println(lrucache.getNode(10));
            System.out.println(lrucache.getNode(15));

    }*/
} 