package unsw.dungeon;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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

        GridPane gridPane = new GridPane();
        DungeonControllerLoader.loadBackground(50, 50, gridPane);

        Button start = new Button("Start");
        start.setOnAction(event -> {
            root.getChildren().remove(start);
            root.getChildren().addAll(createLevels());
        });
        start.setMinSize(100, 50);
        start.setLayoutX(500);
        start.setLayoutY(200);

        root.getChildren().addAll(gridPane, start);
        scene = new Scene(root);
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
        Button button = new Button(level.toUpperCase());
        button.setOnAction(event -> {
            this.level = level + ".json";
            notifyObservers();
        });
        button.setMinSize(100, 50);
        button.setLayoutX(x);
        button.setLayoutY(y);
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