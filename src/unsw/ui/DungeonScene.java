package unsw.ui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import unsw.DungeonApplication;
import unsw.dungeon.Dungeon;

public class DungeonScene {
    private Scene scene;
    private Stage stage;
    private Parent root;
    private GridPane squares;
    private StackPane gameOver;
    private Label text;
    private double width;
    private double height;

    public DungeonScene(DungeonApplication application) throws IOException {
        this.stage = application.getStage();
        width = application.getWidth();
        height = application.getHeight();

        DungeonControllerLoader dungeonLoader = new DungeonControllerLoader(application);
        DungeonController controller = dungeonLoader.loadController(application);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../DungeonView.fxml"));
        loader.setController(controller);

        root = loader.load();
        scene = new Scene(root, application.getWidth(), application.getHeight());

        squares = (GridPane) root.getChildrenUnmodifiable().get(0);
        squares.requestFocus();
        center();
    }

    private void center() {
        gameOver = (StackPane) root.getChildrenUnmodifiable().get(1);
        gameOver.setPrefHeight(height/3);
        gameOver.setPrefWidth(width/4*3);
        gameOver.setLayoutX(width/2 - gameOver.getPrefWidth()/2);
        gameOver.setLayoutY(height/2 - gameOver.getPrefHeight()/2);

        text = (Label) gameOver.getChildren().get(0);
        StackPane.setAlignment(text, Pos.TOP_CENTER);
        StackPane.setAlignment(gameOver.getChildren().get(1), Pos.BOTTOM_CENTER);
    }

    public void gameOver(Dungeon dungeon) {
        if (dungeon.isComplete()) {
            text.setText("You won");
            text.setStyle("-fx-text-fill: goldenrod");
        } else {
            text.setText("You lost");
            text.setStyle("-fx-text-fill: red");
        }

        squares.setEffect(new GaussianBlur());
        root.getChildrenUnmodifiable().get(1).setVisible(true);
    }

    public void start() {
        stage.setScene(scene);
        stage.show();
    }
}