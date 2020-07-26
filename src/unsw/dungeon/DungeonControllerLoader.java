package unsw.dungeon;

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
    private static Image ground = new Image((new File("images/dirt_0_new.png")).toURI().toString());
    private Image update = null;
    private Image playerImage = new Image((new File("images/human_new.png")).toURI().toString());
    private Image wallImage = new Image((new File("images/brick_brown_0.png")).toURI().toString());
    private Image exitImage = new Image((new File("images/exit.png")).toURI().toString());
    private Image portalImage = new Image((new File("images/portal.png")).toURI().toString());
    private Image keyImage = new Image((new File("images/key.png")).toURI().toString());
    private Image openDoorImage = new Image((new File("images/open_door.png")).toURI().toString());
    private Image closedDoorImage = new Image((new File("images/closed_door.png")).toURI().toString());
    private Image switchImage = new Image((new File("images/pressure_plate.png")).toURI().toString());
    private Image boulderImage = new Image((new File("images/boulder.png")).toURI().toString());
    private Image treasureImage = new Image((new File("images/gold_pile.png")).toURI().toString());
    private Image enemyImage = new Image((new File("images/gnome.png")).toURI().toString());
    private Image swordImage = new Image((new File("images/greatsword_1_new.png")).toURI().toString());
    private Image potionImage = new Image((new File("images/bubbly.png")).toURI().toString());
    private Image medicineImage = new Image((new File("images/brilliant_blue_new.png")).toURI().toString());
    private Image houndImage = new Image((new File("images/hound.png")).toURI().toString());

    public DungeonControllerLoader(DungeonApplication application) throws FileNotFoundException {
        super(application);
    }

    @Override
    public void onLoad(Entity player) {
        ImageView view = new ImageView(playerImage);
        addEntity(player, view);
    }

    @Override
    public void onLoad(Wall wall) {
        ImageView view = new ImageView(wallImage);
        addEntity(wall, view);
    }

    @Override
    public void onLoad(Exit exit) {
        ImageView view = new ImageView(exitImage);
        addEntity(exit, view);
    }

    @Override
    public void onLoad(Portal portal) {
        ImageView view = new ImageView(portalImage);
        addEntity(portal, view);
    }

    @Override
    public void onLoad(Key key) {
        ImageView view = new ImageView(keyImage);
        view.setId(key.toString());
        items.put(key, view);
        addEntity(key, view);
    }

    @Override
    public void onLoad(Door door) {
        ImageView view = new ImageView(closedDoorImage);
        view.setId(door.toString());
        items.put(door, view);
        addEntity(door, view);
    }

    @Override
    public void onLoad(Treasure treasure) {
        ImageView view = new ImageView(treasureImage);
        view.setId(treasure.toString());
        items.put(treasure, view);
        addEntity(treasure, view);
    }

    @Override
    public void onLoad(Switch floorSwitch) {
        ImageView view = new ImageView(switchImage);
        addEntity(floorSwitch, view);
    }

    @Override
    public void onLoad(Boulder boulder) {
        ImageView view = new ImageView(boulderImage);
        addEntity(boulder, view);
    }

    @Override
    public void onLoad(Enemy enemy) {
        ImageView view = new ImageView(enemyImage);
        view.setId(enemy.toString());
        items.put(enemy, view);
        addEntity(enemy, view);
    }

    @Override
    public void onLoad(Sword sword) {
        ImageView view = new ImageView(swordImage);
        view.setId(sword.toString());
        items.put(sword, view);
        addEntity(sword, view);
    }

    @Override
    public void onLoad(Potion potion) {
        ImageView view = new ImageView(potionImage);
        view.setId(potion.toString());
        items.put(potion, view);
        addEntity(potion, view);
    }

    @Override
    public void onLoad(Medicine medicine) {
        ImageView view = new ImageView(medicineImage);
        view.setId(medicine.toString());
        items.put(medicine, view);
        addEntity(medicine, view);
    }

    @Override
    public void onLoad(Hound hound) {
        ImageView view = new ImageView(houndImage);
        view.setId(hound.toString());
        items.put(hound, view);
        addEntity(hound, view);
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
        if (entity.getClass() == Door.class) update = openDoorImage;
        else update = null;
        removeEntity(entity);
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
                squares.add(new ImageView(ground), x, y);
            }
        }
    }

    /**
     * Create a controller that can be attached to the DungeonView with all the
     * loaded entities.
     * @return
     * @throws FileNotFoundException
     */
    public DungeonController loadController() throws FileNotFoundException {
        return new DungeonController(load(), entities);
    }
}