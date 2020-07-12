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

    private Dungeon dungeon;
    private Key key = null;
    private int treasure = 0;

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

    public int getTreasure() {
        return treasure;
    }

    public void setTreasure() {
        treasure++;
    }

    public Door getKeyDoor() {
        return key.getDoor();
    }

    /**
     * Notify the Dungeon Loader to change the image of the closed door to an open door
     * @param door
     */
    public void open(Door door) {
        key = null;
        dungeon.open(door);
    }

    /**
     * Notify the Dungeon Loader to make the item disappear after being picked up
     * @param pickupable
     */
    public void pickup(Pickupable pickupable) {
        dungeon.pickup(pickupable);
    }

    /**
     * Get all the entities of the same type or implment the same interface
     * @param entityType
     * @return a list of entities of a given type
     */
    private List<Entity> getEntities(Class<?> entityType) {
        return dungeon.getEntities().stream().filter(entity -> entityType.isAssignableFrom(entity.getClass())).collect(Collectors.toList());
    }

    /**
     * If the player is currently on the given entity,
     * returns the entity,
     * otherwise throws noSuchElementException
     * @return the entity the player is on right now
     * @throws noSuchElementException
     */
    private Entity getEntity(Class<?> entityType) {
        return getEntities(entityType).stream().filter(entity -> this.isOn(entity)).findFirst().get();
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
     * given a portal set the player's position to its corresponding portal
     * @param portal
     */
    private void teleport(Portal portal) {
        setPosition(x(), portal.getPortal().getX());
        setPosition(y(), portal.getPortal().getY());
    }

    /**
     * Take certain actions depending on the corresponding entity that the player stepped on
     * @param coordinate a x or y value the the player
     * @param position the previous x or y value before the player took the move
     */
    private void action(IntegerProperty coordinate, int position) {
        if (isOn(Portal.class)) {
            teleport((Portal) getEntity(Portal.class));
        } else if (isOn(Blockable.class)) {
            ((Blockable)getEntity(Blockable.class)).block(this, coordinate, position);
        } else if (isOn(Pickupable.class)) {
            ((Pickupable)getEntity(Pickupable.class)).pickup(this);
        } else if (isOn(Exit.class)) {
            System.out.println("Game over!! Woo!");
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
