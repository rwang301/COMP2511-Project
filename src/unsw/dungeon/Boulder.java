package unsw.dungeon;

import javafx.beans.property.IntegerProperty;

public class Boulder extends Entity implements Blockable {

    public Boulder(int x, int y) {
        super(x, y);
    }

    /**
     * Check if the player can push the bouler
     * @param player
     * @return true if the boulder can be pushed in the direction given otherwise false
     */
    private boolean canPush(Player player, IntegerProperty coordinate, int position) {
        return false;
    }

    @Override
    public void block(Player player, IntegerProperty coordinate, int position) {
        if (!canPush(player, coordinate, position)) {
            player.setPosition(coordinate, position);
        } else {
            this.setPosition(coordinate, position);
        }
    }

}
