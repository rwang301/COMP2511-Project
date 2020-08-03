package unsw.ui;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import unsw.DungeonApplication;
import unsw.dungeon.Boulder;
import unsw.dungeon.Door;
import unsw.dungeon.Dungeon;
import unsw.dungeon.Enemy;
import unsw.dungeon.Entity;
import unsw.dungeon.Exit;
import unsw.dungeon.Fire;
import unsw.dungeon.Gnome;
import unsw.dungeon.Hound;
import unsw.dungeon.Key;
import unsw.dungeon.Medicine;
import unsw.dungeon.Portal;
import unsw.dungeon.Potion;
import unsw.dungeon.Subject;
import unsw.dungeon.Switch;
import unsw.dungeon.Sword;
import unsw.dungeon.Treasure;
import unsw.dungeon.Wall;

import java.io.File;

/**
 * A DungeonLoader that also creates the necessary ImageViews for the UI,
 * connects them via listeners to the model, and creates a controller.
 * @author Robert Clifton-Everest
 *
 */
public class DungeonControllerLoader extends DungeonLoader {

    private List<ImageView> entities = new ArrayList<>();
    private Map<Entity, ImageView> items = new HashMap<>();

    //Images
    /**
     * If the update image is Door then it will be openDoorImage
     * otherwise it will be null so that images can be removed
     */
    private Image update = null;
    static final Image ground = new Image((new File("src/images/dirt_0_new.png")).toURI().toString());
    static final Image playerImage = new Image((new File("src/images/human_new.png")).toURI().toString());
    static final Image wallImage = new Image((new File("src/images/brick_brown_0.png")).toURI().toString());
    static final Image exitImage = new Image((new File("src/images/exit.png")).toURI().toString());
    static final Image portalImage = new Image((new File("src/images/portal.png")).toURI().toString());
    static final Image keyImage = new Image((new File("src/images/key.png")).toURI().toString());
    static final Image openDoorImage = new Image((new File("src/images/open_door.png")).toURI().toString());
    static final Image closedDoorImage = new Image((new File("src/images/closed_door.png")).toURI().toString());
    static final Image switchImage = new Image((new File("src/images/pressure_plate.png")).toURI().toString());
    static final Image boulderImage = new Image((new File("src/images/boulder.png")).toURI().toString());
    static final Image treasureImage = new Image((new File("src/images/gold_pile.png")).toURI().toString());
    static final Image enemyImage = new Image((new File("src/images/deep_elf_master_archer.png")).toURI().toString());
    static final Image swordImage = new Image((new File("src/images/greatsword_1_new.png")).toURI().toString());
    static final Image potionImage = new Image((new File("src/images/bubbly.png")).toURI().toString());
    static final Image medicineImage = new Image((new File("src/images/brilliant_blue_new.png")).toURI().toString());
    static final Image houndImage = new Image((new File("src/images/hound.png")).toURI().toString());
    static final Image gnomeImage = new Image((new File("src/images/gnome.png")).toURI().toString());
    static final Image fireImage = new Image((new File("src/images/fire.gif")).toURI().toString());

    public DungeonControllerLoader(DungeonApplication application) throws FileNotFoundException {
        super(application);
    }

    public static double getWidth() {
        return ground.getWidth();
    }

    public static double getHeight() {
        return ground.getHeight();
    }

    @Override
    public void onLoad(Entity player) {
        ImageView view = new ImageView(playerImage);
        view.setViewOrder(Layer.MOVEABLE.getZIndex());
        addEntity(player, view);
    }

    @Override
    public void onLoad(Wall wall) {
        ImageView view = new ImageView(wallImage);
        view.setViewOrder(Layer.STATIC.getZIndex());
        addEntity(wall, view);
    }

    @Override
    public void onLoad(Exit exit) {
        ImageView view = new ImageView(exitImage);
        view.setViewOrder(Layer.THROUGHABLE.getZIndex());
        addEntity(exit, view);
    }

    @Override
    public void onLoad(Portal portal) {
        ImageView view = new ImageView(portalImage);
        view.setViewOrder(Layer.STATIC.getZIndex());
        addEntity(portal, view);
    }

    @Override
    public void onLoad(Key key) {
        ImageView view = new ImageView(keyImage);
        view.setViewOrder(Layer.PICKUPABLE.getZIndex());
        view.setId(key.toString());
        items.put(key, view);
        addEntity(key, view);
    }

    @Override
    public void onLoad(Door door) {
        ImageView view = new ImageView(closedDoorImage);
        view.setViewOrder(Layer.THROUGHABLE.getZIndex());
        view.setId(door.toString());
        items.put(door, view);
        addEntity(door, view);
    }

