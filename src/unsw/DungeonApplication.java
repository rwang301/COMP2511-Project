package unsw;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import unsw.dungeon.Dungeon;
import unsw.dungeon.Observer;
import unsw.dungeon.Subject;
import unsw.ui.DungeonScene;
import unsw.ui.MainMenuScene;

public class DungeonApplication extends Application implements Observer {
    private Stage stage;
    private MainMenuScene mainMenuScene;
    private String level;

    public Stage getStage() {
        return stage;
    }

    public String getLevel() {
        return level;
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
            try {
                new DungeonScene(this).start();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (subject.getClass() == Dungeon.class) {
            Platform.runLater(() -> {
                mainMenuScene.start();
            });
        }
    }
}