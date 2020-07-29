package unsw.ui;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import unsw.DungeonApplication;
import unsw.dungeon.Dungeon;
import unsw.dungeon.Observer;
import unsw.dungeon.Player;
import unsw.dungeon.Subject;

/**
 * A JavaFX controller for the dungeon.
 *
 * @author Robert Clifton-Everest
 *
 */
public class DungeonController implements Subject {

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

    private final int backpackDimension = 100;
    private final int backpackInventory = 4;
    private final int gap = 3;

    private boolean shift = false;

    public DungeonController(Dungeon dungeon, List<ImageView> initialEntities, DungeonApplication application) {
        this.dungeon = dungeon;
        this.player = dungeon.getPlayer();

        width = application.getWidth();
        height = application.getHeight();

        this.initialEntities = new ArrayList<>(initialEntities);
        attach(application);
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
            Label space = new Label();
            space.setMinSize(spaceDimension, spaceDimension);
            space.setMaxSize(spaceDimension, spaceDimension);
            space.setStyle("-fx-background-color: black");
            backpack.getChildren().add(space);
        }
        backpack.setVgap(gap);
        backpack.setHgap(gap);
        backpack.setPadding(new Insets(0, gap, gap, 0));
        backpack.setPrefWrapLength(backpackDimension);

        backpack.setPrefSize(backpackDimension + gap*2, backpackDimension + gap*2);
        backpack.setLayoutX(width - backpack.getPrefWidth());
        backpack.setLayoutY(height - backpack.getPrefHeight());
    }

    @FXML
    public void handleReturn(ActionEvent event) {
        notifyObservers();
    }

    @FXML
    public void handleResume(ActionEvent event) {
        gameOver.setVisible(false);
        squares.setEffect(null);
        squares.requestFocus();
    }

    @FXML
    public void handleSetting(ActionEvent event) {
        gameOver.setVisible(true);
        squares.setEffect(new GaussianBlur());
        text.setVisible(false);
        resume.setText("Resume");
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

}