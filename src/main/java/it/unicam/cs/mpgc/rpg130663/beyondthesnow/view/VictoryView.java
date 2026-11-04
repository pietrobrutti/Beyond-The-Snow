package it.unicam.cs.mpgc.rpg130663.beyondthesnow.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class VictoryView {
    private final StackPane root;
    private final Button menuButton;
    private final Button exitButton;

    public VictoryView(String heroName, long elapsedSeconds) {
        this.root = new StackPane();
        this.menuButton = new Button();
        this.exitButton = new Button();

        initUI(heroName, elapsedSeconds);
    }

    private void initUI(String heroName, long elapsedSeconds) {
        // --- 1. Sfondo con l'immagine di Vittoria ---
        ImageView backgroundView = new ImageView();
        try {
            String imagePath = getClass().getResource("/images/victory_background.png").toExternalForm();
            Image backgroundImage = new Image(imagePath);

            backgroundView.setImage(backgroundImage);
            backgroundView.setPreserveRatio(true);
            backgroundView.setSmooth(true);
            backgroundView.fitHeightProperty().bind(root.heightProperty());

        } catch (Exception e) {
            System.err.println("Errore nel caricamento dell'immagine di sfondo: " + e.getMessage());
            root.setStyle("-fx-background-color: #0D47A1;");
        }

        // --- 2. Container Principale spostato più in basso ---
        VBox contentContainer = new VBox(12);
        contentContainer.setAlignment(Pos.CENTER);

        // Abbassato a 145 per posizionarsi perfettamente nello spazio vuoto sotto la scritta
        contentContainer.setTranslateY(145);

        // Box delle statistiche con icone
        VBox statsBox = new VBox(6);
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setMaxWidth(340);
        statsBox.setStyle(
                "-fx-background-color: rgba(6, 20, 40, 0.75); " +
                        "-fx-background-radius: 10px; " +
                        "-fx-padding: 10px 15px; " +
                        "-fx-border-color: rgba(79, 195, 247, 0.4); " +
                        "-fx-border-radius: 10px; " +
                        "-fx-border-width: 1px;"
        );

        HBox heroRow = createStatRow(loadIcon("/images/hero.png"), "Eroe: " + heroName, "#E1F5FE");
        HBox runeRow = createStatRow(loadIcon("/images/rune.png"), "Rune Raccolte: 5 / 5 (100%)", "#CE93D8");

        long minutes = elapsedSeconds / 60;
        long seconds = elapsedSeconds % 60;
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        HBox timeRow = createStatRow(loadIcon("/images/timer.png"), "Tempo Impiegato: " + timeFormatted, "#80DEEA");

        statsBox.getChildren().addAll(heroRow, runeRow, timeRow);

        // --- 3. Stile e Lunghezza Fissa dei Pulsanti ---
        String baseButtonStyle =
                "-fx-background-color: rgba(2, 136, 209, 0.75); " +
                        "-fx-text-fill: #E1F5FE; " +
                        "-fx-font-family: 'Arial', sans-serif; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10px 0px; " +
                        "-fx-background-radius: 6px; " +
                        "-fx-border-color: #81D4FA; " +
                        "-fx-border-radius: 6px; " +
                        "-fx-border-width: 2px; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(79, 195, 247, 0.6), 10, 0, 0, 0);";

        String hoverButtonStyle =
                "-fx-background-color: rgba(3, 169, 244, 0.95); " +
                        "-fx-border-color: #B3E5FC;";

        // Larghezza fissa identica per entrambi i pulsanti (stesso stile del Game Over)
        double buttonWidth = 240;

        menuButton.setText("TORNA AL MENU");
        menuButton.setMinWidth(buttonWidth);
        menuButton.setMaxWidth(buttonWidth);
        menuButton.setStyle(baseButtonStyle);
        menuButton.setOnMouseEntered(e -> menuButton.setStyle(baseButtonStyle + hoverButtonStyle));
        menuButton.setOnMouseExited(e -> menuButton.setStyle(baseButtonStyle));

        exitButton.setText("ESCI DAL GIOCO");
        exitButton.setMinWidth(buttonWidth);
        exitButton.setMaxWidth(buttonWidth);
        exitButton.setStyle(baseButtonStyle);
        exitButton.setOnMouseEntered(e -> exitButton.setStyle(baseButtonStyle + hoverButtonStyle));
        exitButton.setOnMouseExited(e -> exitButton.setStyle(baseButtonStyle));

        VBox buttonsBox = new VBox(10, menuButton, exitButton);
        buttonsBox.setAlignment(Pos.CENTER);

        contentContainer.getChildren().addAll(statsBox, buttonsBox);

        // --- 4. Assemblaggio finale ---
        root.getChildren().addAll(backgroundView, contentContainer);
        StackPane.setAlignment(backgroundView, Pos.CENTER);
    }

    private HBox createStatRow(ImageView icon, String text, String textColor) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);

        Label label = new Label(text);
        label.setStyle("-fx-text-fill: " + textColor + "; -fx-font-weight: bold; -fx-font-size: 12px;");

        if (icon != null) {
            row.getChildren().add(icon);
        }
        row.getChildren().add(label);
        return row;
    }

    private ImageView loadIcon(String path) {
        try {
            var resource = getClass().getResource(path);
            if (resource != null) {
                ImageView imgView = new ImageView(new Image(resource.toExternalForm()));
                imgView.setFitWidth(18);
                imgView.setFitHeight(18);
                imgView.setPreserveRatio(true);
                imgView.setSmooth(false);
                return imgView;
            }
        } catch (Exception e) {
            // Ignora se non trovato
        }
        return null;
    }

    public StackPane getRoot() {
        return root;
    }

    public Button getMenuButton() {
        return menuButton;
    }

    public Button getExitButton() {
        return exitButton;
    }
}