package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
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
        Image ground = new Image((new File("images/dirt_0_new.png")).toURI().toString());

        // Add the ground first so it is below all other entities
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
        if (event.getCode() == KeyCode.SHIFT) {
            this.shift = true;
        }
        switch (event.getCode()) {
        case UP:
            if (shift == true) {
                player.moveBoulder("up");
                this.shift = false;
            } else {
                player.moveUp();
            }
            break;
        case DOWN:
            if (shift == true) {
                player.moveBoulder("down");
                this.shift = false;
            } else {
                player.moveDown();
            }
            break;
        case LEFT:
            if (shift == true) {
                player.moveBoulder("left");
                this.shift = false;
            } else {
                player.moveLeft();
            }
            break;
        case RIGHT:
            if (shift == true) {
                player.moveBoulder("right");
                this.shift = false;
            } else {
                player.moveRight();
            }
            break;
        default:
            break;
        }
    }

}