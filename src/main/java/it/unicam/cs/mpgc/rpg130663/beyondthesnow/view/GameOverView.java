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

    public GameOverView(String heroName) {
        this.root = new StackPane();
        this.restartButton = new Button();
        this.menuButton = new Button();

        initUI(heroName);
    }

    private void initUI(String heroName) {
        // --- 1. Sfondo con l'immagine di Game Over ---
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

        // --- 2. Container per i Pulsanti al centro del riquadro ---
        VBox buttonsContainer = new VBox(15);
        buttonsContainer.setAlignment(Pos.CENTER);

        // Posizionamento verticale nel riquadro
        buttonsContainer.setTranslateY(135);

        // --- 3. Stile e Lunghezza fissa dei Pulsanti ---
        String baseButtonStyle =
                "-fx-background-color: rgba(13, 37, 63, 0.8); " +
                        "-fx-text-fill: #E1F5FE; " +
                        "-fx-font-family: 'Arial', sans-serif; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 12px 0px; " + // Padding orizzontale gestito dalla larghezza fissa
                        "-fx-background-radius: 6px; " +
                        "-fx-border-color: #4FC3F7; " +
                        "-fx-border-radius: 6px; " +
                        "-fx-border-width: 2px; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0, 150, 255, 0.5), 10, 0, 0, 0);";

        String hoverButtonStyle =
                "-fx-background-color: rgba(21, 101, 192, 0.95); " +
                        "-fx-border-color: #81D4FA;";

        // Impostiamo una larghezza fissa identica per entrambi i pulsanti
        double buttonWidth = 240;

        restartButton.setText("RIPROVA");
        restartButton.setMinWidth(buttonWidth);
        restartButton.setMaxWidth(buttonWidth);
        restartButton.setStyle(baseButtonStyle);
        restartButton.setOnMouseEntered(e -> restartButton.setStyle(baseButtonStyle + hoverButtonStyle));
        restartButton.setOnMouseExited(e -> restartButton.setStyle(baseButtonStyle));

        menuButton.setText("MENU PRINCIPALE");
        menuButton.setMinWidth(buttonWidth);
        menuButton.setMaxWidth(buttonWidth);
        menuButton.setStyle(baseButtonStyle);
        menuButton.setOnMouseEntered(e -> menuButton.setStyle(baseButtonStyle + hoverButtonStyle));
        menuButton.setOnMouseExited(e -> menuButton.setStyle(baseButtonStyle));

        buttonsContainer.getChildren().addAll(restartButton, menuButton);

        // --- 4. Assemblaggio finale ---
        root.getChildren().addAll(backgroundView, buttonsContainer);
        StackPane.setAlignment(backgroundView, Pos.CENTER);
    }

    public StackPane getRoot() {
        return root;
    }

    public Button getRestartButton() {
        return restartButton;
    }

    public Button getMenuButton() {
        return menuButton;
    }
}