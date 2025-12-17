package pl.lodz.p.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pl.lodz.p.DensityLevel;
import pl.lodz.p.PlainGameOfLifeSimulator;

import java.util.Locale;
import java.util.ResourceBundle;


public class InitialScene {
    @FXML
    private Label chooseLangLabel;

    @FXML
    private Text firstPaneLabel;

    @FXML
    private Text secondPaneLabel;

    @FXML
    private ChoiceBox<String> languageChoiceBox;

    @FXML
    private TextField heightField;

    @FXML
    private TextField widthField;

    @FXML
    private RadioButton lowButton;

    @FXML
    private RadioButton mediumButton;

    @FXML
    private RadioButton highButton;

    @FXML
    private Label authorsLabel;

    private ResourceBundle bundle;

    private Locale locale;
    @FXML
    public void initialize() {
        ToggleGroup group = new ToggleGroup();
        lowButton.setToggleGroup(group);
        mediumButton.setToggleGroup(group);
        highButton.setToggleGroup(group);
        lowButton.setSelected(true);

        authorsLabel.setWrapText(true);

        languageChoiceBox.getItems().addAll("Polski", "English");
        locale = new Locale("pl");
        languageChoiceBox.setValue("Polski");

        bundle = ResourceBundle.getBundle("bundles.messages", locale);

        updateTexts();

        languageChoiceBox.setOnAction(e -> {
            String selected = languageChoiceBox.getValue();
            if (selected.equals("Polski")) {
                locale = new Locale("pl");
            } else {
                locale = new Locale("en");
            }
            bundle = ResourceBundle.getBundle("bundles.messages", locale);
            updateTexts();
        });
    }

    private void updateTexts() {
        chooseLangLabel.setText(bundle.getString("chooseLang"));
        firstPaneLabel.setText(bundle.getString("firstPaneTitle"));
        secondPaneLabel.setText(bundle.getString("secondPaneTitle"));

        heightField.setPromptText(bundle.getString("heightField"));
        widthField.setPromptText(bundle.getString("widthField"));

        lowButton.setText(bundle.getString("lowRadioButton"));
        mediumButton.setText(bundle.getString("mediumRadioButton"));
        highButton.setText(bundle.getString("highRadioButton"));

        ResourceBundle authorsBundle = ResourceBundle.getBundle(
                "bundles.Authors", locale);
        authorsLabel.setText(authorsBundle.getString("authors"));
    }

    @FXML
    private void handleStart() {
        try {
            int height = Integer.parseInt(heightField.getText());
            int width = Integer.parseInt(widthField.getText());

            DensityLevel density = DensityLevel.LOW;
            if (mediumButton.isSelected()) {
                density = DensityLevel.MEDIUM;
            }
            else if (highButton.isSelected()) {
                density = DensityLevel.HIGH;
            }

            Stage stage = (Stage) widthField.getScene().getWindow();

            GameOfLifeScene gameScene = new GameOfLifeScene(height, width, density, new PlainGameOfLifeSimulator(),
                    locale);
            stage.setScene(gameScene.getScene());


        } catch (NumberFormatException e) {
            System.out.println("Błędne dane wejściowe!");
        }
    }
}
