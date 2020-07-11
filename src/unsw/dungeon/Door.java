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

    public boolean isOpen() {
        return open;
    }

    public void open(Player player) {
        open = !open;
        player.setKey(null);
    }

    /**
     * Check if the player can open a door
     * @param door
     * @return true if the door is already open or the player met all the conditions of openning a door otherwise false
     */
    private boolean canOpen(Player player) {
        if (!isOpen()) {
            if (player.getKey() == null) return false;
            else if (player.getKeyDoor() == this) open(player);
            else return false;
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