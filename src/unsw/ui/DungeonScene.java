package unsw.ui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import unsw.DungeonApplication;

public class DungeonScene {
    private Scene scene;
    private Stage stage;
    private Parent root;
    private GridPane squares;

    public DungeonScene(DungeonApplication application) throws IOException {
        this.stage = application.getStage();

        DungeonControllerLoader dungeonLoader = new DungeonControllerLoader(application);
        DungeonController controller = dungeonLoader.loadController(application);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../DungeonView.fxml"));
        loader.setController(controller);

        root = loader.load();
        scene = new Scene(root, application.getWidth(), application.getHeight());
        squares = (GridPane) root.getChildrenUnmodifiable().get(0);
        squares.requestFocus();
    }

    public void gameOver() {
        squares.setEffect(new GaussianBlur());
        root.getChildrenUnmodifiable().get(1).setVisible(true);
    }

    public void start() {
        stage.setScene(scene);
        stage.show();
    }
}