package unsw.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import unsw.DungeonApplication;
import unsw.dungeon.Dungeon;
import unsw.dungeon.Key;
import unsw.dungeon.Observer;
import unsw.dungeon.Pickupable;
import unsw.dungeon.Player;
import unsw.dungeon.Subject;
import unsw.dungeon.Sword;

/**
 * A JavaFX controller for the dungeon.
 *
 * @author Robert Clifton-Everest
 *
 */
public class DungeonController implements Subject, Observer {

    @FXML
    private GridPane squares;

    @FXML
    private FlowPane backpack;

    @FXML
    private StackPane gameOver;

    @FXML
    private Button resume;

    @FXML
    private Button setting;

    @FXML
    private Label text;

    private List<ImageView> initialEntities;

    private Observer application;

    private Player player;

    private Dungeon dungeon;

    private double width;
    private double height;

    private final int backpackDimension = 150;
    private final int backpackInventory = 4;

    private boolean shift = false;
    private boolean restart;
    private List<Node> inventory;

    public DungeonController(Dungeon dungeon, List<ImageView> initialEntities, DungeonApplication application) {
        this.dungeon = dungeon;
        dungeon.attach(this);

        player = dungeon.getPlayer();
        player.attach(this);

        width = application.getWidth();
        height = application.getHeight();

        this.initialEntities = new ArrayList<>(initialEntities);
        attach(application);
    }

    public boolean isRestart() {
        return restart;
    }

   @FXML
    public void initialize() {
        // Add the ground first so it is below all other entities
        DungeonControllerLoader.loadBackground(dungeon.getWidth(), dungeon.getHeight(), squares);

        for (ImageView entity : initialEntities) {
            squares.getChildren().add(entity);
        }

        int spaceDimension = backpackDimension*2/backpackInventory;
        for (int i = 0; i < backpackInventory; i++) {
            StackPane space = new StackPane(new ImageView(new Image((new File("images/crate.png")).toURI().toString(), spaceDimension, spaceDimension, true, true)));
            space.setMinSize(spaceDimension, spaceDimension);
            space.setMaxSize(spaceDimension, spaceDimension);
            backpack.getChildren().add(space);
        }
        // TODO background image doesn't work
        inventory = backpack.getChildren();
        backpack.setPrefWrapLength(backpackDimension);
        backpack.setPrefSize(backpackDimension, backpackDimension);
        backpack.setLayoutX(width - backpack.getPrefWidth());
        backpack.setLayoutY(height - backpack.getPrefHeight());
    }

    @FXML
    public void handleReturn(ActionEvent event) {
        restart = false;
        notifyObservers();
    }

    @FXML
    public void handleResume(ActionEvent event) {
        restart = true;
        notifyObservers();
    }

    @FXML
    public void handleSetting(ActionEvent event) {
        gameOver.setVisible(true);
        blur(new GaussianBlur());
        text.setVisible(false);
        resume.setText("Restart");
        dungeon.setPause();
        // TODO requestFocus behaves a bit weird, pause potion

        setting.setOnAction(event1 -> {
            squares.requestFocus();
            dungeon.setPause();
            blur(null);
            gameOver.setVisible(false);
            setting.setOnAction(event2 -> {
                handleSetting(event2);
            });
        });
    }

    void blur(GaussianBlur blur) {
        squares.setEffect(blur);
        backpack.setEffect(blur);
    }

    @FXML
    public void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case UP:
                if (shift) {
                    player.moveBoulder("up");
                    shift = false;
                } else {
                    player.moveUp();
                }
                break;
            case DOWN:
                if (shift) {
                    player.moveBoulder("down");
                    shift = false;
                } else {
                    player.moveDown();
                }
                break;
            case LEFT:
                if (shift) {
                    player.moveBoulder("left");
                    shift = false;
                } else {
                    player.moveLeft();
                }
                break;
            case RIGHT:
                if (shift) {
                    player.moveBoulder("right");
                    shift = false;
                } else {
                    player.moveRight();
                }
                break;
            case SHIFT:
                shift = true;
            default:
                break;
        }
    }

    @Override
    public void attach(Observer observer) {
        application = observer;
    }

    @Override
    public void detach(Observer observer) {
        application = null;
    }

    @Override
    public void notifyObservers() {
        application.update(this);
    }

    @Override
    public void update(Subject subject) {
        if (subject.getClass() == Dungeon.class) {
            switch (dungeon.getEntity().getClass().getSimpleName()) {
                case "Sword":
                    addImage(DungeonControllerLoader.swordImage);
                    break;
                case "Treasure":
                    pickupTreasure(DungeonControllerLoader.treasureImage, dungeon.getPlayer().getTreasure());
                    break;
                case "Key":
                    addImage(DungeonControllerLoader.keyImage);
                    break;
                default:
                    break;
            }
        } else if (subject.getClass() == Player.class) {
            Pickupable item = ((Player) subject).getUse();
            if (item.getClass() == Key.class) {
                removeImage(DungeonControllerLoader.keyImage);
            } else if (item.getClass() == Sword.class) {
                removeImage(DungeonControllerLoader.swordImage);
            }
        }
    }

    private void removeImage(Image image) {
        Platform.runLater(() -> {
            List<Node> space;
            for (int i = 0; i < inventory.size(); i++) {
                space = ((StackPane) inventory.get(i)).getChildren();
                if (space.size() > 1){
                    ImageView item = (ImageView) space.get(1);
                    if (item.getImage() == image) {
                        space.remove(item);
                        break;
                    }
                }
            }
        });
    }

    private void addImage(Image image) {
        List<Node> space;
        for (int i = 0; i < inventory.size(); i++) {
            space = ((StackPane) inventory.get(i)).getChildren();
            if (space.size() == 1) {
                space.add(new ImageView(image));
                break;
            }
        }
    }

    private void pickupTreasure(Image image, int amount) {
        List<Node> space;
        for (int i = 0; i < inventory.size(); i++) {
            space = ((StackPane) inventory.get(i)).getChildren();
            if (space.size() > 1) {
                ImageView item = (ImageView) space.get(1);
                if (item.getImage() == image) {
                    Label num = (Label) space.get(2);
                    num.setText(Integer.toString(Integer.parseInt((num).getText()) + 1));
                    return;
                }
            }
        }
        for (int i = 0; i < inventory.size(); i++) {
            space = ((StackPane) inventory.get(i)).getChildren();
            if (space.size() == 1) {
                space.add(new ImageView(image));
                Label num = new Label(Integer.toString(amount));
                num.setId("num");
                StackPane.setAlignment(num, Pos.BOTTOM_RIGHT);
                space.add(num);
                break;
            }
        }
    }
}