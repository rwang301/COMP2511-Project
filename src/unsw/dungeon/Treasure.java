package unsw.dungeon;

public class Treasure extends Entity implements Pickupable {
    public Treasure(int x, int y) {
        super(x, y);
    }

    @Override
    public void pickup(Player player, Dungeon dungeon) {
        player.setTreasure();
        dungeon.disappear(this);
        dungeon.complete();
    }
}
