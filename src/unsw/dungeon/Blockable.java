package unsw.dungeon;

import javafx.beans.property.IntegerProperty;

public interface Blockable {
    public void block(Player p, IntegerProperty coordinate, int position);
}
