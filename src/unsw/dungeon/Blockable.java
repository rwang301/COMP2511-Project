package unsw.dungeon;

import javafx.beans.property.IntegerProperty;

public interface Blockable {
    public default void block(Player player, IntegerProperty coordinate, int position) {
        player.setPosition(coordinate, position);
        player.setCurrPosition(player.getPrevPosition());
    }
}