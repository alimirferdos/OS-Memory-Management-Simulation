/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.kntu.os.mem;

import java.util.Date;
import java.util.Random;

public class Util{
    private static final Random RANDOM = new Random(new Date().getTime());

    public static int getNextRandom(int maxNumber){
        return RANDOM.nextInt(maxNumber);
    }

}
