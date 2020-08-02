package unsw.ui;

import java.io.IOException;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import unsw.DungeonApplication;
import unsw.dungeon.Component;
import unsw.dungeon.Dungeon;
import unsw.dungeon.GoalBoulders;
import unsw.dungeon.GoalEnemies;
import unsw.dungeon.GoalExit;
import unsw.dungeon.GoalTreasure;
import unsw.dungeon.Player;

public class GoalScene {

    private Stage stage;
    private double width;
    private double height;
    private Scene scene;
    private Component goals;
    private Dungeon dungeon;
    private Button back;
    private final double prefDimension;
    private VBox goalExit;
    private VBox goalBoulders;
    private VBox goalTreasure;
    private VBox goalEnemies;

    public GoalScene(DungeonApplication application) throws IOException {
        stage = application.getStage();
        width = application.getWidth();
        height = application.getHeight();
        prefDimension = application.getPrefDimension();

        dungeon = application.getDungeon();
        goals = dungeon.getGoal();
        createGoals();

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
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(prefDimension);

        if (goals.toString().contains("GoalExit")) hBox.getChildren().add(goalExit);
        if (goals.toString().contains("GoalBoulders")) hBox.getChildren().add(goalBoulders);
        if (goals.toString().contains("GoalTreasure")) hBox.getChildren().add(goalTreasure);
        if (goals.toString().contains("GoalEnemies")) hBox.getChildren().add(goalEnemies);

        Label mission = new Label("Mission");
        mission.setId("mission");

        VBox vBox = new VBox(mission, hBox);
        vBox.setAlignment(Pos.CENTER);
        borderPane.setCenter(vBox);

        return borderPane;
    }

    private void createGoals() {
        Player player = dungeon.getPlayer();
        goalExit = createGoal("Exit", GoalExit.description, new ImageView(DungeonControllerLoader.exitImage), player.getExit().asString(), new SimpleIntegerProperty(1).asString());
        goalBoulders = createGoal("Boulders", GoalBoulders.description, new ImageView(DungeonControllerLoader.boulderImage), player.getTriggers().asString(), dungeon.getSwitches().asString());
        goalTreasure = createGoal("Treasure", GoalTreasure.description, new ImageView(DungeonControllerLoader.treasureImage), player.getTreasureProperty().asString(), dungeon.getTreasureProperty().asString());
        goalEnemies = createGoal("Enemies", GoalEnemies.description, new ImageView(DungeonControllerLoader.enemyImage), player.getKills().asString(), dungeon.getEnemies().asString());
    }

    private VBox createGoal(String title, String description, ImageView view, StringBinding progressStatsProperty, StringBinding goalStatsProperty) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("goalTitle");

        Label descriptionLabel = new Label(description);
        descriptionLabel.getStyleClass().add("goalDescription");
        descriptionLabel.setWrapText(true);

        view.setFitWidth(prefDimension);
        view.setFitHeight(prefDimension);
        view.setPreserveRatio(true);

        Label progress = new Label("Your Progress:");
        progress.getStyleClass().add("progress");

        Label progressStats = new Label();
        progressStats.textProperty().bind(progressStatsProperty);
        progressStats.getStyleClass().add("stats");

        Label slash = new Label("/");
        slash.getStyleClass().add("stats");

        Label goalStats = new Label();
        goalStats.textProperty().bind(goalStatsProperty);
        goalStats.getStyleClass().add("stats");

        HBox stats = new HBox(progressStats, slash, goalStats);
        stats.setAlignment(Pos.CENTER);

        VBox goal = new VBox(titleLabel, descriptionLabel, view, progress, stats);
        goal.getStyleClass().add("goalBox");
        goal.setMinSize(width/5, height/2);
        goal.setMaxSize(width/5, height/2);
        goal.setSpacing(height/50);
        goal.setAlignment(Pos.CENTER);
        return goal;
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