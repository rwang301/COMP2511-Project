package unsw.ui;

import java.io.IOException;
import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import unsw.DungeonApplication;
import unsw.dungeon.Dungeon;

public class DungeonScene {
    private Scene scene;
    private Stage stage;
    private Parent root;
    private Label text;
    private HBox buttons;
    private DungeonController controller;
    private List<Node> children;
    private double width;
    private double height;
    private StackPane gameOver;
    private Node squares;
    private Node setting;

    public DungeonScene(DungeonApplication application) throws IOException {
        this.stage = application.getStage();
        width = application.getWidth();
        height = application.getHeight();

        DungeonControllerLoader dungeonLoader = new DungeonControllerLoader(application);
        controller = dungeonLoader.loadController(application);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../DungeonView.fxml"));
        loader.setController(controller);

        root = loader.load();
        scene = new Scene(root, application.getWidth(), application.getHeight());

        children = root.getChildrenUnmodifiable();
        squares = children.get(0);
        gameOver = (StackPane) children.get(1);
        setting = children.get(2);

        squares.requestFocus();
        layout();
    }

    private void layout() {
        gameOver.setPrefSize(width/4*3, height/3);
        gameOver.setLayoutX(width/2 - gameOver.getPrefWidth()/2);
        gameOver.setLayoutY(height/2 - gameOver.getPrefHeight()/2);

        text = (Label) gameOver.getChildren().get(0);
        StackPane.setAlignment(text, Pos.TOP_CENTER);

        buttons = (HBox) gameOver.getChildren().get(1);
        buttons.setAlignment(Pos.BOTTOM_CENTER);
    }

    public void gameOver(Dungeon dungeon) {
        String style = "-fx-font-size: 15em;";
        if (dungeon.isComplete()) {
            text.setText("You Won!");
            text.setStyle(style + "-fx-text-fill: goldenrod; -fx-font-family: Elephant");
            ((Button) buttons.getChildren().get(1)).setText("Continue");
        } else {
            text.setText("You Lost!");
            text.setStyle(style + "-fx-text-fill: red; -fx-font-family: Elephant");
            ((Button) buttons.getChildren().get(1)).setText("Restart");
        }
        dungeon.setPause();
        root.requestFocus();
        controller.blur(new GaussianBlur());
        setting.setDisable(true);
        gameOver.setVisible(true);
    }

    public void start() {
        stage.setScene(scene);
        stage.show();
    }
}