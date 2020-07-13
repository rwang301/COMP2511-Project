package unsw.dungeon;

public class Sword extends Entity implements Pickupable {
    private int hits = 0;
    public Sword(int x, int y) {
        super(x, y);
    }

    public void sethit() {
        this.hits =+ 1;
        return;
    }

    public int getHits() {
        return hits;
    }

    @Override
    public void pickup(Player player) {
        if (player.getSword() == null) {
            player.setSword(this);
            player.disappear(this);
        }
    }
}