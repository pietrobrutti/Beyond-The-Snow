package it.unicam.cs.mpgc.rpg130663.beyondthesnow.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Gestisce la vista della schermata di Vittoria, configurando l'interfaccia grafica,
 * il caricamento dello sfondo celebrativo e l'associazione dei comandi di riavvio o ritorno al menu.
 */
public class VictoryView {
    private final StackPane root;
    private final Button restartButton;
    private final Button menuButton;

    public VictoryView(long elapsedSeconds, Runnable onRestart, Runnable onMenu) {
        this.root = new StackPane();
        this.restartButton = new Button();
        this.menuButton = new Button();

        // Collega le azioni di navigazione/riavvio ai rispettivi bottoni (DIP)
        if (onRestart != null) {
            restartButton.setOnAction(e -> onRestart.run());
        }
        if (onMenu != null) {
            menuButton.setOnAction(e -> onMenu.run());
        }

        initUI();
    }

    /**
     * Inizializza i componenti grafici, carica l'immagine di sfondo e dispone il layout dei bottoni.
     */
    private void initUI() {
        ImageView backgroundView = new ImageView();
        try {
            String imagePath = getClass().getResource("/images/victory_background.png").toExternalForm();
            Image backgroundImage = new Image(imagePath);

            backgroundView.setImage(backgroundImage);
            backgroundView.setPreserveRatio(true);
            backgroundView.setSmooth(true);
            backgroundView.fitHeightProperty().bind(root.heightProperty());

        } catch (Exception e) {
            System.err.println("Errore nel caricamento dell'immagine di Vittoria: " + e.getMessage());
            root.setStyle("-fx-background-color: #0A101D;"); // Fallback di colore in caso di errore risorse
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

    /**
     * Metodo di supporto per configurare stili, dimensioni ed effetti di hover dei pulsanti.
     */
    private void configureButton(Button button, String text, double width, String baseStyle, String hoverStyle) {
        button.setText(text);
        button.setMinWidth(width);
        button.setMaxWidth(width);
        button.setStyle(baseStyle);
        button.setOnMouseEntered(e -> button.setStyle(baseStyle + hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
    }

    public StackPane getRoot() {
        return root;
    }
}