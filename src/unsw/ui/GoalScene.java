package unsw.ui;

import java.io.IOException;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import unsw.DungeonApplication;
import unsw.dungeon.Component;
import unsw.dungeon.Dungeon;

public class GoalScene {

    private Stage stage;
    private String level;
    private double width;
    private double height;
    private Scene scene;
    private Component goal;

    public GoalScene(DungeonApplication application) throws IOException {
        stage = application.getStage();
        level = application.getLevel();
        width = application.getWidth();
        height = application.getHeight();
        Dungeon dungeon = application.getDungeon();
        goal = dungeon.getGoal();

        scene = new Scene(setGoalScene());
    }

    private BorderPane setGoalScene() {
        BorderPane borderPane = new BorderPane();
        HBox hBox = new HBox();
        VBox vBox = new VBox(new Label("Mission"), hBox);
        borderPane.setCenter(vBox);
        return borderPane;
    }

    public void start() {
        stage.setScene(scene);
        stage.show();
    }
}