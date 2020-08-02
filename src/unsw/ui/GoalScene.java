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
    private Component goals;
    private Dungeon dungeon;
    private Button back;
    private final double prefDimension;
    private Label goalExit;
    private Label goalBoulders;
    private Label goalTreasure;
    private Label goalEnemies;

    public GoalScene(DungeonApplication application) throws IOException {
        stage = application.getStage();
        width = application.getWidth();
        height = application.getHeight();
        prefDimension = application.getPrefDimension();

        dungeon = application.getDungeon();
        goal = dungeon.getGoal();
        goalDescription();

        GridPane gridPane = new GridPane();
        DungeonControllerLoader.loadBackground((int) (width / DungeonControllerLoader.getWidth()), (int) (height / DungeonControllerLoader.getHeight()), gridPane);
        
        StackPane root = new StackPane();
        root.getStylesheets().add("/styles/goal.css");
        back = ((DungeonApplication) application).backButton();
        root.getChildren().addAll(gridPane, setGoalScene(), back);
        scene = new Scene(root, width, height);
    }

    private BorderPane setGoalScene() {
        BorderPane borderPane = new BorderPane();
        HBox hBox = new HBox();
        for (Component goal : goals.get) {

        }
        Label label = new Label("Mission");
        label.setId("mission");
        VBox vBox = new VBox(label, hBox);
        System.out.println(goal);
        borderPane.setCenter(vBox);
        vBox.setAlignment(Pos.CENTER);
        return borderPane;
    }

    public void goalDescription() {
        goalExit = new Label("GoalExit");
        goalBoulders = new Label("goalBoulders");
        goalEnemies = new Label("goalEnemies");
        goalTreasure= new Label("goalTreasure");
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