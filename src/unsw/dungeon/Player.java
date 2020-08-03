package unsw.dungeon;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import unsw.ui.DungeonController;

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
    private Observer dungeonController;
    private Pickupable use = null;
    private int currHealth;
    private int prevHealth;
    private final int startingHealth = 1;
    private final int healthCapacity = 3;
    private int startingX;
    private int startingY;
    private Entity currPosition;
    private Entity prevPosition;
    private IntegerProperty exit = new SimpleIntegerProperty(0);
    private IntegerProperty triggers = new SimpleIntegerProperty(0);
    private IntegerProperty kills = new SimpleIntegerProperty(0);

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
        currHealth = startingHealth;
        prevHealth = currHealth;
    }

    public List<Observer> getEnemies() {
        return enemies;
    }

    public List<Observer> getGnomes() {
        return gnomes;
    }

    void setCurrPosition(Entity currPosition) {
        this.currPosition = currPosition;
    }

    Entity getCurrPosition() {
        return currPosition;
    }

    Entity getPrevPosition() {
        return prevPosition;
    }

    public Pickupable getUse() {
        return use;
    }

    public void setUse(Pickupable use) {
        this.use = use;
    }

    public int getCurrHealth() {
        return currHealth;
    }

    public int getPrevHealth() {
        return prevHealth;
    }

    public void setPrevHealth(int prevHealth) {
        this.prevHealth = prevHealth;
    }

    void setCurrHealth() {
        currHealth++;
        notifyObservers();
    }

    Hound getHound() {
        return (Hound) hound;
    }

    public int getTreasure() {
        return backpack.getTreasure();
    }

    public IntegerProperty getExit() {
        return exit;
    }

    public void setExit(int exit) {
        this.exit.set(exit);
    }

    public IntegerProperty getTriggers() {
        return triggers;
    }

    public void setTriggers(int triggers) {
        this.triggers.set(this.triggers.get() + triggers);
    }

    public IntegerProperty getTreasureProperty() {
        return backpack.getTreasureProperty();
    }

    public IntegerProperty getHits() {
        return backpack.getHits();
    }

    public IntegerProperty getKills() {
        return kills;
    }

    public Key getKey() {
        return backpack.getKey();
    }

    public Potion getPotion(){
        return backpack.getPotion();
    }

    public LongProperty getTick() {
        return backpack.getTick();
    }

    public Sword getSword() {
        return backpack.getSword();
    }

    Door getKeyDoor() {
        return backpack.getKeyDoor();
    }

    int getTotalTreasure() {
        return dungeon.getTreasure();
    }

    void useupPotion(Potion potion) {
        use(potion);
        backpack.setPotion(null);
    }


    /**
     * Check if the player's lives are full
     * @return true if the player has less than 3 lives otherwise false
     */
    boolean canAddLive() {
        if (currHealth < healthCapacity) return true;
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

    void use(Pickupable item) {
        use = item;
        notifyObservers();
    }

    /**
     * Notify the Dungeon Loader to change the image of the closed door to an open door
     * @param door
     */
    void open(Door door) {
        use(getKey());
        backpack.setKey(null);
        dungeon.open(door);
    }

    /**
     * Reduce the times the sword can be used when hitting an enemy
     */
    void hit() {
        Platform.runLater(() -> backpack.hit(this));
    }

    /**
     * Remove the enemy entity when killed
     * @param enemy
     */
    void kill(Character character) {
        // TODO weird bug where potion calls this method twice
        kills.set(kills.get() + 1);
        disappear(character);
        detach(character);
        if (character.getClass() == Enemy.class) {
            Enemy enemy = (Enemy) character;
            enemy.cancelTimer();
            if (getPotion() != null) getPotion().detach(enemy);
            complete();
        }
    }

    /**
     * The player lost one live
     */
    void die() {
        if (currHealth == startingHealth) {
            dungeon.complete(true);
        } else {
            setPosition(x(), startingX);
            setPosition(y(), startingY);
            detach(hound);
        }
        currHealth--;
        notifyObservers();
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
    List<Entity> getEntities(Class<?> entityType) {
        return dungeon.getEntities(entityType);
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
            setExit(0);
            teleport((Portal) current);
        } else if (isOn(Blockable.class)) {
            setExit(0);
            ((Blockable) current).block(this, coordinate, position);
        } else if (isOn(Pickupable.class)) {
            setExit(0);
            pickup();
        } else if (isOn(Exit.class)) {
            setExit(1);
            complete();
        } else if (isOn(Character.class)) {
            setExit(0);
            ((Character) current).collide(this);
        } else {
            setExit(0);
        }
        notifyObservers(); // notify all the characters every time the player moves
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
        else if (observer.getClass() == DungeonController.class) dungeonController = observer;
	};

	@Override
	public void detach(Observer observer) {
        if (observer != null) {
            if (observer.getClass() == Enemy.class) enemies.remove(observer);
            else if (observer.getClass() == Gnome.class) gnomes.remove(observer);
            else if (observer.getClass() == Hound.class) hound = null;
            else if (observer.getClass() == DungeonController.class) dungeonController = null;
        }
	}

	@Override
	public void notifyObservers() {
        if (use != null || prevHealth != currHealth) {
            if (dungeonController != null) dungeonController.update(this);
        } else {
            if (getPotion() != null) enemies.forEach(enemy -> enemy.update(this));
            else enemies.forEach(enemy -> ((Enemy) enemy).reset());

            if (hound != null) hound.update(this); // Move the hound first
            gnomes.forEach(gnome -> gnome.update(this)); // Then move the gnome
            // If gnome moves to the new position of hound then the hound dies
        }
	}
}
