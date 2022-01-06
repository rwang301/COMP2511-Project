package unsw.dungeon;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * An entity in the dungeon.
 * @author Robert Clifton-Everest
 *
 */
public class Entity {

    // IntegerProperty is used so that changes to the entities position can be
    // externally observed.
    private IntegerProperty x, y;

    /**
     * Create an entity positioned in square (x,y)
     * @param x
     * @param y
     */
    public Entity(int x, int y) {
        this.x = new SimpleIntegerProperty(x);
        this.y = new SimpleIntegerProperty(y);
    }

    public IntegerProperty x() {
        return x;
    }

    public IntegerProperty y() {
        return y;
    }

    public int getY() {
        return y().get();
    }

    public int getX() {
        return x().get();
    }

    public boolean isOn(Entity e) {
        return this.getX() == e.getX() && this.getY() == e.getY();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this.getClass() == obj.getClass()) {
            Entity entity = (Entity) obj;
            return this.getX() == entity.getX() && this.getY() == entity.getY();
        }
        return false;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [" + getX() + ", " + getY() + "]";
    }
}
