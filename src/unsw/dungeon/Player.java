package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.IntegerProperty;

/**
 * The player entity
 * @author Robert Clifton-Everest
 *
 */
public class Player extends Entity implements Subject {

    private Dungeon dungeon;
    private Key key;
    private int treasure = 0;
    private Sword sword = null;
    private Potion potion = null;
    private List<Observer> enemies = new ArrayList<>();

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

    private void action(IntegerProperty coordinate, int position) {
        notifyObservers();
        if (isOn(Blockable.class)) {
            ((Blockable)getCurrEntity(Blockable.class)).block(this, coordinate, position);
        } else if (isOn(Portal.class)) {
            portalTeleport();
        } else if (isOn(Pickupable.class)) {
            if (key == null || !isOn(Key.class)) ((Pickupable)getCurrEntity(Pickupable.class)).pickup(this, dungeon);
        } else if (isOn(Exit.class)) {
            this.dungeon.complete();
        } else if (isOn(Enemy.class)) {
            ((Enemy)getCurrEntity(Enemy.class)).collide(this, this.getDungeon());
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

    public int getTreasure() {
        return treasure;
    }

    public int getTreasureGoal() {
        return this.dungeon.getTotalTreasure();
    }

    public void setTreasure() {
        this.treasure = treasure + 1;
    }

    public Sword getSword() {
        return sword;
    }

    public void setSword(Sword sword) {
        this.sword = sword;
    }

    public void hit() {
        if (this.sword.capable()) {
            this.sword.setHits();
        } else {
            this.sword = null;
        }
    }

    public Potion getPotion() {
        return this.potion;
    }

    public void setPotion(Potion potion) {
        this.potion = potion;
    }

    public boolean isOn(Class<?> entityType) {
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

    public List<Entity> getEntityType(Class<?> entityType) {
        return dungeon.getEntities().stream().filter(e -> entityType.isAssignableFrom(e.getClass()))
                .collect(Collectors.toList());
    }

    public void moveBoulder(Direction direction) {
        Boulder boulder = null;
        switch (direction) {
            case UP:
                boulder = (Boulder)getEntity(Boulder.class, this.getX(), this.getY() - 1); 
                if (boulder != null) {
                    boulder.push(this, this.getX(), this.getY() - 2);
                }
                break;
            case DOWN:
                boulder = (Boulder)getEntity(Boulder.class, this.getX(), this.getY() + 1); 
                if (boulder != null) {
                    boulder.push(this, this.getX(), this.getY() + 2);
                }
                break;
            case LEFT:
                boulder = (Boulder)getEntity(Boulder.class, this.getX() - 1, this.getY()); 
                if (boulder != null) {
                    boulder.push(this, this.getX() - 2, this.getY());
                }
                break;
            case RIGHT:
                boulder = (Boulder)getEntity(Boulder.class, this.getX() + 1, this.getY()); 
                if (boulder != null) {
                    boulder.push(this, this.getX() + 2, this.getY());
                }
                break;
        }
    }

    public Entity getEntity(Class<?> entityType, int x, int y) {
        List<Entity> entities = getEntityType(entityType);
        for (Entity e : entities) {
            if (e.getX() == x && e.getY() == y) return e;
        }
        return null;
    }

    public boolean canMoveBoulder(int x, int y) {
        for (Entity e: this.dungeon.getEntities()) {
            if (e.getX() == x && e.getY() == y) {
                if (e.getClass() == Door.class) {
                    return ((Door)e).getIsOpen();
                } else if (e.getClass() == Switch.class) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void attach(Observer o) {
        enemies.add(o);
    }

    @Override
    public void detach(Observer o) {
        enemies.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : this.enemies) {
            observer.update(this);
        }
    }

    public int getEnemies() {
        return enemies.size();
    }
}
