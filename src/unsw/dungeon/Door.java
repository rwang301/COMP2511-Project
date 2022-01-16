package unsw.dungeon;

import javafx.beans.property.IntegerProperty;

public class Door extends Entity implements Blockable {

    private Key key = null;
    private Boolean isOpen = false;

    public Door(int x, int y) {
        super(x, y);
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public Key getKey() {
        return key;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    public void openDoor(Player p, Dungeon d) {
        isOpen = true;
        d.open(this);
        p.setKey(null);
    }

    private boolean canEnter(Player p) {
        if (!isOpen) {
            if (p.getKey() == key) {
                openDoor(p, p.getDungeon());
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public void block(Player p, IntegerProperty coordinate, int position) {
        if (!canEnter(p)) {
            p.setPosition(coordinate, position);
        }
    }
}
