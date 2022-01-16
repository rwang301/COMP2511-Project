package unsw.dungeon;

public class Sword extends Entity implements Pickupable {

    private int hits = 5;

    public Sword(int x, int y) {
        super(x, y);
    }

    public void setHits() {
        this.hits = hits - 1;
    }

    public int getHits() {
        return hits;
    }

    public boolean capable() {
        return hits > 0;
    }

    @Override
    public void pickup(Player player, Dungeon dungeon) {
        if (player.getSword() == null) {
            player.setSword(this);
            dungeon.disappear(this);
        }
    }

    
    
}
