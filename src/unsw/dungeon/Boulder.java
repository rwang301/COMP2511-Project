package unsw.dungeon;

import javafx.beans.property.IntegerProperty;

public class Boulder extends Entity implements Blockable {
    public Boulder(int x, int y) {
        super(x, y);
    }

    @Override
    public void block(Player p, IntegerProperty coordinate, int position) {
        p.setPosition(coordinate, position);
    }

    public void push(Dungeon d, int x, int y) {
        if (d.isBlocked(x, y)) {
            this.x().set(x);
            this.y().set(y);
            d.complete();
        }
    }
}
