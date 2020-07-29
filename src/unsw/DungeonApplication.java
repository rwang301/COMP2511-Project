package unsw;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Screen;
import javafx.stage.Stage;
import unsw.dungeon.Dungeon;
import unsw.dungeon.Observer;
import unsw.dungeon.Subject;
import unsw.ui.DungeonController;
import unsw.ui.DungeonScene;
import unsw.ui.MainMenuScene;

public class DungeonApplication extends Application implements Observer {
    private Stage stage;
    private MainMenuScene mainMenuScene;
    private DungeonScene dungeonScene;
    private String level;
    private double width = Screen.getScreens().get(0).getVisualBounds().getWidth();
    private double height = Screen.getScreens().get(0).getVisualBounds().getHeight();

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

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.stage = primaryStage;
        primaryStage.setTitle("Dungeon");

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
        } else if (subject.getClass() == DungeonController.class) {
            if (((DungeonController) subject).isRestart()) {
                start();
            } else {
                mainMenuScene.start();
            }
        }
    }

    private void start() {
        try {
            dungeonScene = new DungeonScene(this);
            dungeonScene.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}