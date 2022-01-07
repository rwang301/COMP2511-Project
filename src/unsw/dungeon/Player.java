package unsw.dungeon;

import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.IntegerProperty;

/**
 * The player entity
 * @author Robert Clifton-Everest
 *
 */
public class Player extends Entity {

    private Dungeon dungeon;
    private Key key;

    /**
     * Create a player positioned in square (x,y)
     * @param x
     * @param y
     */
    public Player(Dungeon dungeon, int x, int y) {
        super(x, y);
        this.dungeon = dungeon;
    }
    
    public void moveDown() {
        if (getY() < dungeon.getHeight() - 1)
            y().set(getY() + 1);
        action(y(), getY() - 1);
    }
    
    public void moveLeft() {
        if (getX() > 0)
            x().set(getX() - 1);
        action(x(), getX() + 1);
    }
    
    public void moveRight() {
        if (getX() < dungeon.getWidth() - 1)
            x().set(getX() + 1);
        action(x(), getX() - 1);
    }
    
    public void moveUp() {
        if (getY() > 0)
            y().set(getY() - 1);
        action(y(), getY() + 1);
    }

    public void setPosition(IntegerProperty coordinate, int position) {
        coordinate.set(position);
    }

    private void action(IntegerProperty coordinate, int position) {
        
        if (isOn(Blockable.class)) {
            // if (isOn(Door.class)) {
            //     if (!canEnter((Door) getCurrEntity(Door.class))) {
            //         coordinate.set(position);
            //     }
            // } else {
            //     coordinate.set(position);
            // }
            ((Blockable)getCurrEntity(Blockable.class)).block(this, coordinate, position);
        } else if (isOn(Portal.class)) {
            portalTeleport();
        } else if (isOn(Key.class)) {
            if (key == null) ((Key)getCurrEntity(Key.class)).pickup(this);
        }
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    private boolean isOn(Class<?> entityType) {
        for (Entity e : getEntityType(entityType)) {
            if (this.getX() == e.getX() && this.getY() == e.getY()) {
                return true;
            }
        }
        return false;
    }

    private void portalTeleport() {
        Portal matchingPortal = ((Portal) getCurrEntity(Portal.class)).getMatchingPortal();
        x().set(matchingPortal.getX());
        y().set(matchingPortal.getY());
    }

    private Entity getCurrEntity(Class<?> entityType) {
        return getEntityType(entityType).stream().filter(e -> e.isOn(this)).findFirst().get();
    }

    private List<Entity> getEntityType(Class<?> entityType) {
        return dungeon.getEntities().stream().filter(e -> entityType.isAssignableFrom(e.getClass()))
                .collect(Collectors.toList());
    }
}
