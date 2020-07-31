/**
 *
 */
package unsw.dungeon;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import unsw.DungeonApplication;
import unsw.ui.DungeonController;
import unsw.ui.DungeonControllerLoader;

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
    private List<Entity> entities = new CopyOnWriteArrayList<>();
    private Player player = null;
    private Observer dungeonLoader;
    private Observer application;
    private Observer dungeonController;
    /**
     * A temporary copy of an entity to be updated in the UI
     */
    private Entity entity = null;
    private int treasure = 0;
    private Component goal;
    private boolean complete = false;
    private boolean pause = false;

    public Dungeon(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause() {
        pause = !pause;
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

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public int getTreasure() {
        return treasure;
    }

    public void setGoal(Component goal) {
        this.goal = goal;
    }

    public boolean isComplete() {
        return complete;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<Entity> getEntities(Class<?> entityType) {
        return player.getEntities(entityType);
    }

    public void addEntity(Entity entity) {
        if (entity.getClass() == Treasure.class) treasure++;
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    void open(Door door) {
        entity = door;
        notifyObservers();
    }

    void disappear(Entity entity) {
        this.entity = entity;
        removeEntity(entity);
        notifyObservers();
    }

    /**
     * Check if the dungeon is completed by the player
     * @param dead
     */
	void complete(boolean dead) {
        if (dead) complete = false;
        else if (goal.complete(player)) complete = true;
        else return;
        notifyObservers();
	}


    @Override
    public void attach(Observer observer) {
        if (observer.getClass() == DungeonApplication.class) application = observer;
        else if (observer.getClass() == DungeonControllerLoader.class) dungeonLoader = observer;
        else if (observer.getClass() == DungeonController.class) dungeonController = observer;
    }

    @Override
    public void detach(Observer observer) {
        if (observer.getClass() == DungeonApplication.class) application = null;
        else if (observer.getClass() == DungeonControllerLoader.class) dungeonLoader = null;
        else if (observer.getClass() == DungeonController.class) dungeonController = null;
    }

    @Override
    public void notifyObservers() {
        if (entity != null) {
            if (dungeonController != null) {
                if (entity instanceof Pickupable && entity.getClass() != Potion.class && entity.getClass() != Medicine.class) {
                    dungeonController.update(this);
                }
            }
            if (dungeonLoader != null) dungeonLoader.update(this);
        } else if (application != null) application.update(this);
    }
}
