package unsw.dungeon;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Sword extends Entity implements Pickupable {
    private IntegerProperty hits = new SimpleIntegerProperty(5);

    public Sword(int x, int y) {
        super(x, y);
    }

    int getHits() {
        return hits.get();
    }

    public IntegerProperty getHitsProperty() {
        return hits;
    }

    void setHit() {
        hits.set(getHits() - 1);
    }

    /**
     * Check if the sword is still capable of hitting enemies
     * @return true if the sword has not hit 5 enemies yet otherwise false
     */
    boolean capable() {
        return getHits() > 0;
    }
}
