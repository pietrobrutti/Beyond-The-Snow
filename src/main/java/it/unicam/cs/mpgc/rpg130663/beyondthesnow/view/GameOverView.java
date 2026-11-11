package it.unicam.cs.mpgc.rpg130663.beyondthesnow.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class GameOverView {
    private final StackPane root;
    private final Button restartButton;
    private final Button menuButton;

    public GameOverView(Runnable onRestart, Runnable onMenu) {
        this.root = new StackPane();
        this.restartButton = new Button();
        this.menuButton = new Button();

        // Colleghiamo le azioni ai bottoni
        if (onRestart != null) {
            restartButton.setOnAction(e -> onRestart.run());
        }
        if (onMenu != null) {
            menuButton.setOnAction(e -> onMenu.run());
        }

        initUI();
    }

    private void initUI() {
        ImageView backgroundView = new ImageView();
        try {
            String imagePath = getClass().getResource("/images/gameover_background.png").toExternalForm();
            Image backgroundImage = new Image(imagePath);

            backgroundView.setImage(backgroundImage);
            backgroundView.setPreserveRatio(true);
            backgroundView.setSmooth(true);
            backgroundView.fitHeightProperty().bind(root.heightProperty());

        } catch (Exception e) {
            System.err.println("Errore nel caricamento dell'immagine di Game Over: " + e.getMessage());
            root.setStyle("-fx-background-color: #1A0A0A;");
        }

        VBox buttonsContainer = new VBox(15);
        buttonsContainer.setAlignment(Pos.CENTER);
        buttonsContainer.setTranslateY(135);

        String baseButtonStyle =
                "-fx-background-color: rgba(13, 37, 63, 0.8); " +
                        "-fx-text-fill: #E1F5FE; " +
                        "-fx-font-family: 'Arial', sans-serif; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 12px 0px; " +
                        "-fx-background-radius: 6px; " +
                        "-fx-border-color: #4FC3F7; " +
                        "-fx-border-radius: 6px; " +
                        "-fx-border-width: 2px; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0, 150, 255, 0.5), 10, 0, 0, 0);";

        String hoverButtonStyle =
                "-fx-background-color: rgba(21, 101, 192, 0.95); " +
                        "-fx-border-color: #81D4FA;";

        double buttonWidth = 240;

        configureButton(restartButton, "RIPROVA", buttonWidth, baseButtonStyle, hoverButtonStyle);
        configureButton(menuButton, "MENU PRINCIPALE", buttonWidth, baseButtonStyle, hoverButtonStyle);

        buttonsContainer.getChildren().addAll(restartButton, menuButton);

        root.getChildren().addAll(backgroundView, buttonsContainer);
        StackPane.setAlignment(backgroundView, Pos.CENTER);
    }

    private void configureButton(Button button, String text, double width, String baseStyle, String hoverStyle) {
        button.setText(text);
        button.setMinWidth(width);
        button.setMaxWidth(width);
        button.setStyle(baseStyle);
        button.setOnMouseEntered(e -> button.setStyle(baseStyle + hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
    }

    public StackPane getRoot() { return root; }
}