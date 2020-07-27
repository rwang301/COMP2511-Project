package unsw.ui;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class GameButton extends Button {
    private double height;
    private double width;
    private int levels;

    public GameButton(int levels, double height, double width, String name, int x, int y, ImageView view) {
        super(name.substring(0, 1).toUpperCase() + name.substring(1), view);
        this.height = height;
        this.width = width;

        view.setFitHeight(width/(levels*2));
        view.setFitWidth(width/(levels*2));
        view.setPreserveRatio(true);
        initialise(x, y);
    }

    public GameButton(int levels, double height, double width, String name, int x, int y) {
        super(name.substring(0, 1).toUpperCase() + name.substring(1));
        this.height = height;
        this.width = width;
        this.levels = levels;

        initialise(x, y);
    }

    private void initialise(int x, int y) {
        setMinSize(200, 50);
        setMaxSize(width/(levels*2), width/(levels*2));
        setLayoutX(x);
        setLayoutY(y);
    }
}