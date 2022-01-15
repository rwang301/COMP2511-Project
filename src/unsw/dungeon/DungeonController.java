package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import java.io.File;

/**
 * A JavaFX controller for the dungeon.
 * @author Robert Clifton-Everest
 *
 */
public class DungeonController {

    @FXML
    private GridPane squares;

    private List<ImageView> initialEntities;

    private boolean shift = false;

    private Player player;

    private Dungeon dungeon;

    public DungeonController(Dungeon dungeon, List<ImageView> initialEntities) {
        this.dungeon = dungeon;
        this.player = dungeon.getPlayer();
        this.initialEntities = new ArrayList<>(initialEntities);
    }

    @FXML
    public void initialize() {
        Image ground = new Image((new File("images/dirt_0_new.png")).toURI().toString());

        // Add the ground first so it is below all other entities
        // System.out.println(dungeon.getEntities());
        for (int x = 0; x < dungeon.getWidth(); x++) {
            for (int y = 0; y < dungeon.getHeight(); y++) {
                squares.add(new ImageView(ground), x, y);
            }
        }

        for (ImageView entity : initialEntities)
            squares.getChildren().add(entity);

    }

    @FXML
    public void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
        case UP:
            if (shift) {
                player.moveBoulder(Direction.UP);
                this.shift = false;
            } else {
                player.moveUp();
            }
            break;
        case DOWN:
            if (shift) {
                player.moveBoulder(Direction.DOWN);
                this.shift = false;
            } else {
                player.moveDown();
            }
            break;
        case LEFT:
            if (shift) {
                player.moveBoulder(Direction.LEFT);
                this.shift = false;
            } else {
                player.moveLeft();
            }
            break;
        case RIGHT:
            if (shift) {
                player.moveBoulder(Direction.RIGHT);
                this.shift = false;
            } else {
                player.moveRight();
            }
            break;
        case SHIFT:
            this.shift = true;
        default:
            break;
        }
    }

}
