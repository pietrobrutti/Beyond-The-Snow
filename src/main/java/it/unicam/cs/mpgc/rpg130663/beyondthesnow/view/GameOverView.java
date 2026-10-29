package it.unicam.cs.mpgc.rpg130663.beyondthesnow.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GameOverView {
    private final VBox root;
    private final Button restartButton;
    private final Button menuButton;

    public GameOverView(String heroName) {
        root = new VBox(20);
        root.setAlignment(Pos.CENTER);

        // Caricamento dello sfondo WelcomePhoto.png con una patina scura e drammatica
        try {
            String imagePath = getClass().getResource("/images/WelcomePhoto.png").toExternalForm();
            root.setStyle(
                    "-fx-background-image: url('" + imagePath + "'); " +
                            "-fx-background-size: cover; " +
                            "-fx-background-repeat: no-repeat; " +
                            "-fx-background-position: center center; " +
                            "-fx-padding: 40px;"
            );
        } catch (Exception e) {
            root.setStyle("-fx-background-color: #0F171C; -fx-padding: 40px;");
        }

        // Titolo di Game Over in stile glaciale/fatale
        Label titleLabel = new Label("💀 SEI MORTO 💀");
        titleLabel.setStyle(
                "-fx-font-size: 36px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #FF5252; " +
                        "-fx-effect: dropshadow(three-pass-box, #B71C1C, 15, 0, 0, 0);"
        );

        // Messaggio descrittivo personalizzato
        Label messageLabel = new Label("Il gelo eterno di Niflheim ha reclamato l'eroe " + heroName + ".");
        messageLabel.setStyle(
                "-fx-font-size: 16px; " +
                        "-fx-text-fill: #CFD8DC; " +
                        "-fx-font-style: italic; " +
                        "-fx-padding: 0 0 30px 0;"
        );

        // Pulsante per riprovare
        restartButton = new Button("🔄 Riprova Avventura");
        styleButton(restartButton, "rgba(229, 57, 53, 0.85)", "rgba(198, 40, 40, 0.95)");

        // Pulsante per tornare al menu iniziale
        menuButton = new Button("🏠 Torna al Menu");
        styleButton(menuButton, "rgba(55, 71, 79, 0.85)", "rgba(38, 50, 56, 0.95)");

        root.getChildren().addAll(titleLabel, messageLabel, restartButton, menuButton);
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

    public VBox getRoot() {
        return root;
    }

    public Button getRestartButton() {
        return restartButton;
    }

    public Button getMenuButton() {
        return menuButton;
    }
}
