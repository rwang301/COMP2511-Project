package unsw.ui;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
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

    private List<ImageView> initialEntities;

    private Observer application;

    private Player player;

    private Dungeon dungeon;

    private boolean shift = false;
    private boolean restart = false;

    public DungeonController(Dungeon dungeon, List<ImageView> initialEntities, DungeonApplication application) {
        this.dungeon = dungeon;
        this.player = dungeon.getPlayer();
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

        for (ImageView entity : initialEntities)
            squares.getChildren().add(entity);

    }

    @FXML
    public void handleReturn(ActionEvent event) {
        notifyObservers();
    }

    @FXML
    public void handleRestart(ActionEvent event) {
        restart = true;
        notifyObservers();
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