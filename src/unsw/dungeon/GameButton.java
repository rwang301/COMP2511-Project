package unsw.dungeon;

import javafx.scene.control.Button;

public class GameButton extends Button {

    public GameButton(String name, int x, int y) {
        super(name.substring(0, 1).toUpperCase() + name.substring(1));
        setMinSize(200, 50);
        setLayoutX(x);
        setLayoutY(y);
        setStyle("-fx-text-fill: darkorange; -fx-background-color: silver; -fx-font-size: 2em");
    }
    
}