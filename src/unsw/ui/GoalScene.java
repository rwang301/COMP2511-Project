package unsw.ui;

import java.io.IOException;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import unsw.DungeonApplication;
import unsw.dungeon.Component;
import unsw.dungeon.Dungeon;

public class GoalScene {

    private Stage stage;
    private double width;
    private double height;
    private Scene scene;
    private Component goal;
    private Dungeon dungeon;
    private Button back;
    private final double prefDimension;

    public GoalScene(DungeonApplication application) throws IOException {
        stage = application.getStage();
        width = application.getWidth();
        height = application.getHeight();
        prefDimension = application.getPrefDimension();

        dungeon = application.getDungeon();
        goal = dungeon.getGoal();

        GridPane gridPane = new GridPane();
        DungeonControllerLoader.loadBackground((int) (width / DungeonControllerLoader.getWidth()), (int) (height / DungeonControllerLoader.getHeight()), gridPane);

        StackPane root = new StackPane();
        back = ((DungeonApplication) application).backButton();
        root.getChildren().addAll(gridPane, setGoalScene(), back);
        scene = new Scene(root, width, height);
    }

    private BorderPane setGoalScene() {
        BorderPane borderPane = new BorderPane();
        HBox hBox = new HBox();
        VBox vBox = new VBox(new Label("Mission"), hBox);
        borderPane.setCenter(vBox);
        vBox.setAlignment(Pos.CENTER);
        return borderPane;
    }

    public void start(DungeonController controller) {
        back.setOnAction(event -> {
            dungeon.setPause();
            controller.notifyObservers();
        });
        stage.setScene(scene);
        stage.show();
    }
}