package unsw.dungeon;

import javafx.beans.property.IntegerProperty;

public class Boulder extends Entity implements Blockable {

    public Boulder(int x, int y) {
        super(x, y);
    }

    @Override
    public void block(Player player, IntegerProperty coordinate, int position) {
        player.setPosition(coordinate, position);
    }

}
