package unsw.ui;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class GameButton extends Button {

    public GameButton(double buttonWidth, double buttonHeight, String name, ImageView view, String style) {
        super(name.substring(0, 1).toUpperCase() + name.substring(1), view);
        setMaxSize(buttonWidth, buttonHeight);
        initialise(buttonWidth, buttonHeight, style);
    }

    public GameButton(double buttonWidth, double buttonHeight, String name, String style) {
        super(name.substring(0, 1).toUpperCase() + name.substring(1));
        initialise(buttonWidth, buttonHeight, style);
    }

    private void initialise(double minWidth, double minHeight, String style) {
        setMinSize(minWidth, minHeight);
        setStyle(style + "-fx-content-display: top;");
    }
}