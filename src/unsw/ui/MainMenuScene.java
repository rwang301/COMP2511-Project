package unsw.ui;

import java.io.File;
import java.io.IOException;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import unsw.DungeonApplication;
import unsw.dungeon.Observer;
import unsw.dungeon.Subject;

public class MainMenuScene implements Subject {
    private Scene scene;
    private Stage stage;
    private Observer application;
    private String level;
    private final String style = "-fx-text-fill: firebrick; -fx-background-color: ivory; -fx-font-size: 2em;";
    private final double width;
    private final double height;
    private final double buttonWidth = 200;
    private final double buttonHeight = 50;
    private final double imageDimension;

    public String getLevel() {
        return level;
    }

    public MainMenuScene(DungeonApplication application) throws IOException {
        this.stage = application.getStage();
        attach(application);

        width = application.getWidth();
        height = application.getHeight();
        imageDimension = width/5;

        StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER);

        Button start = new GameButton(buttonWidth, buttonHeight, "Start", style);
        Button help = new GameButton(buttonWidth, buttonHeight, "Help", style);
        Button exit = new GameButton(buttonWidth, buttonHeight, "Exit", style);

        VBox group = new VBox(start, help, exit);
        group.setAlignment(Pos.CENTER);
        group.setSpacing((height/2 - buttonHeight)/2);

        HBox levels = new HBox(createLevel("maze"), createLevel("boulders"), createLevel("advanced"), createLevel("master"));
        levels.setAlignment(Pos.CENTER);
        levels.setSpacing((imageDimension - buttonWidth/2)/5);

        Button back = new Button("Back", new ImageView(new Image((new File("images/back-button.png")).toURI().toString(), buttonHeight, buttonHeight, true, true)));
        StackPane.setAlignment(back, Pos.TOP_LEFT);
        back.setStyle(style);
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

        GridPane gridPane = new GridPane();
        DungeonControllerLoader.loadBackground((int) (width/DungeonControllerLoader.getWidth()), (int) (height/DungeonControllerLoader.getHeight()), gridPane);

        root.getChildren().addAll(gridPane, group);
        scene = new Scene(root, width, height);
    }

    private Button createLevel(String level) {
        Button button = new GameButton(imageDimension + buttonHeight/2, imageDimension + buttonHeight, level, new ImageView(new Image((new File("examples/" + level + ".png")).toURI().toString(), imageDimension, imageDimension, true, true)), style);
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