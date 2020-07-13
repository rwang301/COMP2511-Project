package unsw.dungeon;

public class Potion extends Entity implements Pickupable {
    public Potion(int x, int y) {
        super(x, y);
    }

    @Override
    public void pickup(Player player) {
        if (player.getPotion() == null) {
            player.setPotion(this);
            player.pickup(this);
        }
    }
}