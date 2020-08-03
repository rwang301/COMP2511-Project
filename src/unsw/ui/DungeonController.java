package unsw.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.layout.StackPane;
import unsw.DungeonApplication;
import unsw.dungeon.Dungeon;
import unsw.dungeon.Entity;
import unsw.dungeon.Key;
import unsw.dungeon.Observer;
import unsw.dungeon.Pickupable;
import unsw.dungeon.Player;
import unsw.dungeon.Potion;
import unsw.dungeon.Subject;
import unsw.dungeon.Sword;
import unsw.dungeon.Treasure;

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
    private BorderPane goals;

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
    private ImageView mission;

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

    private boolean shift = false;
    private boolean newGame;
    private boolean pause = false;
    private List<Node> inventory;
    private boolean goal = false;
    private boolean goalScene = false;

    public DungeonController(Dungeon dungeon, List<ImageView> initialEntities, DungeonApplication application) {
        this.dungeon = dungeon;
        this.initialEntities = new ArrayList<>(initialEntities);

        player = dungeon.getPlayer();
        player.attach(this);

        dungeon.attach(this);
        dungeon.getEntities(Potion.class).forEach(potion -> attach((Observer) potion));
        attach(application);

        width = application.getWidth();
        height = application.getHeight();
        prefDimension = application.getPrefDimension();
    }

    public boolean isNewGame() {
        return newGame;
    }

    public boolean isPause() {
        return dungeon.isPause();
    }

    public boolean isGoal() {
        return goal;
    }

    public void setGoal(boolean goal) {
        this.goal = goal;
    }

    public boolean isGoalScene() {
        return goalScene;
    }

    public void setGoalScene(boolean goalScene) {
        this.goalScene = goalScene;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public Player getPlayer() {
        return dungeon.getPlayer();
    }

   @FXML
    public void initialize() {
        // Add the ground first so it is below all other entities
        DungeonControllerLoader.loadBackground(dungeon.getWidth(), dungeon.getHeight(), squares);

        for (ImageView entity : initialEntities) {
            squares.getChildren().add(entity);
        }

        initialiseGoals();
        initialiseButtons();
        initialiseSetting();
        initialiseInventory();
        initialiseHealth();
        initialiseMission();
    }

    private void initialiseGoals() {
        setting.setDisable(true);
        mission.setDisable(true);

        Label title = new Label("Your Mission ...");
        title.setId("title");
        title.setFont(Font.loadFont("file:src/fonts/Ghostz-77qw.ttf", 75));

        HBox titleContainer = new HBox(title);
        titleContainer.setAlignment(Pos.CENTER);

        Label description = new Label(dungeon.getGoal().getDescription(1));
        description.setId("description");

        Button start = new Button("Start");
        start.getStyleClass().add("action");
        start.setOnAction(event -> {
            goals.setVisible(false);
            setting.setDisable(false);
            mission.setDisable(false);
            squares.requestFocus();
            dungeon.setPause();
        });

        HBox buttonContainer = new HBox(start);
        buttonContainer.setAlignment(Pos.CENTER);

        goals.setMinSize(width / 2, height / 2);
        goals.setMaxSize(width / 2, height / 1.5);
        goals.setLayoutX(width / 4);
        goals.setLayoutY(height / 4);

        goals.setCenter(description);
        goals.setTop(titleContainer);
        goals.setBottom(buttonContainer);
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
        text.setText("Setting");
        text.setStyle("-fx-text-fill: cornflowerblue");
        text.setFont(Font.loadFont("file:src/fonts/Ghostz-77qw.ttf", 120));
    }

    private void initialiseMission() {
        mission.setImage(new Image((new File("src/images/success.png")).toURI().toString(), prefDimension, prefDimension, true, true));
        mission.setLayoutX(width - prefDimension);
        mission.setLayoutY(prefDimension * 2);
        mission.setOnMouseClicked(event -> {
            dungeon.setPause();
            goal = true;
            notifyObservers();
        });
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
        backpack.setLayoutX(width - backpackDimension);
        backpack.setLayoutY(height - backpackDimension);
    }

    private void initialiseHealth() {
        ImageView life = (ImageView) health.getChildren().get(0);
        life.setImage(new Image((new File("src/images/heart.png")).toURI().toString(), prefDimension, prefDimension, true, true));
        health.setLayoutX(width - prefDimension);
        health.setLayoutY(prefDimension * 4);
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
        mission.setDisable(true);
        root.requestFocus();
        blur(new GaussianBlur());
        restart.setText("Restart");

        dungeon.setPause();
        pause = true;
        notifyObservers();

        // TODO allow mission button in setting, clean up the booleans and logic
        setting.setOnMouseClicked(event1 -> {
            gameOver.setVisible(false);
            mission.setDisable(false);
            squares.requestFocus();
            blur(null);

            dungeon.setPause();
            pause = true;
            notifyObservers();

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
        if (pause) {
            potions.forEach(potion -> potion.update(this));
            pause = false;
        } else {
            if (goal) potions.forEach(potion -> potion.update(this));
            application.update(this);
        }
    }

    @Override
    public void update(Subject subject) {
        if (subject.getClass() == Dungeon.class) { // Pick up items
            Entity entity = dungeon.getEntity();
            if (entity.getClass() == Key.class) addImage(DungeonControllerLoader.keyImage, new SimpleIntegerProperty(1).asString());
            else if (entity.getClass() == Sword.class) addImage(DungeonControllerLoader.swordImage, player.getHits().asString());
            else if (entity.getClass() == Treasure.class) addImage(DungeonControllerLoader.treasureImage, player.getTreasureProperty().asString());
            else if (entity.getClass() == Potion.class) addImage(DungeonControllerLoader.potionImage, player.getTick().asString());
        } else if (subject.getClass() == Player.class) {
            Player player = (Player) subject;
            List<Node> lives = health.getChildren();
            Pickupable item = player.getUse();
            if (item != null) { // Use up items
                if (item.getClass() == Key.class) removeImage(DungeonControllerLoader.keyImage);
                else if (item.getClass() == Sword.class) removeImage(DungeonControllerLoader.swordImage);
                else if (item.getClass() == Potion.class) removeImage(DungeonControllerLoader.potionImage);
                player.setUse(null);
            } else { // Add health
                int currHealth = player.getCurrHealth();
                int prevHealth = player.getPrevHealth();
                if (currHealth < prevHealth) Platform.runLater(() -> lives.remove(lives.get(lives.size() - 1))); // TODO sometimes get -1 index
                else if (currHealth > prevHealth) Platform.runLater(() -> lives.add(new ImageView(new Image((new File("src/images/heart.png")).toURI().toString(), prefDimension, prefDimension, true, true))));
                player.setPrevHealth(currHealth);
            }
        }
    }

    private void addImage(Image image, StringBinding string) {
        List<Node> space;
        for (int i = 0; i < inventory.size(); i++) {
            space = ((StackPane) inventory.get(i)).getChildren();
            if (space.size() > 1) { // the space has an item in it
                ImageView item = (ImageView) space.get(1);
                Label num = (Label) space.get(2);
                if (item.getImage() == image) {
                    String[] path = image.getUrl().split("/");
                    if (path[path.length - 1].equals("bubbly.png")) num.textProperty().bind(string);
                    return;
                }
            }
        }

        // Did not found the image
        for (int i = 0; i < inventory.size(); i++) {
            space = ((StackPane) inventory.get(i)).getChildren();
            if (space.size() == 1) { // the space has no entity in it, 1 is the background image of the grid
                space.add(new ImageView(image));
                space.add(numItems(string));
                break;
            }
        }
    }

    private Label numItems(StringBinding string) {
        Label num = new Label();
        num.setId("num");
        num.textProperty().bind(string);
        StackPane.setAlignment(num, Pos.BOTTOM_RIGHT);
        return num;
    }

    private void removeImage(Image image) {
        Platform.runLater(() -> {
            List<Node> space;
            for (int i = 0; i < inventory.size(); i++) {
                space = ((StackPane) inventory.get(i)).getChildren();
                if (space.size() > 1) {
                    ImageView item = (ImageView) space.get(1);
                    Label num = (Label) space.get(2);
                    if (item.getImage() == image) {
                        space.remove(item);
                        space.remove(num);
                        break;
                    }
                }
            }
        });
    }
}