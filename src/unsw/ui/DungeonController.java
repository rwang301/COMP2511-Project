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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import unsw.DungeonApplication;
import unsw.dungeon.Dungeon;
import unsw.dungeon.Key;
import unsw.dungeon.Observer;
import unsw.dungeon.Pickupable;
import unsw.dungeon.Player;
import unsw.dungeon.Potion;
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
    private Pane root;

    @FXML
    private GridPane squares;

    @FXML
    private FlowPane backpack;

    @FXML
    private VBox health;

    @FXML
    private StackPane gameOver;

    @FXML
    private HBox buttons;

    @FXML
    private Button back;

    @FXML
    private Button restart;

    @FXML
    private ImageView setting;

    @FXML
    private Label text;

    private List<ImageView> initialEntities;

    private Observer application;
    private List<Observer> potions = new ArrayList<>();

    private Player player;
    private Dungeon dungeon;

    private final double width;
    private final double height;
    private final int backpackDimension = 150;
    private final int backpackInventory = 4;
    private final int prefDimension;
    private final String style = "-fx-font-size: 5em; -fx-text-fill: chartreuse;";

    private boolean shift = false;
    private boolean newGame;
    private boolean pause;
    private List<Node> inventory;

    public DungeonController(Dungeon dungeon, List<ImageView> initialEntities, DungeonApplication application) {
        this.dungeon = dungeon;
        dungeon.attach(this);

        player = dungeon.getPlayer();
        player.attach(this);

        dungeon.getEntities(Potion.class).forEach(potion -> attach((Observer) potion));
        pause = dungeon.isPause();

        width = application.getWidth();
        height = application.getHeight();
        prefDimension = application.getPrefDimension();

        this.initialEntities = new ArrayList<>(initialEntities);
        attach(application);
    }

    public String getStyle() {
        return style;
    }

    public boolean isNewGame() {
        return newGame;
    }

   @FXML
    public void initialize() {
        // TODO layering is already done like this? What about Enum z index
        // Add the ground first so it is below all other entities
        DungeonControllerLoader.loadBackground(dungeon.getWidth(), dungeon.getHeight(), squares);

        for (ImageView entity : initialEntities) {
            squares.getChildren().add(entity);
        }

        initialiseButtons();
        initialiseSetting();
        initialiseInventory();
        initialiseHealth();
    }

    private void initialiseButtons() {
        buttons.setSpacing(width / 10);
        back.getStyleClass().add("action");
        restart.getStyleClass().add("action");
    }

    private void initialiseSetting() {
        setting.setImage(new Image((new File("src/images/gear.png")).toURI().toString(), prefDimension, prefDimension, true, true));
        setting.setLayoutX(width - prefDimension);
        setting.setOnMouseClicked(event -> {
            handleSetting(event);
        });
        text.setStyle(style);
        text.setText("What would you like to do?\nClick the gear icon again to resume.");
    }

    private void initialiseInventory() {
        int spaceDimension = backpackDimension*2/backpackInventory;
        for (int i = 0; i < backpackInventory; i++) {
            StackPane space = new StackPane(new ImageView(new Image((new File("src/images/crate.png")).toURI().toString(), spaceDimension, spaceDimension, true, true)));
            space.setMinSize(spaceDimension, spaceDimension);
            space.setMaxSize(spaceDimension, spaceDimension);
            backpack.getChildren().add(space);
        }
        inventory = backpack.getChildren();
        backpack.setStyle("-fx-background-size:" + backpackDimension + " " + backpackDimension);
        backpack.setPrefWrapLength(backpackDimension);
        backpack.setPrefSize(backpackDimension, backpackDimension);
        backpack.setLayoutX(width - backpack.getPrefWidth());
        backpack.setLayoutY(height - backpack.getPrefHeight());
    }

    private void initialiseHealth() {
        ImageView life = (ImageView) health.getChildren().get(0);
        life.setImage(new Image((new File("src/images/heart.png")).toURI().toString(), prefDimension, prefDimension, true, true));
        health.setLayoutX(width - prefDimension);
        health.setLayoutY(prefDimension * 2);
    }

    @FXML
    public void handleReturn(ActionEvent event) {
        newGame = false;
        notifyObservers();
    }

    @FXML
    public void handleRestart(ActionEvent event) {
        newGame = true;
        notifyObservers();
    }

    public void handleSetting(MouseEvent event) {
        gameOver.setVisible(true);
        restart.setText("Restart");
        root.requestFocus();
        blur(new GaussianBlur());

        dungeon.setPause();
        notifyObservers();

        setting.setOnMouseClicked(event1 -> {
            gameOver.setVisible(false);
            dungeon.setPause();
            notifyObservers();
            squares.requestFocus();
            blur(null);
            setting.setOnMouseClicked(event2 -> {
                handleSetting(event2);
            });
        });
    }

    void blur(GaussianBlur blur) {
        squares.setEffect(blur);
        backpack.setEffect(blur);
        health.setEffect(blur);
    }

    public boolean isPause() {
        return dungeon.isPause();
    }

    public Player getPlayer() {
        return dungeon.getPlayer();
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
        if (observer.getClass() == DungeonApplication.class) application = observer;
        else if (observer.getClass() == Potion.class) potions.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        if (observer.getClass() == DungeonApplication.class) application = null;
        else if (observer.getClass() == Potion.class) potions.remove(observer);
    }

    @Override
    public void notifyObservers() {
        if (pause != dungeon.isPause()) {
            potions.forEach(potion -> potion.update(this));
            pause = dungeon.isPause();
        } else application.update(this);
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
            Player player = (Player) subject;
            List<Node> lives = health.getChildren();
            if (player.getCurrHealth() < player.getPrevHealth()) {
                Platform.runLater(() -> {
                    lives.remove(lives.get(lives.size() - 1));
                });
            } else if (player.getCurrHealth() > player.getPrevHealth()) {
                Platform.runLater(() -> {
                    ImageView life = new ImageView(new Image((new File("src/images/heart.png")).toURI().toString(), prefDimension, prefDimension, true, true));
                    lives.add(life);
                });
            } else {
                Pickupable item = player.getUse();
                if (item.getClass() == Key.class) {
                    removeImage(DungeonControllerLoader.keyImage);
                } else if (item.getClass() == Sword.class) {
                    removeImage(DungeonControllerLoader.swordImage);
                }
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