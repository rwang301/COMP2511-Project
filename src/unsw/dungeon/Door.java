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

    @Override
    public void block(Player player, IntegerProperty coordinate, int position) {
        if (player.getKey() != null && player.getKeyDoor() == this) {
            open = true;
            player.open(this);
        } else {
            player.setPosition(coordinate, position);
        }
    }
}