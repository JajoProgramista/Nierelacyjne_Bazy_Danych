package pl.lodz.p.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pl.lodz.p.*;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.ResourceBundle;

public class GameOfLifeScene {
    private Scene scene;
    private GridPane gridPane = new GridPane();
    private Rectangle[][] cells;
    private GameOfLifeBoard game;
    private int cellSize;
    private Locale locale;

    public GameOfLifeScene(int height, int width, DensityLevel density, PlainGameOfLifeSimulator plainGameOfLifeSimulator,
                           Locale locale) {
        game = new GameOfLifeBoard(height, width, density, new PlainGameOfLifeSimulator(), locale);

        this.locale = locale;
        ResourceBundle bundle = ResourceBundle.getBundle("bundles.messages", locale);

        int maxWidth = 900;
        int maxHeight = 600;
        cellSize = Math.min(maxWidth / width, maxHeight / height);
        cellSize = Math.max(5, Math.min(cellSize, 50));

        Text title = new Text("GameOfLifeBoard");
        title.setFont(new Font("Times New Roman", 41));
        title.setFill(Color.web("#e85319"));

        initializeGridPane(height, width);

        Button nextStepButton = new Button(bundle.getString("nextStepButton"));
        nextStepButton.setFont(new Font(20));
        nextStepButton.setPrefWidth(200);
        nextStepButton.setPrefHeight(50);
        nextStepButton.setStyle("-fx-background-color: #BF913B; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: black; " +
                "-fx-border-width: 2;");
        nextStepButton.setOnAction(e -> {
            game.doSimulationStep();
            updateBoard();
        });

        Button saveButton = new Button(bundle.getString("saveBoardButton"));
        saveButton.setFont(new Font(20));
        saveButton.setPrefWidth(200);
        saveButton.setPrefHeight(50);
        saveButton.setStyle("-fx-background-color: #BF913B; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: black; " +
                "-fx-border-width: 2;");

        saveButton.setOnAction(e -> {
            FileChooser explorer = new FileChooser();
            explorer.setTitle(bundle.getString("saveBoardButton"));

            File file = explorer.showSaveDialog(scene.getWindow());

            if (file != null) {
                FileGameOfLifeDaoFactory gameSaver =
                        (FileGameOfLifeDaoFactory) GameOfLifeBoardDaoFactory.getFileDao(file.toString(), locale);

                try {
                    gameSaver.write(game);
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


        Button loadButton = new Button(bundle.getString("loadBoardButton"));
        loadButton.setFont(new Font(20));
        loadButton.setPrefWidth(200);
        loadButton.setPrefHeight(50);
        loadButton.setStyle("-fx-background-color: #BF913B; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-border-color: black; " +
                "-fx-border-width: 2;");

        loadButton.setOnAction(e -> {
            FileChooser explorer = new FileChooser();
            explorer.setTitle(bundle.getString("loadBoardButton"));

            File file = explorer.showOpenDialog(scene.getWindow());

            if (file != null) {
                FileGameOfLifeDaoFactory gameReader =
                        (FileGameOfLifeDaoFactory) GameOfLifeBoardDaoFactory.getFileDao(file.toString(), locale);

                try {
                    game.setBoard(gameReader.read().getBoard());
                    updateBoard();
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(saveButton, loadButton, nextStepButton);

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #DCDDC6;");
        root.getChildren().addAll(title, gridPane, buttonBox);



        scene = new Scene(root, 900, 770);
        updateBoard();
    }

    private void initializeGridPane(int height, int width) {
        gridPane.getChildren().clear();
        gridPane.setAlignment(Pos.CENTER);
        cells = new Rectangle[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Rectangle cell = new Rectangle(cellSize, cellSize);
                final int iPos = i;
                final int jPos = j;

                cell.setOnMouseClicked(e -> {
                    game.reverseCellValue(iPos, jPos);
                    updateBoard();
                });

                cell.setFill(Color.WHITE);
                cell.setStroke(Color.GRAY);
                cell.setStrokeWidth(0.5);
                cells[i][j] = cell;
                gridPane.add(cell, j, i);
            }
        }
    }

    private void updateBoard() {
        GameOfLifeCell[][] board = game.getBoard();

        if (cells.length != board.length || cells[0].length != board[0].length) {
            int maxWidth = 900;
            int maxHeight = 500;
            cellSize = Math.min(maxWidth / board[0].length, maxHeight / board.length);
            cellSize = Math.max(5, Math.min(cellSize, 50));

            initializeGridPane(board.length, board[0].length);
        }

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                cells[i][j].setFill(board[i][j].getCellValue() ? Color.BLACK : Color.WHITE);
            }
        }
    }

    public Scene getScene() {
        return scene;
    }
}