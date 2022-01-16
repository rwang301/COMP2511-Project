package unsw.dungeon;

public class Potion extends Entity implements Pickupable {

    private long startTime;

    public Potion(int x, int y) {
        super(x, y);
    }

    public boolean isTimeOver() {
        return System.currentTimeMillis() > this.startTime + 5000;
    }

    public long getStartTime() {
        return startTime;
    }

    @Override
    public void pickup(Player player, Dungeon dungeon) {
        if (player.getPotion() == null) {
            this.startTime = System.currentTimeMillis();
        } else {
            this.startTime = player.getPotion().isTimeOver() ? System.currentTimeMillis() : player.getPotion().getStartTime() + 5000;
        }
        player.setPotion(this);
        dungeon.disappear(this);
    }
}
