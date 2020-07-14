package unsw.dungeon;

public class Potion extends Entity implements Pickupable {
    private long time = 0;

    public Potion(int x, int y) {
        super(x, y);
    }

    /**
     * Record the time of pickup in milliseconds.
     * If the player already had a potion record the time as the existing potion plus 5 seconds to extend the effect time
     * @return this potion
     */
    public Potion pickup(Potion potion) {
        // TODO set a timer
        time = (potion == null) ? System.currentTimeMillis() : potion.time + 5000;
        return this;
    }

    /**
     * Check if it has been 5 seconds since pickup
     * @return true if 5 seconds has passed otherwise false
     */
    public boolean timesUp() {
        return System.currentTimeMillis() - time > 5000;
    }
}