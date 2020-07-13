package unsw.dungeon;

public class Potion extends Entity implements Pickupable {
    private long time = 0;

    public Potion(int x, int y) {
        super(x, y);
    }

    /**
     * Record the time of pickup in milliseconds
     * @return this potion
     */
    public Potion pickup(Potion potion) {
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