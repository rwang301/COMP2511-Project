package unsw.dungeon;

import javafx.beans.property.IntegerProperty;

public class Door extends Entity implements Blockable {
    private Key key = null;
    private boolean open = false;

    public Door(int x, int y) {
        super(x, y);
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    /**
     * Check if the player can open a door
     * @param door
     * @return true if the door is already open or the player has the complementary key otherwise false
     */
    private boolean canOpen(Player player) {
        if (!open) {
            if (player.getKey() != null && player.getKeyDoor() == this) {
                open = !open;
                player.setKey(null);
            } else return false;
        }
        return true;
    }

    @Override
    public void block(Player player, IntegerProperty coordinate, int position) {
        if (!canOpen(player)) {
            player.setPosition(coordinate, position);
        }
    }

    @Override
    public String toString() {
        return super.toString() + " [key=" + key + ", open=" + open + "]";
    }
}