    @Override
    public void onLoad(Treasure treasure) {
        ImageView view = new ImageView(treasureImage);
        view.setViewOrder(Layer.PICKUPABLE.getZIndex());
        view.setId(treasure.toString());
        items.put(treasure, view);
        addEntity(treasure, view);
    }

    @Override
    public void onLoad(Switch floorSwitch) {
        ImageView view = new ImageView(switchImage);
        view.setViewOrder(Layer.STATIC.getZIndex());
        addEntity(floorSwitch, view);
    }

    @Override
    public void onLoad(Boulder boulder) {
        ImageView view = new ImageView(boulderImage);
        view.setViewOrder(Layer.MOVEABLE.getZIndex());
        addEntity(boulder, view);
    }

    @Override
    public void onLoad(Enemy enemy) {
        ImageView view = new ImageView(enemyImage);
        view.setViewOrder(Layer.MOVEABLE.getZIndex());
        view.setId(enemy.toString());
        items.put(enemy, view);
        addEntity(enemy, view);
    }

    @Override
    public void onLoad(Sword sword) {
        ImageView view = new ImageView(swordImage);
        view.setViewOrder(Layer.PICKUPABLE.getZIndex());
        view.setId(sword.toString());
        items.put(sword, view);
        addEntity(sword, view);
    }

    @Override
    public void onLoad(Potion potion) {
        ImageView view = new ImageView(potionImage);
        view.setViewOrder(Layer.PICKUPABLE.getZIndex());
        view.setId(potion.toString());
        items.put(potion, view);
        addEntity(potion, view);
    }

    @Override
    public void onLoad(Medicine medicine) {
        ImageView view = new ImageView(medicineImage);
        view.setId(medicine.toString());
        view.setViewOrder(Layer.PICKUPABLE.getZIndex());
        items.put(medicine, view);
        addEntity(medicine, view);
    }

    @Override
    public void onLoad(Hound hound) {
        ImageView view = new ImageView(houndImage);
        view.setViewOrder(Layer.MOVEABLE.getZIndex());
        view.setId(hound.toString());
        items.put(hound, view);
        addEntity(hound, view);
    }

    @Override
    public void onLoad(Gnome gnome) {
        ImageView view = new ImageView(gnomeImage);
        view.setViewOrder(Layer.FLYABLE.getZIndex());
        view.setId(gnome.toString());
        items.put(gnome, view);
        addEntity(gnome, view);
    }

    @Override
    public void onLoad(Fire fire) {
        ImageView view = new ImageView(fireImage);
        view.setViewOrder(Layer.MOVEABLE.getZIndex());
        view.setId(fire.toString());
        view.setFitWidth(30);
        view.setFitHeight(30);
        items.put(fire, view);
        addEntity(fire, view);
    }


    private void addEntity(Entity entity, ImageView view) {
        trackPosition(entity, view);
        entities.add(view);
    }

    private void removeEntity(Entity entity) {
        for (int i = 0; i < entities.size(); i++) {
            if (items.get(entity).getId().equals(entities.get(i).getId())) {
                entities.get(i).setImage(update);
                entities.remove(i);
                break;
            }
        }
    }

    @Override
    public void update(Subject subject) {
        Dungeon dungeon = (Dungeon) subject;
        Entity entity = dungeon.getEntity();
        dungeon.setEntity(null);
        if (dungeon.isRespawn()) {
            addEntity(entity, new ImageView(fireImage));
        } else {
            if (entity.getClass() == Door.class) update = openDoorImage;
            else update = null;
            removeEntity(entity);
        }
    }

    /**
     * Set a node in a GridPane to have its position track the position of an
     * entity in the dungeon.
     *
     * By connecting the model with the view in this way, the model requires no
     * knowledge of the view and changes to the position of entities in the
     * model will automatically be reflected in the view.
     * @param entity
     * @param node
     */
    private void trackPosition(Entity entity, Node node) {
        GridPane.setColumnIndex(node, entity.getX());
        GridPane.setRowIndex(node, entity.getY());
        entity.x().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                GridPane.setColumnIndex(node, newValue.intValue());
            }
        });
        entity.y().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                GridPane.setRowIndex(node, newValue.intValue());
            }
        });
    }

    /**
     * Load the ground as the background of the game
     * @param squares
     * @param ground
     */
    static void loadBackground(int width, int height, GridPane squares) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                ImageView view = new ImageView(ground);
                view.setViewOrder(Layer.GROUND.getZIndex());
                squares.add(view, x, y);
            }
        }
    }

    /**
     * Create a controller that can be attached to the DungeonView with all the
     * loaded entities.
     * @return
     * @throws FileNotFoundException
     */
    public DungeonController loadController(DungeonApplication application) throws FileNotFoundException {
        return new DungeonController(load(), entities, application);
    }
}