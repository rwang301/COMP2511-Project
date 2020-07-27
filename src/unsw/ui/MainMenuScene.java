package unsw.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import unsw.DungeonApplication;
import unsw.dungeon.Observer;
import unsw.dungeon.Subject;

public class MainMenuScene implements Subject {
    private Scene scene;
    private Stage stage;
    private Observer application;
    private String level;

    public String getLevel() {
        return level;
    }

    public MainMenuScene(DungeonApplication application) throws IOException {
        this.stage = application.getStage();
        attach(application);
        Pane root = new Pane();

        double width = application.getWidth();
        double height = application.getHeight();

        GridPane gridPane = new GridPane();
        DungeonControllerLoader.loadBackground((int) (width/DungeonControllerLoader.getWidth()), (int) (height/DungeonControllerLoader.getHeight()), gridPane);

        Button start = new GameButton("Start", 500, 200);
        start.setOnAction(event -> {
            root.getChildren().remove(start);
            root.getChildren().addAll(createLevels());
        });

        Button exit = new GameButton("Exit", 500, 400);
        exit.setOnAction(event -> {
            System.exit(0);
        });

        root.getChildren().addAll(gridPane, start, exit);
        scene = new Scene(root, width, height);
    }

    private List<Button> createLevels() {
        List<Button> levels = new ArrayList<>();
        levels.add(createLevel("maze", 500, 200));
        levels.add(createLevel("boulders", 500, 300));
        levels.add(createLevel("advanced", 500, 400));
        levels.add(createLevel("master", 500, 500));
        return levels;
    }

    private Button createLevel(String level, int x, int y) {
        Button button = new GameButton(level, x, y);
        button.setOnAction(event -> {
            this.level = level + ".json";
            notifyObservers();
        });
        return button;
    }

    public void start() {
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void attach(Observer observer) {
        application = observer;
    }

    @Override
    public void detach(Observer observer) {
        application = null;
    }

    @Override
    public void notifyObservers() {
        application.update(this);
    }
}