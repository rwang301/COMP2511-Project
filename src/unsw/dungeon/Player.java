package unsw.dungeon;

import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.IntegerProperty;

/**
 * The player entity
 * 
 * @author Robert Clifton-Everest
 *
 */
public class Player extends Entity {

    private Key key;
    private Dungeon dungeon;

    /**
     * Create a player positioned in square (x,y)
     * @param x
     * @param y
     */
    public Player(Dungeon dungeon, int x, int y) {
        super(x, y);
        this.dungeon = dungeon;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public void setDungeon(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    private List<Entity> getEntities(Class<?> entityType) {
        return dungeon.getEntities().stream().filter(entity -> entity.getClass() == entityType).collect(Collectors.toList());
    }

    /**
     * Check whether the player is on a given type of entity
     * @param entityType
     * @return true if the player is on the given type of entity otherwise false
     */
    private boolean isOn(Class<?> entityType) {
        for (Entity entity: getEntities(entityType)) {
            if (this.isOn(entity)) return true;
        }
        return false;
    }

    /**
     * If the player is currently on the given entity,
     * returns the entity,
     * otherwise throws noSuchElementException
     * @return the entity the player is on right now
     * @throws noSuchElementException
     */
    private Entity getEntity(Class<?> entityType) {
        return getEntities(entityType).stream().filter(entity -> entity.isOn(this)).findFirst().get();
    }

    private void setPosition(IntegerProperty coordinate, int position) {
        coordinate.set(position);
    }

    /**
     * given a portal set the player's position to its corresponding portal
     * @param portal
     */
    private void teleport(Portal portal) {
        setPosition(x(), portal.getPortal().getX());
        setPosition(y(), portal.getPortal().getY());
    }

    private boolean canEnter(Door door) {
        if (!door.isOpen()) {
            if (key == null) return false;
            else if (key.getDoor() == door) door.open(this);
            else return false;
        }
        return true;
    }

    private void action(IntegerProperty coordinate, int position) {
        if (isOn(Wall.class)) {
            setPosition(coordinate, position);
        } else if (isOn(Portal.class)) {
            teleport((Portal) getEntity(Portal.class));
        } else if (isOn(Key.class)) {
            Key pickupable = (Key) getEntity(Key.class);
            pickupable.pickup(this);
        } else if (isOn(Door.class)) {
            if (!canEnter((Door) getEntity(Door.class))) {
                setPosition(coordinate, position);
            }
        }
    }

    public void moveUp() {
        if (getY() > 0)
            y().set(getY() - 1);
        action(y(), getY() + 1);
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
}
