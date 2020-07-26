package unsw.dungeon;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DungeonScene {
    private Scene scene;
    private Stage stage;

    public DungeonScene(DungeonApplication application) throws IOException {
        this.stage = application.getStage();

        DungeonControllerLoader dungeonLoader = new DungeonControllerLoader(application);
        DungeonController controller = dungeonLoader.loadController();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("DungeonView.fxml"));
        loader.setController(controller);

        Parent root = loader.load();
        scene = new Scene(root);
        root.requestFocus();
    }

    public void start() {
        stage.setScene(scene);
        stage.show();
    }
}