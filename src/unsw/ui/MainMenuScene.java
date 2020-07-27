package unsw.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private int centerWidth;
    private int centerHeight;
    private int levels = 4;

    public String getLevel() {
        return level;
    }

    public MainMenuScene(DungeonApplication application) throws IOException {
        this.stage = application.getStage();
        attach(application);

        Pane root = new Pane();
        root.getStylesheets().add("styles/mainMenu.css");

        width = application.getWidth();
        height = application.getHeight();
        centerWidth = (int) (width/2) - 100;
        centerHeight = (int) (height/2) - 150;

        GridPane gridPane = new GridPane();
        DungeonControllerLoader.loadBackground((int) (width/DungeonControllerLoader.getWidth()), (int) (height/DungeonControllerLoader.getHeight()), gridPane);

        Button start = new GameButton(levels, height, width, "Start", centerWidth, (int) (height/4));
        Button help = new GameButton(levels, height, width, "Help", centerWidth, (int) (height/4*2));
        Button exit = new GameButton(levels, height, width, "Exit", centerWidth, (int) (height/4*3));
        Group group = new Group(start, help, exit);
        Group levels = new Group(createLevels());

        ImageView backView = new ImageView(new Image((new File("images/back-button.png")).toURI().toString()));
        backView.setFitHeight(50);
        backView.setFitWidth(50);
        Button back = new Button("", backView);
        back.setOnAction(event -> {
            root.getChildren().removeAll(levels, back);
            root.getChildren().add(group);
        });

        start.setOnAction(event -> {
            root.getChildren().remove(group);
            root.getChildren().addAll(levels, back);
        });

        help.setOnAction(event -> {
            root.getChildren().remove(group);
            root.getChildren().addAll(back);
        });

        exit.setOnAction(event -> {
            System.exit(0);
        });

        root.getChildren().addAll(gridPane, group);
        scene = new Scene(root, width, height);
    }

    private List<Node> createLevels() {
        List<Node> levels = new ArrayList<>();
        levels.add(createLevel("maze", (int) (width/(this.levels*2)), centerHeight));
        levels.add(createLevel("boulders", (int) (width/(this.levels*2)*2), centerHeight));
        levels.add(createLevel("advanced", (int) (width/(this.levels*2)*4), centerHeight));
        levels.add(createLevel("master", (int) (width/(this.levels*2)*6), centerHeight));
        return levels;
    }

    private Button createLevel(String level, int x, int y) {
        ImageView view = new ImageView(new Image((new File("examples/" + level + ".png")).toURI().toString()));
        Button button = new GameButton(levels, height, width, level, x, y, view);
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