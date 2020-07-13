package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.IntegerProperty;

/**
 * The player entity
 * 
 * @author Robert Clifton-Everest
 *
 */
public class Player extends Entity implements Subject {

    /**
     * Stores the entity that the player is currently on
     */
    private Entity current;
    private Dungeon dungeon;
    private Key key = null;
    private int treasure = 0;
    private Potion potion = null;
    private Sword sword = null;
    ArrayList<Observer> listObservers = new ArrayList<Observer>();
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

    public void setPotion(Potion p) {
        this.potion = p;
    }

    public Potion getPotion(){
        return potion;
    }

    public Sword getSword() {
        return sword;
    }

    public void setSword(Sword sword) {
        this.sword = sword;
    }

    public Door getKeyDoor() {
        return key.getDoor();
    }

    public int getTotalTreasure() {
        return dungeon.getTreasure();
    }

    @Override
	public void attach(Observer o) {
		if(! listObservers.contains(o)) { listObservers.add(o); }
	};

	@Override
	public void detach(Observer o) {
		listObservers.remove(o);
	}

	@Override
	public void notifyObservers() {
		for(Observer obs : listObservers) {
			obs.update(this);
		}
	}

    /**
     * Check if the goal of this dungeon is met
     */
    public void complete() {
        dungeon.complete();
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
     * Check whether the player is on a given type of entity
     * @param entityType
     * @return true if the player is on the given type of entity otherwise false
     */
    public boolean isOn(Class<?> entityType) {
        for (Entity entity: getEntities(entityType)) {
            if (this.isOn(entity)) {
                current = entity;
                return true;
            }
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
            teleport((Portal)current);
        } else if (isOn(Blockable.class)) {
            ((Blockable)current).block(this, coordinate, position);
        } else if (isOn(Pickupable.class)) {
            ((Pickupable)current).pickup(this);
        } else if (isOn(Exit.class)) {
            complete();
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
