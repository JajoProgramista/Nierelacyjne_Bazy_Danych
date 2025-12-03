package pl.lodz.p.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import pl.lodz.p.DensityLevel;
import pl.lodz.p.GameOfLifeBoard;
import pl.lodz.p.GameOfLifeSimulator;
import pl.lodz.p.PlainGameOfLifeSimulator;

public class Main extends Application {

    private int width;
    private int height;
    private DensityLevel densityLevel;
    private GameOfLifeBoard board;


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Game of Life");
        primaryStage.setResizable(false);
        Scene scene = primaryStage(primaryStage);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Scene primaryStage(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, 700, 700);
        root.setStyle("-fx-background-color: rgba(148,248,215,0.65);");
        Label titleLabel = new Label("Witaj w grze Game of Life!");
        titleLabel.setStyle("-fx-font-size: 30px;");
        titleLabel.setPrefSize(500, 20);
        titleLabel.setLayoutX(200);
        titleLabel.setLayoutY(20);
        root.getChildren().add(titleLabel);

        Label sizeLabel = new Label("Podaj wymiary planszy:");
        sizeLabel.setStyle("-fx-font-size: 20px;");
        TextField widthField = new TextField();
        widthField.setPromptText("Szerokość");
        TextField heightField = new TextField();
        heightField.setPromptText("Wysokość");

        VBox sizeBox = new VBox(5, sizeLabel, widthField, heightField);
        sizeBox.setPadding(new Insets(30, 200, 30, 200));
        sizeBox.setStyle("-fx-border-color: black; -fx-border-width: 1;");
        sizeBox.setLayoutX(50);
        sizeBox.setLayoutY(100);
        root.getChildren().add(sizeBox);

        ToggleGroup densityGroup = new ToggleGroup();
        VBox densityBox = new VBox(5);

        Label densityLabel = new Label("Podaj poziom zagęszczenia komórek:");
        densityLabel.setStyle("-fx-font-size: 20px;");

        densityBox.getChildren().add(densityLabel);

        densityLevel = DensityLevel.LOW;
        for (DensityLevel level : DensityLevel.values()) {
            RadioButton rb = new RadioButton(level.toString());
            rb.setToggleGroup(densityGroup);
            rb.setUserData(level);
            rb.setStyle("-fx-font-size: 15px;");
            rb.setOnAction(e -> densityLevel = (DensityLevel) rb.getUserData());

            if (level == DensityLevel.LOW) {
                rb.setSelected(true);
            }

            densityBox.getChildren().add(rb);
        }

        densityBox.setPadding(new Insets(30, 137, 30, 137));
        densityBox.setStyle("-fx-border-color: black; -fx-border-width: 1;");
        densityBox.setLayoutX(50);
        densityBox.setLayoutY(300);

        root.getChildren().add(densityBox);

        Button acceptButton = new Button("Zaakceptuj wybory!");
        acceptButton.setStyle("-fx-font-size: 20px;");
        acceptButton.setLayoutX(250);
        acceptButton.setLayoutY(530);
        acceptButton.setOnAction(e -> {
            width = Integer.parseInt(widthField.getText());
            height = Integer.parseInt(heightField.getText());
            board = new GameOfLifeBoard(height, width, densityLevel, new PlainGameOfLifeSimulator());
            System.out.println(board);
            primaryStage.setScene(gameStage());
        });
        root.getChildren().add(acceptButton);

        return scene;
    }

    private Scene gameStage() {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 700, 700);
        root.setStyle("-fx-background-color: rgba(250,234,71,0.65);");

        VBox topBox = new VBox(10);
        topBox.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Zagrajmy w Game Of Life!");
        titleLabel.setStyle("-fx-font-size: 30px;");

        Button startButton = new Button("Kolejny krok!");
        startButton.setStyle("-fx-font-size: 20px;");

        topBox.getChildren().addAll(titleLabel, startButton);
        root.setTop(topBox);

        GridPane boardPane = new GridPane();
        boardPane.setHgap(0);
        boardPane.setVgap(0);
        boardPane.setAlignment(Pos.CENTER);

        for (int i = 0; i < board.getBoard().length; i++) {
            for (int j = 0; j < board.getBoard()[0].length; j++) {
                StackPane cell = new StackPane();
                cell.setPrefSize(40, 40);
                if (board.getBoard()[i][j].getCellValue()) {
                    cell.setStyle("-fx-background-color: black;");
                } else {
                    cell.setStyle("-fx-background-color: white;");
                }
                boardPane.add(cell, j, i);
            }
        }

        root.setCenter(boardPane);

        return scene;
    }


    public static void main(String[] args) {
        launch(args);
    }
}

