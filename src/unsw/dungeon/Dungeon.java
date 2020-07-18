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
    private Observer dungeonLoader;
    /**
     * A temporary copy of an entity to be updated in the UI
     */
    private Entity entity = null;
    private int treasure = 0;
    private Component goal;
    private boolean complete = false;

    public Dungeon(int width, int height) {
        this.width = width;
        this.height = height;
        this.entities = new ArrayList<>();
        this.player = null;
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

    public void addEntity(Entity entity) {
        if (entity.getClass() == Treasure.class) treasure++;
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }


    public void open(Door door) {
        entity = door;
        notifyObservers();
    }

    public void disappear(Entity entity) {
        this.entity = entity;
        removeEntity(entity);
        notifyObservers();
    }

    /**
     * Check if the dungeon is completed by the player
     * @param dead
     */
	public void complete(boolean dead) {
        // TODO implement game engine to deal with game over
        if (dead) {
            complete = false;
            System.out.println("You won: " + complete);
        } else if (goal.complete(player)) {
            complete = true;
            System.out.println("You won: " + complete);
        }
	}


    @Override
    public void attach(Observer observer) {
        dungeonLoader = observer;
    }

    @Override
    public void detach(Observer o) {
        dungeonLoader = null;
    }

    @Override
    public void notifyObservers() {
        if (dungeonLoader != null) dungeonLoader.update(this);
    }
}
