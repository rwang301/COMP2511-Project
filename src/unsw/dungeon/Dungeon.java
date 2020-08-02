/**
 *
 */
package unsw.dungeon;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
    private IntegerProperty switches = new SimpleIntegerProperty(0);
    private IntegerProperty treasure = new SimpleIntegerProperty(0);
    private IntegerProperty enemies = new SimpleIntegerProperty(0);
    private Component goal;
    private boolean complete = false;
    private boolean pause = true;

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
        return treasure.get();
    }

    public IntegerProperty getTreasureProperty() {
        return treasure;
    }

    public IntegerProperty getSwitches() {
        return switches;
    }

    public IntegerProperty getEnemies() {
        return enemies;
    }

    public void setGoal(Component goal) {
        this.goal = goal;
    }

    public Component getGoal() {
        return goal;
    }

    public boolean isComplete() {
        return complete;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<Entity> getEntities(Class<?> entityType) {
        return entities.stream().filter(entity -> entityType.isAssignableFrom(entity.getClass())).collect(Collectors.toList());
    }

    public void addEntity(Entity entity) {
        if (entity.getClass() == Treasure.class) treasure.set(treasure.get() + 1);
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public void initialise() {
        getEntities(Switch.class).forEach(floorSwitch -> {
            getEntities(Boulder.class).forEach(boulder -> {
                if (boulder.isOn(floorSwitch)) {
                    ((Switch) floorSwitch).setTriggered();
                    player.setTriggers(1);
                }
            });
            this.switches.set(this.switches.get() + 1);
        });

        List<Entity> enemies = getEntities(Enemy.class);
        enemies.forEach(enemy -> {
            player.attach((Observer) enemy);
            ((Enemy) enemy).initialise(player);
            this.enemies.set(this.enemies.get() + 1);
        });

        List<Entity> gnomes = getEntities(Gnome.class);
        gnomes.forEach(gnome -> {
            player.attach((Observer) gnome);
            ((Gnome) gnome).initialise(player);
        });
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
                if (entity instanceof Pickupable && entity.getClass() != Medicine.class) {
                    dungeonController.update(this);
                }
            }
            if (dungeonLoader != null) dungeonLoader.update(this);
        } else if (application != null) application.update(this);
    }
}
