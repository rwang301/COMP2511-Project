package unsw.dungeon;

import javafx.beans.property.IntegerProperty;

public interface Blockable {
    public void block(Player player, IntegerProperty coordinate, int position);
}