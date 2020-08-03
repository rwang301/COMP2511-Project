package unsw;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import unsw.dungeon.Dungeon;
import unsw.dungeon.Observer;
import unsw.dungeon.Subject;
import unsw.ui.DungeonController;
import unsw.ui.DungeonScene;
import unsw.ui.GoalScene;
import unsw.ui.MainMenuScene;

public class DungeonApplication extends Application implements Observer {
    private Stage stage;
    private MainMenuScene mainMenuScene;
    private DungeonScene dungeonScene;
    private GoalScene goalScene;
    private String level;
    private double width;
    private double height;
    private Dungeon dungeon;
    private final int prefDimension = 50;
    private final String buttonStyle = "-fx-text-fill: firebrick; -fx-background-color: ivory; -fx-font-size: 2em;";

    public Stage getStage() {
        return stage;
    }

    public String getLevel() {
        return level;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public int getPrefDimension() {
        return prefDimension;
    }

    public String getButtonStyle() {
        return buttonStyle;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public Button backButton() {
        Button back = new Button("Back", new ImageView(new Image((new File("src/images/back-button.png")).toURI().toString(), prefDimension, prefDimension, true, true)));
        StackPane.setAlignment(back, Pos.TOP_LEFT);
        back.setStyle(buttonStyle);
        return back;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.stage = primaryStage;
        primaryStage.setTitle("Dungeon");
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        width = Screen.getScreens().get(0).getBounds().getWidth();
        height = Screen.getScreens().get(0).getBounds().getHeight();
        this.mainMenuScene = new MainMenuScene(this);
        mainMenuScene.start();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void update(Subject subject) {
        if (subject.getClass() == MainMenuScene.class) {
            this.level = ((MainMenuScene) subject).getLevel();
            start();
        } else if (subject.getClass() == Dungeon.class) {
            Platform.runLater(() -> {
                dungeonScene.gameOver((Dungeon) subject);
            });
            return;
        } else if (subject.getClass() == DungeonController.class) {
            DungeonController controller = (DungeonController) subject;
            if (controller.isNewGame()) {
                start();
            } else if (controller.isGoalScene()) {
                dungeonScene.start();
                controller.setGoalScene(false);
                controller.setGoal(false);
            } else if (controller.isGoal()) {
                goalScene.start(controller);
                controller.setGoalScene(true);
            } else {
                mainMenuScene.start();
            }
        } else if (subject.getClass() == DungeonScene.class) {
            level = ((DungeonScene) subject).getLevel();
            start();
        }
        stage.setFullScreen(true);
    }

    private void start() {
        try {
            dungeonScene = new DungeonScene(this);
            dungeonScene.start();

            dungeon = dungeonScene.getDungeon();
            goalScene = new GoalScene(this);
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
    }
}