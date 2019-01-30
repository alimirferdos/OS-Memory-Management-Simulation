
package ir.ac.kntu.os.mem;

import java.util.Date;
import java.util.Random;

public class LocalityAwareRandomAddressGenerator{
    private class JumpLocality{
        public int percent;
        public int jumpBound;

        public JumpLocality(int percent, int jumpBound){
            this.percent = percent;
            this.jumpBound = jumpBound;
        }
    }

    public final JumpLocality[] localityPercent = new JumpLocality[]{
        new JumpLocality(50, 1),
        new JumpLocality(60, 128),
        new JumpLocality(85, 512),
        new JumpLocality(95, 1024),
        new JumpLocality(100, 2048),
    };

    private Random random = new Random(new Date().getTime());

    private int currentAddr;

    public LocalityAwareRandomAddressGenerator(int startAddr){
        this.currentAddr = startAddr;
    }

    public LocalityAwareRandomAddressGenerator(){
        this.currentAddr = 0;
    }

    public int getNextAddress(){
        if(currentAddr < 0)
            currentAddr  = 0;

        int nextInt = random.nextInt(101);

        for (JumpLocality jumpLocality : localityPercent){
            if(nextInt <= jumpLocality.percent){
                final int randomJump = random.nextInt(2 * jumpLocality.jumpBound)
                         - jumpLocality.jumpBound;

                currentAddr = currentAddr + randomJump;
            }
        }

        return currentAddr;
    }

    public static void main(String[] args){
        //Test Code

        LocalityAwareRandomAddressGenerator addressGenerator = new
 LocalityAwareRandomAddressGenerator(0);
        for (int i = 0; i < 1000; i++)
        {
            final int nextAddress = addressGenerator.getNextAddress();
            System.out.println("nextAddress = " + nextAddress);
        }
    }
}

