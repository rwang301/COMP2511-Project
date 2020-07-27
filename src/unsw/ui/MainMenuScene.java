package unsw.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
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
    private double width;
    private double height;
    private int center;

    public String getLevel() {
        return level;
    }

    public MainMenuScene(DungeonApplication application) throws IOException {
        this.stage = application.getStage();
        attach(application);
        Pane root = new Pane();

        width = application.getWidth();
        height = application.getHeight();
        center = (int) (width/2) - 100;

        GridPane gridPane = new GridPane();
        DungeonControllerLoader.loadBackground((int) (width/DungeonControllerLoader.getWidth()), (int) (height/DungeonControllerLoader.getHeight()), gridPane);

        Button start = new GameButton("Start", center, (int) (height/4));
        Button instruction = new GameButton("Intruction", center, (int) (height/4*2));
        Button exit = new GameButton("Exit", center, (int) (height/4*3));
        Group group = new Group(start, instruction, exit);

        start.setOnAction(event -> {
            root.getChildren().remove(group);
            root.getChildren().addAll(createLevels());
        });

        instruction.setOnAction(event -> {
            root.getChildren().remove(group);
        });

        exit.setOnAction(event -> {
            System.exit(0);
        });

        root.getChildren().addAll(gridPane, group);
        scene = new Scene(root, width, height);
    }

    private List<Button> createLevels() {
        List<Button> levels = new ArrayList<>();
        levels.add(createLevel("maze", center, (int) (height/5)));
        levels.add(createLevel("boulders", center, (int) (height/5*2)));
        levels.add(createLevel("advanced", center, (int) (height/5*3)));
        levels.add(createLevel("master", center, (int) (height/5*4)));
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