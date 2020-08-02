package unsw.ui;

import java.io.File;
import java.io.IOException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
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
    private StackPane root;
    private Observer application;
    private String level;
    private final String style;
    private final double width;
    private final double height;
    private final double buttonWidth = 200;
    private final double buttonHeight;
    private final double prefDimension;
    private final double imageDimension;

    public String getLevel() {
        return level;
    }

    public MainMenuScene(DungeonApplication application) throws IOException {
        stage = application.getStage();
        attach(application);

        width = application.getWidth();
        height = application.getHeight();
        prefDimension = application.getPrefDimension();
        buttonHeight = prefDimension;
        imageDimension = width / 5;
        style = application.getButtonStyle();

        root = new StackPane();
        root.setAlignment(Pos.CENTER);

        GridPane gridPane = new GridPane();
        DungeonControllerLoader.loadBackground((int) (width / DungeonControllerLoader.getWidth()), (int) (height / DungeonControllerLoader.getHeight()), gridPane);

        root.getChildren().addAll(gridPane, createLevels());
        scene = new Scene(root, width, height);
    }

    private VBox createLevels() {
        Button start = new GameButton(buttonWidth, buttonHeight, "Start", style);
        Button help = new GameButton(buttonWidth, buttonHeight, "Help", style);
        Button exit = new GameButton(buttonWidth, buttonHeight, "Exit", style);
        BorderPane borderPane = createInstructions();

        Label title = new Label("DungeonEscape");
        title.setStyle("-fx-font-weight: bold; -fx-text-fill: orchid; -fx-font-size: 10em; -fx-font-family: serif;");
        StackPane logo = new StackPane(new ImageView((new Image((new File("src/images/sand_castle.png")).toURI().toString()))), title);

        HBox hBox = new HBox(start, help, exit);
        hBox.setAlignment(Pos.CENTER);

        VBox group = new VBox(logo, hBox);
        group.setAlignment(Pos.CENTER);
        group.setSpacing(height/20);
        hBox.setSpacing((width / 2 - buttonWidth*3) / 2);

        HBox levels = new HBox(createLevel("maze"), createLevel("boulders"), createLevel("advanced"), createLevel("master"));
        levels.setAlignment(Pos.CENTER);
        levels.setSpacing((imageDimension - buttonWidth / 2) / 5);

        Button back = ((DungeonApplication) application).backButton();
        back.setOnAction(event -> {
            root.getChildren().removeAll(back, levels, borderPane);
            root.getChildren().add(group);
        });

        start.setOnAction(event -> {
            root.getChildren().remove(group);
            root.getChildren().addAll(levels, back);
        });

        help.setOnAction(event -> {
            root.getChildren().remove(group);
            root.getChildren().addAll(borderPane, back);
        });

        exit.setOnAction(event -> {
            System.exit(0);
        });
        return group;
    }

    private BorderPane createInstructions() {
        BorderPane borderPane = new BorderPane();
        VBox vBox = new VBox(7);
        HBox hBox = new HBox();
        Label label1 = createLabel("Try to kill the enemy before it kills YOU!", new ImageView(DungeonControllerLoader.enemyImage));
        Label label2 = createLabel("Flying Enemies can move through walls.", new ImageView(DungeonControllerLoader.gnomeImage));
        Label label3 = createLabel("Sword can kill up to 5 enemies.", new ImageView(DungeonControllerLoader.swordImage));
        Label label4 = createLabel("Use the key to open doors.", new ImageView(DungeonControllerLoader.keyImage));
        Label label5 = createLabel("Drink this to increase your health.", new ImageView(DungeonControllerLoader.medicineImage));
        Label label6 = createLabel("Drink this potion and enemy runs away for 5 seconds.", new ImageView(DungeonControllerLoader.potionImage));
        Label label7 = createLabel("SHIFT + KEY to move these out of your way!", new ImageView(DungeonControllerLoader.boulderImage));
        Label label8 = createLabel("Mans best friend will sacrifice themselves for you.", new ImageView(DungeonControllerLoader.houndImage));
        Label help = new Label("HELP");

        vBox.getChildren().addAll(label1, label2, label3, label4, label5, label6, label7, label8);
        hBox.getChildren().add(help);

        vBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setAlignment(Pos.TOP_CENTER);
        vBox.setMaxSize(width/3*2, height/3*2);
        label6.setWrapText(true);
        help.setStyle("-fx-font-size: 10em; -fx-text-fill: mediumseagreen; -fx-font-family: serif; -fx-font-weight: bold");

        vBox.setStyle("-fx-border-color: chocolate; -fx-border-insets: 5; -fx-border-width: 3; -fx-background-color: blanchedalmond; -fx-border-radius: 20; -fx-background-radius: 20;");
        vBox.setPadding(new Insets(prefDimension));
        borderPane.setCenter(vBox);
        borderPane.setTop(hBox);
        BorderPane.setAlignment(vBox, Pos.TOP_CENTER);
        return borderPane;
    }

    private Label createLabel(String text, ImageView view){
        Label label = new Label(text, view);
        view.setFitWidth(prefDimension);
        view.setFitHeight(prefDimension);
        view.setPreserveRatio(true);
        label.setStyle("-fx-font-size: 2em;");
        label.setGraphicTextGap(prefDimension);
        return label;
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