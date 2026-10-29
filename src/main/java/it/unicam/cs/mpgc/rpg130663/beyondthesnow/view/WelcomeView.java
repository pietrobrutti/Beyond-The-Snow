package it.unicam.cs.mpgc.rpg130663.beyondthesnow.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class WelcomeView {
    private final VBox root;
    private final TextField nameField;
    private final Button startButton;
    private final Button exitButton;

    public WelcomeView() {
        root = new VBox(20);
        root.setAlignment(Pos.CENTER);

        // Caricamento dell'immagine di sfondo WelcomePhoto.png dalle risorse
        String imagePath = getClass().getResource("/images/WelcomePhoto.png").toExternalForm();

        root.setStyle(
                "-fx-background-image: url('" + imagePath + "'); " +
                        "-fx-background-size: cover; " +
                        "-fx-background-position: center center; " +
                        "-fx-padding: 40px;"
        );

        // Titolo principale
        Label titleLabel = new Label("❄ BEYOND THE SNOW ❄");
        titleLabel.setStyle(
                "-fx-font-size: 32px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #E3F2FD; " +
                        "-fx-effect: dropshadow(three-pass-box, #1976D2, 12, 0, 0, 0);"
        );

        // Sottotitolo
        Label subtitleLabel = new Label("Niflheim: Il regno del gelo eterno");
        subtitleLabel.setStyle(
                "-fx-font-size: 15px; " +
                        "-fx-text-fill: #B0BEC5; " +
                        "-fx-font-style: italic; " +
                        "-fx-padding: 0 0 20px 0;"
        );

        // Campo di testo per inserire il nome dell'eroe
        nameField = new TextField();
        nameField.setPromptText("Inserisci il nome dell'eroe...");
        nameField.setMaxWidth(240);
        nameField.setPrefHeight(40);
        nameField.setStyle(
                "-fx-background-color: rgba(30, 39, 44, 0.85); " +
                        "-fx-text-fill: white; " +
                        "-fx-prompt-text-fill: #90A4AE; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 6px; " +
                        "-fx-border-color: #1976D2; " +
                        "-fx-border-radius: 6px; " +
                        "-fx-alignment: center;"
        );

        // Pulsante per iniziare la partita
        startButton = new Button("⚔ Inizia Nuova Avventura");
        styleButton(startButton, "rgba(25, 118, 210, 0.85)", "rgba(21, 101, 192, 0.95)");

        // Pulsante per uscire
        exitButton = new Button("🚪 Esci");
        styleButton(exitButton, "rgba(55, 71, 79, 0.85)", "rgba(38, 50, 56, 0.95)");

        root.getChildren().addAll(titleLabel, subtitleLabel, nameField, startButton, exitButton);
    }

    private void styleButton(Button btn, String baseColor, String hoverColor) {
        btn.setPrefWidth(240);
        btn.setPrefHeight(45);
        btn.setStyle(
                "-fx-background-color: " + baseColor + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 6px; " +
                        "-fx-cursor: hand;"
        );

        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: " + hoverColor + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 6px; " +
                        "-fx-cursor: hand;"
        ));

        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: " + baseColor + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-radius: 6px; " +
                        "-fx-cursor: hand;"
        ));
    }

    // Metodo per recuperare il nome inserito dall'utente
    public String getHeroName() {
        String name = nameField.getText().trim();
        return name.isEmpty() ? "Ragnar" : name; // Valore di fallback se l'utente non scrive nulla
    }

    public VBox getRoot() {
        return root;
    }

    public Button getStartButton() {
        return startButton;
    }

    public Button getExitButton() {
        return exitButton;
    }
}