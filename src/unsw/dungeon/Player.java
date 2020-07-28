package unsw.dungeon;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
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
     * Stores the entity that the player is currently on starting at the player itself
     */
    private Entity current = this;
    private Dungeon dungeon;
    private Backpack backpack = new Backpack();
    private List<Observer> enemies = new CopyOnWriteArrayList<>();
    private List<Observer> gnomes = new CopyOnWriteArrayList<>();
    private Observer hound;
    private int lives = 1;
    private int startingX;
    private int startingY;
    private Entity currPosition;
    private Entity prevPosition;

    /**
     * Create a player positioned in square (x,y)
     * @param x
     * @param y
     */
    public Player(Dungeon dungeon, int x, int y) {
        super(x, y);
        this.dungeon = dungeon;
        this.startingX = x;
        this.startingY = y;
    }

    public List<Observer> getEnemies() {
        return enemies;
    }

    public List<Observer> getGnomes() {
        return gnomes;
    }

    public Hound getHound() {
        return (Hound) hound;
    }

    public int getTreasure() {
        return backpack.getTreasure();
    }

    public Key getKey() {
        return backpack.getKey();
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

    public Door getKeyDoor() {
        return backpack.getKeyDoor();
    }

    public int getTotalTreasure() {
        return dungeon.getTreasure();
    }

    public void setCurrPosition(Entity currPosition) {
        this.currPosition = currPosition;
    }

    public Entity getCurrPosition() {
        return currPosition;
    }

    public Entity getPrevPosition() {
        return prevPosition;
    }


    /**
     * Add one life if not full
     * @return true if one life is added otherwise false
     */
    boolean setLives() {
        if (lives < 3) {
            lives++;
            return true;
        }
        return false;
    }

    /**
     * Check all switches are triggered
     * @return true if all switches are triggered otherwise false
     */
    boolean checkSwitches() {
        for (Entity floorSwitch: getEntities(Switch.class)) {
            if (!((Switch) floorSwitch).isTriggered()) return false;
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
    void kill(Character character) {
        if (character.getClass() == Enemy.class) {
            Enemy enemy = (Enemy) character;
            enemy.cancelTimer();
            if (getPotion() != null) getPotion().detach(enemy);
            complete();
        }
        disappear(character);
        detach(character);
    }

    /**
     * Inform the dungeon that the player is dead
     */
    void die() {
        lives--;
        setPosition(x(), startingX);
        setPosition(y(), startingY);
        detach(hound);
        if (lives == 0) dungeon.complete(true);
    }

    void sacrifice() {
        disappear((Entity) hound);
        detach(hound);
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
    void open(Door door) {
        backpack.setKey(null);
        dungeon.open(door);
    }

    /**
     * Notify the Dungeon Loader to make an entity disappear
     * @param entity
     */
    void disappear(Entity entity) {
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
                if (entity.getClass() == Door.class && ((Door) entity).isOpen()) continue;
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
                if (entity.getClass() == Door.class) hasBlockable = !((Door) entity).isOpen();
                else if (entity.getClass() == Switch.class && !hasBlockable) hasBlockable = false;
                else hasBlockable = true;
            }
        }
        return hasBlockable;
    }

    /**
     * Check if the player is within the gnome's range
     * @param gnome
     * @return true if the absolute path between the player and the gnome is within the range otherwise false
     */
    boolean withinRange(Gnome gnome) {
        if (Math.abs(gnome.getX() - getX()) + Math.abs(gnome.getY() - getY()) <= gnome.getRange()) return true;
        else return false;
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
        if (backpack.noItem((Pickupable)current, this)) {
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
        if (isOn(Portal.class)) {
            teleport((Portal) current);
        } else if (isOn(Blockable.class)) {
            ((Blockable) current).block(this, coordinate, position);
        } else if (isOn(Pickupable.class)) {
            pickup();
        } else if (isOn(Exit.class)) {
            complete();
        } else if (isOn(Enemy.class)) {
            ((Enemy) current).collide(this);
        } else if (isOn(Hound.class)) {
            if (hound == null) ((Hound) current).initialise(this);
        }
        notifyObservers(); // notify all the characters every time the player moves
        // TODO walk on top of switches
    }


    public void moveUp() {
        if (getY() > 0) {
            prevPosition = currPosition;
            currPosition = new Entity(getX(), getY());
            y().set(getY() - 1);
            action(y(), getY() + 1);
        }
    }

    public void moveDown() {
        if (getY() < dungeon.getHeight() - 1) {
            prevPosition = currPosition;
            currPosition = new Entity(getX(), getY());
            y().set(getY() + 1);
            action(y(), getY() - 1);
        }
    }

    public void moveLeft() {
        if (getX() > 0) {
            prevPosition = currPosition;
            currPosition = new Entity(getX(), getY());
            x().set(getX() - 1);
            action(x(), getX() + 1);
        }
    }

    public void moveRight() {
        if (getX() < dungeon.getWidth() - 1) {
            prevPosition = currPosition;
            currPosition = new Entity(getX(), getY());
            x().set(getX() + 1);
            action(x(), getX() - 1);
        }
    }

    /**
     * Move the boulder according to the direction
     * @param direction
     */
    public void moveBoulder(String direction) {
        for (Entity entity: getEntities(Boulder.class)) {
            if (this.getY() == entity.getY() && (this.getX() - entity.getX() == 1)) {
                if (direction.equals("left")) ((Boulder) entity).push(this, direction);
            } else if (this.getY() == entity.getY() && (this.getX() - entity.getX() == -1)) {
                if (direction.equals("right")) ((Boulder) entity).push(this, direction);
            } else if ((this.getY() - entity.getY() == -1) && this.getX() == entity.getX()) {
                if (direction.equals("down")) ((Boulder) entity).push(this, direction);
            } else if ((this.getY() - entity.getY() == 1) && this.getX() == entity.getX()) {
                if (direction.equals("up")) ((Boulder) entity).push(this, direction);
            }
        }
    }


    @Override
	public void attach(Observer observer) {
        if (observer.getClass() == Enemy.class) enemies.add(observer);
        else if (observer.getClass() == Gnome.class) gnomes.add(observer);
        else if (observer.getClass() == Hound.class) hound = observer;
	};

	@Override
	public void detach(Observer observer) {
        if (observer != null) {
            if (observer.getClass() == Enemy.class) enemies.remove(observer);
            if (observer.getClass() == Gnome.class) gnomes.remove(observer);
            else if (observer.getClass() == Hound.class) hound = null;
        }
	}

	@Override
	public void notifyObservers() {
        // TODO potential bug using current gone
        if (current.getClass() == Potion.class) enemies.forEach(enemy -> enemy.update(this));
        enemies.forEach(enemy -> ((Enemy) enemy).reset());

        if (hound != null) hound.update(this); // Move the hound first
        gnomes.forEach(gnome -> gnome.update(this)); // Then move the gnome
        // If gnome moves to the new position of hound then the hound dies
	}
}
