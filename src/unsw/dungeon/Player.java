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
     * starting at the player itself
     */
    private Entity current = this;
    private Dungeon dungeon;
    private Backpack backpack = new Backpack();
    private List<Observer> enemies = new ArrayList<Observer>();

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

    public List<Observer> getEnemies() {
        return enemies;
    }

    public int getTreasure() {
        return backpack.getTreasure();
    }

    public Key getKey() {
        return backpack.getKey();
    }

    public void setKey(Key key) {
        backpack.setKey(key);
    }

    public Potion getPotion(){
        return backpack.getPotion();
    }

    public void setPotion(Potion potion) {
        backpack.setPotion(potion);
    }

    public Sword getSword() {
        return backpack.getSword();
    }

    public void setSword(Sword sword) {
        backpack.setSword(sword);
    }

    public Door getKeyDoor() {
        return backpack.getKeyDoor();
    }

    public int getTotalTreasure() {
        return dungeon.getTreasure();
    }


    boolean checkSwitches() {
        for (Entity floorSwitch: getEntities(Switch.class)) {
            if (!((Switch)floorSwitch).isTriggered()) return false;
        }
        return true;
	}

    /**
     * Reduce the times the sword can be used when hitting an enemy
     */
    void hit() {
        backpack.hit();
    }

    /**
     * Remove the enemy entity when killed
     * @param enemy
     */
    void kill(Enemy enemy) {
        enemy.cancelTimer();
        disappear(enemy);
        if (getPotion() != null) getPotion().detach(enemy);
        detach(enemy);
        complete();
    }

    /**
     * Inform the dungeon that the player is dead
     */
    void die() {
        dungeon.complete(true);
    }    

    /**
     * Check if the goal of this dungeon is met
     */
    void complete() {
        dungeon.complete(false);
    }

    /**
     * Notify the Dungeon Loader to change the image of the closed door to an open door
     * @param door
     */
    public void open(Door door) {
        backpack.setKey(null);
        dungeon.open(door);
    }

    /**
     * Notify the Dungeon Loader to make an entity disappear
     * @param entity
     */
    public void disappear(Entity entity) {
        dungeon.disappear(entity);
    }

    /**
     * Get all the entities of the same type or implement the same interface
     * @param entityType
     * @return a list of entities of a given type
     */
    public List<Entity> getEntities(Class<?> entityType) {
        return dungeon.getEntities().stream().filter(entity -> entityType.isAssignableFrom(entity.getClass())).collect(Collectors.toList());
    }

    /**
     * Check whether the player is on a given type of entity
     * @param entityType
     * @return true if the player is on the given type of entity otherwise false
     */
    boolean isOn(Class<?> entityType) {
        for (Entity entity: getEntities(entityType)) {
            if (this.isOn(entity)) {
                if (entity.getClass() == Door.class && ((Door)entity).isOpen()) continue;
                current = entity;
                return true;
            }
        }
        return false;
    }

    /**
     * Check whether a given coordinate has entities on it
     * @param x
     * @param y
     * @return true if there's an entity on the given coordinate except for a floor switch otherwise false
     */
    boolean hasEntity(int x, int y) {
        boolean hasBlockable = false;
        for (Entity entity: dungeon.getEntities()) {
            if (entity.getX() == x && entity.getY() == y) {
                if (entity.getClass() == Door.class) hasBlockable = !((Door)entity).isOpen();
                else if (entity.getClass() == Switch.class && !hasBlockable) hasBlockable = false;
                else hasBlockable = true;
            }
        }
        return hasBlockable;
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
     * Add a pickupable item to the backpack if the player does not have one already
     * @param pickupable
     */
    private void pickup() {
        if (backpack.noItem((Pickupable)current)) {
            backpack.setItem((Pickupable)current, this);
            disappear(current);
        }
    }

    /**
     * Take certain actions depending on the corresponding entity that the player stepped on
     * @param coordinate current x or y value of the player
     * @param position the previous x or y value before the player took the move
     */
    private void action(IntegerProperty coordinate, int position) {
        notifyObservers(); // notify the enemies every time the player moves
        if (isOn(Portal.class)) {
            teleport((Portal)current);
        } else if (isOn(Blockable.class)) {
            ((Blockable)current).block(this, coordinate, position);
        } else if (isOn(Pickupable.class)) {
            pickup();
        } else if (isOn(Exit.class)) {
            complete();
        } else if (isOn(Enemy.class)) {
            ((Enemy)current).collide(this);
        }
        // TODO walk on top of switches
    }


    public void moveUp() {
        if (getY() > 0) {
            y().set(getY() - 1);
            action(y(), getY() + 1);
        }
    }

    public void moveDown() {
        if (getY() < dungeon.getHeight() - 1) {
            y().set(getY() + 1);
            action(y(), getY() - 1);
        }
    }

    public void moveLeft() {
        if (getX() > 0) {
            x().set(getX() - 1);
            action(x(), getX() + 1);
        }
    }

    public void moveRight() {
        if (getX() < dungeon.getWidth() - 1) {
            x().set(getX() + 1);
            action(x(), getX() - 1);
        }
    }

    /**
     * The adjacent position of player to the boulder determines what direction it moves
     */
    public void moveBoulder(String direction) {
        for (Entity entity: getEntities(Boulder.class)) {
            if (this.getY() == entity.getY() && (this.getX() - entity.getX() == 1)) {
                if (direction.equals("left")) ((Boulder)entity).push(this, direction);
            } else if (this.getY() == entity.getY() && (this.getX() - entity.getX() == -1)) {
                if (direction.equals("right")) ((Boulder)entity).push(this, direction);
            } else if ((this.getY() - entity.getY() == -1) && this.getX() == entity.getX()) {
                if (direction.equals("down")) ((Boulder)entity).push(this, direction);
            } else if ((this.getY() - entity.getY() == 1) && this.getX() == entity.getX()) {
                if (direction.equals("up")) ((Boulder)entity).push(this, direction);
            }
        }
    }


    @Override
	public void attach(Observer enemy) {
        enemies.add(enemy);
	};

	@Override
	public void detach(Observer enemy) {
		enemies.remove(enemy);
	}

	@Override
	public void notifyObservers() {
        if (isOn(Potion.class)) enemies.forEach(enemy -> enemy.update(this));
        else enemies.forEach(enemy -> ((Enemy)enemy).reset());
	}
}
