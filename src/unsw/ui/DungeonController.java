package unsw.ui;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import unsw.dungeon.Dungeon;
import unsw.dungeon.Player;

/**
 * A JavaFX controller for the dungeon.
 * @author Robert Clifton-Everest
 *
 */
public class DungeonController {

    @FXML
    private GridPane squares;

    private List<ImageView> initialEntities;

    private Player player;

    private Dungeon dungeon;

    private boolean shift = false;

    public DungeonController(Dungeon dungeon, List<ImageView> initialEntities) {
        this.dungeon = dungeon;
        this.player = dungeon.getPlayer();
        this.initialEntities = new ArrayList<>(initialEntities);
    }

    @FXML
    public void initialize() {
        // Add the ground first so it is below all other entities
        DungeonControllerLoader.loadBackground(dungeon.getWidth(), dungeon.getHeight(), squares);

        for (ImageView entity : initialEntities)
            squares.getChildren().add(entity);

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

}