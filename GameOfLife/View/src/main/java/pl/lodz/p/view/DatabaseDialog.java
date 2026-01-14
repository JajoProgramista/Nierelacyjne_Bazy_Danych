package pl.lodz.p.view;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pl.lodz.p.GameOfLifeBoard;
import pl.lodz.p.GameOfLifeCell;
import pl.lodz.p.JdbcGameOfLifeBoardDao;
import exceptions.DataBaseException;
import java.util.Locale;
import java.util.ResourceBundle;

public class DatabaseDialog {
    private Stage dialogStage;
    private TextField urlField;
    private TextField userField;
    private PasswordField passwordField;
    private TextField boardNameField;
    private ResourceBundle bundle;
    private Locale locale;
    private boolean isSaveMode;
    private GameOfLifeBoard game;
    private GameOfLifeBoard loadedGame;


    public DatabaseDialog(Locale locale, boolean isSaveMode, GameOfLifeBoard gameBoard) {
        this.locale = locale;
        this.bundle = ResourceBundle.getBundle("bundles.messages", locale);
        this.isSaveMode = isSaveMode;
        this.game = gameBoard;
        createDialog();
    }

    private void createDialog() {
        dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle(isSaveMode ? bundle.getString("saveToDatabase") : bundle.getString("loadFromDatabase"));

        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setStyle("-fx-background-color: #DCDDC6;");

        Label titleLabel = new Label(isSaveMode ? bundle.getString("saveToDatabase") : bundle.getString("loadFromDatabase"));
        titleLabel.setFont(new Font("Times New Roman", 24));

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);

        Label urlLabel = new Label(bundle.getString("databaseUrl") + ":");
        urlField = new TextField("jdbc:postgresql://localhost:5432/nbddb");
        urlField.setPrefWidth(300);

        Label userLabel = new Label(bundle.getString("username") + ":");
        userField = new TextField("nbd");
        userField.setPrefWidth(300);

        Label passwordLabel = new Label(bundle.getString("password") + ":");
        passwordField = new PasswordField();
        passwordField.setPrefWidth(300);

        Label boardNameLabel = new Label(bundle.getString("boardName") + ":");
        boardNameField = new TextField();
        boardNameField.setPrefWidth(300);

        gridPane.add(urlLabel, 0, 0);
        gridPane.add(urlField, 1, 0);
        gridPane.add(userLabel, 0, 1);
        gridPane.add(userField, 1, 1);
        gridPane.add(passwordLabel, 0, 2);
        gridPane.add(passwordField, 1, 2);
        gridPane.add(boardNameLabel, 0, 3);
        gridPane.add(boardNameField, 1, 3);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button confirmButton = new Button(bundle.getString("confirm"));
        confirmButton.setFont(new Font(16));
        confirmButton.setPrefWidth(120);
        confirmButton.setPrefHeight(40);
        confirmButton.setStyle("-fx-background-color: #BF913B; " +
                "-fx-background-radius: 10; " +
                "-fx-border-radius: 10; " +
                "-fx-border-color: black; " +
                "-fx-border-width: 2;");
        confirmButton.setOnAction(e -> handleConfirm());

        Button cancelButton = new Button(bundle.getString("cancel"));
        cancelButton.setFont(new Font(16));
        cancelButton.setPrefWidth(120);
        cancelButton.setPrefHeight(40);
        cancelButton.setStyle("-fx-background-color: #BF913B; " +
                "-fx-background-radius: 10; " +
                "-fx-border-radius: 10; " +
                "-fx-border-color: black; " +
                "-fx-border-width: 2;");
        cancelButton.setOnAction(e -> dialogStage.close());

        buttonBox.getChildren().addAll(confirmButton, cancelButton);

        mainLayout.getChildren().addAll(titleLabel, gridPane, buttonBox);

        Scene scene = new Scene(mainLayout, 500, 350);
        dialogStage.setScene(scene);
    }

    private void handleConfirm() {
        String url = urlField.getText().trim();
        String user = userField.getText().trim();
        String password = passwordField.getText();
        String boardName = boardNameField.getText().trim();

        if (url.isEmpty() || user.isEmpty() || boardName.isEmpty()) {
            showAlert(bundle.getString("error"), bundle.getString("fillAllFields"));
            return;
        }

        try {
            JdbcGameOfLifeBoardDao dao = new JdbcGameOfLifeBoardDao(url, user, password, locale, boardName);

            if (isSaveMode) {
                dao.write(game);
                showAlert(bundle.getString("success"), bundle.getString("boardSavedSuccess"));
                dialogStage.close();
            } else {
                loadedGame = dao.read();
                showAlert(bundle.getString("success"), bundle.getString("boardLoadedSuccess"));
                dialogStage.close();
            }

            dao.close();

        } catch (DataBaseException e) {
            showAlert(bundle.getString("error"), e.getMessage());
        } catch (Exception e) {
            showAlert(bundle.getString("error"), bundle.getString("databaseConnectionError"));
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals(bundle.getString("error")) ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    public GameOfLifeCell[][] getLoadedGame() {
        return loadedGame.getBoard();
    }

    public void show() {
        dialogStage.showAndWait();
    }
}