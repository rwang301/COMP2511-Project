/**
 *
 */
package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;

/**
 * A dungeon in the interactive dungeon player.
 *
 * A dungeon can contain many entities, each occupy a square. More than one
 * entity can occupy the same square.
 *
 * @author Robert Clifton-Everest
 *
 */
public class Dungeon implements Subject {

    private int width, height;
    private List<Entity> entities;
    private Player player;
    private Entity toUpdate = null;
    private List<Observer> observers;

    public Dungeon(int width, int height) {
        this.width = width;
        this.height = height;
        this.entities = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.player = null;
    }

    public void pickup(Pickupable item) {
        Entity update = (Entity)item;
        removeEntity(update);
        this.toUpdate = update;
        notifyObservers();
    }

    public void open(Door door) {
        this.toUpdate = door;
        notifyObservers();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public Entity getToUpdate() {
        return toUpdate;
    }

    public void setToUpdate(Entity toUpdate) {
        this.toUpdate = toUpdate;
    }

    @Override
    public void attach(Observer o) {
        observers.add(o);
    }

    @Override
    public void detach(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(o -> o.update(this));
    }
}
