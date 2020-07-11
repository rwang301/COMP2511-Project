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

    /**
     * Get all the entities of all the same type
     * @param entityType
     * @return a list of entities of a given type
     */
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

    /**
     * Set the given coordinate to the given position
     * @param coordinate a x or a y value of an entity
     * @param position a new x or y value to set the corresponding coordinate to
     */
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

    /**
     * Check if the player can enter a door
     * @param door
     * @return true if the player met all the conditions of entering a door otherwise false
     */
    private boolean canEnter(Door door) {
        if (!door.isOpen()) {
            if (key == null) return false;
            else if (key.getDoor() == door) door.open(this);
            else return false;
        }
        return true;
    }

    /**
     * Take certain actions depending on the corresponding entity that the player stepped on
     * @param coordinate a x or y value the the player
     * @param position the previous x or y value before the player took the move
     */
    private void action(IntegerProperty coordinate, int position) {
        if (isOn(Wall.class)) {
            setPosition(coordinate, position);
        } else if (isOn(Portal.class)) {
            teleport((Portal) getEntity(Portal.class));
        } else if (isOn(Key.class)) {
            if (key == null) ((Key)getEntity(Key.class)).pickup(this);
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
