package unsw.dungeon;

import java.util.Timer;
import java.util.TimerTask;

public class Potion extends Entity implements Pickupable {
    private long pickupTime = 0;
    private long effectTime = 5000;
    private Timer timer;

    public Potion(int x, int y) {
        super(x, y);
    }

    /**
     * Record the time of pickup in milliseconds.
     * If the player already had a potion record the time as the existing potion plus 5 seconds to extend the effect time
     * @return this potion
     */
    public Potion pickup(Potion potion, Backpack backpack) {
        pickupTime = System.currentTimeMillis();
        if (potion != null) {
            effectTime += potion.effectTime - (System.currentTimeMillis() - potion.pickupTime);
            potion.cancelTimer();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                backpack.setPotion(null);
            }
        }, effectTime);
        return this;
    }

    private void cancelTimer() {
        timer.cancel();
        timer.purge();
    }
}