package it.unicam.cs.mpgc.rpg130663.beyondthesnow.view;

import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.ScoreManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class MainMenuView {
    private final StackPane root;
    private final TextField nameInput;
    private final Button startButton;
    private final Button rulesButton;
    private final Button leaderboardButton;
    private final Button exitButton;

    public MainMenuView() {
        this.root = new StackPane();
        this.nameInput = new TextField();
        this.startButton = new Button();
        this.rulesButton = new Button();
        this.leaderboardButton = new Button();
        this.exitButton = new Button();

        initUI();
        initActions();
    }

    private void initUI() {
        // --- 1. Sfondo del Menu Principale ---
        ImageView backgroundView = new ImageView();
        try {
            String imagePath = getClass().getResource("/images/menu_background.png").toExternalForm();
            Image backgroundImage = new Image(imagePath);

            backgroundView.setImage(backgroundImage);
            backgroundView.setPreserveRatio(true);
            backgroundView.setSmooth(true);
            backgroundView.fitHeightProperty().bind(root.heightProperty());
        } catch (Exception e) {
            System.err.println("Errore caricamento sfondo menu, uso fallback: " + e.getMessage());
            try {
                String fallbackPath = getClass().getResource("/images/victory_background.png").toExternalForm();
                backgroundView.setImage(new Image(fallbackPath));
                backgroundView.setPreserveRatio(true);
                backgroundView.fitHeightProperty().bind(root.heightProperty());
            } catch (Exception ex) {
                root.setStyle("-fx-background-color: #0A192F;");
            }
        }

        // --- 2. Container Centrale (Nome + Pulsanti Principali) ---
        VBox menuBox = new VBox(12);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setTranslateY(-5);

        // Riquadro per l'inserimento del nome dell'eroe
        VBox inputContainer = new VBox(4);
        inputContainer.setAlignment(Pos.CENTER);

        Label inputLabel = new Label("NOME DELL'EROE");
        inputLabel.setStyle("-fx-text-fill: #81D4FA; -fx-font-weight: bold; -fx-font-size: 11px; -fx-font-family: 'Arial';");

        nameInput.setPromptText("Inserisci il tuo nome...");
        nameInput.setMaxWidth(240);
        nameInput.setStyle(
                "-fx-background-color: rgba(6, 20, 40, 0.85); " +
                        "-fx-text-fill: #E1F5FE; " +
                        "-fx-prompt-text-fill: #80DEEA; " +
                        "-fx-font-weight: bold; " +
                        "-fx-alignment: center; " +
                        "-fx-padding: 7px; " +
                        "-fx-background-radius: 6px; " +
                        "-fx-border-color: #81D4FA; " +
                        "-fx-border-radius: 6px; " +
                        "-fx-border-width: 1.5px;"
        );

        inputContainer.getChildren().addAll(inputLabel, nameInput);

        // Stile dei pulsanti principali
        String baseButtonStyle =
                "-fx-background-color: rgba(2, 136, 209, 0.75); " +
                        "-fx-text-fill: #E1F5FE; " +
                        "-fx-font-family: 'Arial', sans-serif; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 8px 0px; " +
                        "-fx-background-radius: 6px; " +
                        "-fx-border-color: #81D4FA; " +
                        "-fx-border-radius: 6px; " +
                        "-fx-border-width: 2px; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(79, 195, 247, 0.6), 10, 0, 0, 0);";

        String hoverButtonStyle =
                "-fx-background-color: rgba(3, 169, 244, 0.95); " +
                        "-fx-border-color: #B3E5FC;";

        double buttonWidth = 240;

        setupButton(startButton, "NUOVA PARTITA", buttonWidth, baseButtonStyle, hoverButtonStyle);
        setupButton(exitButton, "ESCI DAL GIOCO", buttonWidth, baseButtonStyle, hoverButtonStyle);

        VBox buttonsBox = new VBox(8, startButton, exitButton);
        buttonsBox.setAlignment(Pos.CENTER);

        // --- 3. Pulsanti Icona in basso (Classifica '🏆' e Istruzioni '?') ---
        String iconButtonStyle =
                "-fx-background-color: rgba(2, 136, 209, 0.8); " +
                        "-fx-text-fill: #E1F5FE; " +
                        "-fx-font-family: 'Arial', sans-serif; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 20px; " +
                        "-fx-border-color: #81D4FA; " +
                        "-fx-border-radius: 20px; " +
                        "-fx-border-width: 2px; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(79, 195, 247, 0.6), 8, 0, 0, 0);";

        rulesButton.setText("?");
        rulesButton.setMinWidth(38); rulesButton.setMinHeight(38);
        rulesButton.setMaxWidth(38); rulesButton.setMaxHeight(38);
        rulesButton.setStyle(iconButtonStyle);
        rulesButton.setOnMouseEntered(e -> rulesButton.setStyle(iconButtonStyle + "-fx-background-color: rgba(3, 169, 244, 1);"));
        rulesButton.setOnMouseExited(e -> rulesButton.setStyle(iconButtonStyle));

        leaderboardButton.setText("🏆");
        leaderboardButton.setMinWidth(38); leaderboardButton.setMinHeight(38);
        leaderboardButton.setMaxWidth(38); leaderboardButton.setMaxHeight(38);
        leaderboardButton.setStyle(iconButtonStyle);
        leaderboardButton.setOnMouseEntered(e -> leaderboardButton.setStyle(iconButtonStyle + "-fx-background-color: rgba(3, 169, 244, 1);"));
        leaderboardButton.setOnMouseExited(e -> leaderboardButton.setStyle(iconButtonStyle));

        HBox iconButtonsBox = new HBox(15, leaderboardButton, rulesButton);
        iconButtonsBox.setAlignment(Pos.CENTER);

        menuBox.getChildren().addAll(inputContainer, buttonsBox, iconButtonsBox);

        root.getChildren().addAll(backgroundView, menuBox);
        StackPane.setAlignment(backgroundView, Pos.CENTER);
    }

    private void setupButton(Button button, String text, double width, String baseStyle, String hoverStyle) {
        button.setText(text);
        button.setMinWidth(width);
        button.setMaxWidth(width);
        button.setStyle(baseStyle);
        button.setOnMouseEntered(e -> button.setStyle(baseStyle + hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(baseStyle));
    }

    private void initActions() {
        rulesButton.setOnAction(e -> showCustomDialog(
                "❄ GUIDA ALLE LANDE GHIACCIATE ❄",
                "• OBIETTIVO: Esplora la griglia e raccogli tutte le 5 rune magiche per vincere la partita.\n\n" +
                        "• SOPRAVVIVENZA: Tieni d'occhio i tuoi parametri vitali e il freddo.\n\n" +
                        "• COMANDI: Usa i tasti FRECCIA o WASD per muoverti sulla mappa."
        ));

        leaderboardButton.setOnAction(e -> {
            StringBuilder leaderboardText = new StringBuilder();
            List<ScoreManager.ScoreEntry> topScores = ScoreManager.getScores();

            if (topScores.isEmpty()) {
                leaderboardText.append("Nessun eroe ha ancora completato l'impresa.\nSii il primo a incidere il tuo nome!");
            } else {
                int rank = 1;
                for (ScoreManager.ScoreEntry entry : topScores) {
                    leaderboardText.append(rank).append(". ")
                            .append(entry.getHeroName())
                            .append(" — ")
                            .append(entry.getFormattedTime())
                            .append(" (5 Rune)\n");
                    rank++;
                }
            }

            showCustomDialog("🏆 SALA DEGLI EROI (CLASSIFICA) 🏆", leaderboardText.toString());
        });
    }

    private void showCustomDialog(String titleText, String contentText) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(5, 15, 30, 0.75);");

        VBox dialogBox = new VBox(15);
        dialogBox.setAlignment(Pos.CENTER);
        dialogBox.setMaxWidth(420);
        dialogBox.setMaxHeight(280);
        dialogBox.setPadding(new Insets(25));
        dialogBox.setStyle(
                "-fx-background-color: rgba(10, 30, 60, 0.95); " +
                        "-fx-background-radius: 12px; " +
                        "-fx-border-color: #81D4FA; " +
                        "-fx-border-radius: 12px; " +
                        "-fx-border-width: 2px; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(129, 212, 250, 0.4), 20, 0, 0, 0);"
        );

        Label titleLabel = new Label(titleText);
        titleLabel.setStyle("-fx-text-fill: #E1F5FE; -fx-font-size: 15px; -fx-font-weight: bold; -fx-font-family: 'Arial';");

        Label contentLabel = new Label(contentText);
        contentLabel.setWrapText(true);
        contentLabel.setStyle("-fx-text-fill: #B3E5FC; -fx-font-size: 13px; -fx-font-family: 'Arial'; -fx-alignment: center;");

        Button closeBtn = new Button("CHIUDI");
        closeBtn.setMinWidth(120);
        String btnStyle =
                "-fx-background-color: rgba(2, 136, 209, 0.8); " +
                        "-fx-text-fill: #E1F5FE; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 8px 0px; " +
                        "-fx-background-radius: 6px; " +
                        "-fx-border-color: #81D4FA; " +
                        "-fx-border-radius: 6px; " +
                        "-fx-border-width: 1.5px; " +
                        "-fx-cursor: hand;";
        closeBtn.setStyle(btnStyle);
        closeBtn.setOnMouseEntered(e -> closeBtn.setStyle(btnStyle + "-fx-background-color: rgba(3, 169, 244, 1);"));
        closeBtn.setOnMouseExited(e -> closeBtn.setStyle(btnStyle));

        closeBtn.setOnAction(e -> root.getChildren().remove(overlay));

        dialogBox.getChildren().addAll(titleLabel, contentLabel, closeBtn);
        overlay.getChildren().add(dialogBox);
        StackPane.setAlignment(dialogBox, Pos.CENTER);

        root.getChildren().add(overlay);
    }

    public StackPane getRoot() {
        return root;
    }

    public TextField getNameInput() {
        return nameInput;
    }

    public Button getStartButton() {
        return startButton;
    }

    public Button getRulesButton() {
        return rulesButton;
    }

    public Button getLeaderboardButton() {
        return leaderboardButton;
    }

    public Button getExitButton() {
        return exitButton;
    }
}