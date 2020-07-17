package unsw.dungeon;

import javafx.beans.property.IntegerProperty;

public class Door extends Entity implements Blockable {
    private boolean open = false;

    public Door(int x, int y) {
        super(x, y);
    }

    public boolean isOpen() {
        return open;
    }

    /**
     * Check if the player can open the door
     * @param door
     * @return true if the door is already open or the player has the complementary key otherwise false
     */
    private boolean canOpen(Player player) {
        if (!open) {
            if (player.getKey() != null && player.getKeyDoor() == this) {
                open = true;
                player.open(this);
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
}