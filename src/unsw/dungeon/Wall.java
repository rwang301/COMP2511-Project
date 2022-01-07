package unsw.dungeon;

import javafx.beans.property.IntegerProperty;

public class Wall extends Entity implements Blockable {

    public Wall(int x, int y) {
        super(x, y);
    }

    @Override
    public void block(Player p, IntegerProperty coordinate, int position) {
        p.setPosition(coordinate, position);
    }
}
