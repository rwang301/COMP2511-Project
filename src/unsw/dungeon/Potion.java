package unsw.dungeon;

public class Potion extends Entity implements Pickupable {
    private long time = 0;
    public Potion(int x, int y) {
        super(x, y);
        time = System.currentTimeMillis();
    }
}