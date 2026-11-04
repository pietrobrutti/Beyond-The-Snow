package it.unicam.cs.mpgc.rpg130663.beyondthesnow.view;

import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.ScoreManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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

    // Etichetta di errore con spazio sempre riservato (mai più shift)
    private final Label errorLabel;

    public MainMenuView() {
        this.root = new StackPane();
        this.nameInput = new TextField();
        this.startButton = new Button();
        this.rulesButton = new Button();
        this.leaderboardButton = new Button();
        this.exitButton = new Button();
        this.errorLabel = new Label();

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

        // --- 2. Container Centrale (Menu Principale) ---
        VBox menuBox = new VBox(10);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setMaxWidth(400);
        menuBox.setTranslateY(-5);

        // Etichetta del nome
        Label inputLabel = new Label("NOME DELL'EROE");
        inputLabel.setStyle("-fx-text-fill: #81D4FA; -fx-font-weight: bold; -fx-font-size: 11px; -fx-font-family: 'Arial';");

        // Configurazione dell'errore: sempre gestito nel layout ma invisibile all'avvio
        errorLabel.setText("");
        errorLabel.setOpacity(0.0); // Invisibile ma occupa lo spazio esatto, impedendo qualsiasi shift
        errorLabel.setMinWidth(280);
        errorLabel.setMaxWidth(280);
        errorLabel.setAlignment(Pos.CENTER);
        errorLabel.setWrapText(true);
        errorLabel.setStyle(
                "-fx-background-color: rgba(211, 47, 47, 0.9); " +
                        "-fx-text-fill: #FFFFFF; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 11px; " +
                        "-fx-padding: 6px 10px; " +
                        "-fx-background-radius: 6px; " +
                        "-fx-border-color: #FF5252; " +
                        "-fx-border-radius: 6px; " +
                        "-fx-border-width: 1px;"
        );

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

        // Contenitore centrale del blocco nome ed errore
        VBox inputContainer = new VBox(5, inputLabel, errorLabel, nameInput);
        inputContainer.setAlignment(Pos.CENTER);
        inputContainer.setMaxWidth(300);

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

        // --- 3. Pulsanti Icona in basso ---
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

    public void showNameError(String message) {
        errorLabel.setText(message);
        errorLabel.setOpacity(1.0); // Rende visibile il box rosso senza alterare il layout
    }

    public void hideNameError() {
        errorLabel.setText("");
        errorLabel.setOpacity(0.0); // Rende trasparente il box liberando visivamente lo spazio
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

        leaderboardButton.setOnAction(e -> showArchiveDialog());

        nameInput.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.trim().isEmpty()) {
                hideNameError();
            }
        });
    }

    private void showCustomDialog(String titleText, String contentText) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(5, 15, 30, 0.75);");

        VBox dialogBox = new VBox(15);
        dialogBox.setAlignment(Pos.CENTER);
        dialogBox.setMaxWidth(460);
        dialogBox.setMaxHeight(300);
        dialogBox.setPadding(new Insets(20));
        dialogBox.setStyle(
                "-fx-background-color: rgba(10, 30, 60, 0.95); " +
                        "-fx-background-radius: 12px; " +
                        "-fx-border-color: #81D4FA; " +
                        "-fx-border-radius: 12px; " +
                        "-fx-border-width: 2px; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(129, 212, 250, 0.4), 20, 0, 0, 0);"
        );

        Label titleLabel = new Label(titleText);
        titleLabel.setStyle("-fx-text-fill: #E1F5FE; -fx-font-size: 14px; -fx-font-weight: bold; -fx-font-family: 'Arial';");

        Label contentLabel = new Label(contentText);
        contentLabel.setWrapText(true);
        contentLabel.setStyle("-fx-text-fill: #B3E5FC; -fx-font-size: 12px; -fx-font-family: 'Arial'; -fx-alignment: center;");

        Button closeBtn = createStyledButton("CHIUDI", 120);
        closeBtn.setOnAction(ev -> root.getChildren().remove(overlay));

        dialogBox.getChildren().addAll(titleLabel, contentLabel, closeBtn);
        overlay.getChildren().add(dialogBox);
        StackPane.setAlignment(dialogBox, Pos.CENTER);

        root.getChildren().add(overlay);
    }

    // Finestra Archivio di Niflheim (Riorganizzata a schede a blocco pulite e centrate)
    private void showArchiveDialog() {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(5, 15, 30, 0.85);");

        VBox dialogBox = new VBox(15);
        dialogBox.setAlignment(Pos.CENTER);
        dialogBox.setMaxWidth(820);
        dialogBox.setMaxHeight(540);
        dialogBox.setPadding(new Insets(20));
        dialogBox.setStyle(
                "-fx-background-color: rgba(10, 30, 60, 0.97); " +
                        "-fx-background-radius: 16px; " +
                        "-fx-border-color: #81D4FA; " +
                        "-fx-border-radius: 16px; " +
                        "-fx-border-width: 2px; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(129, 212, 250, 0.5), 25, 0, 0, 0);"
        );

        Label titleLabel = new Label("🏆 ARCHIVIO DI NIFLHEIM 🏆");
        titleLabel.setStyle("-fx-text-fill: #E1F5FE; -fx-font-size: 16px; -fx-font-weight: bold; -fx-font-family: 'Arial';");

        // --- CONTENITORE ORIZZONTALE PER LE DUE SEZIONI ---
        HBox columnsContainer = new HBox(20);
        columnsContainer.setAlignment(Pos.CENTER);

        // 1. Sezione Vittorie
        VBox victoriesList = new VBox(8);
        victoriesList.setPadding(new Insets(5));
        List<ScoreManager.VictoryEntry> topVictories = ScoreManager.getVictories();
        if (topVictories.isEmpty()) {
            Label emptyLbl = new Label("Nessun eroe ha completato l'impresa.");
            emptyLbl.setStyle("-fx-text-fill: #90CAF9; -fx-font-size: 11px; -fx-padding: 10px;");
            victoriesList.getChildren().add(emptyLbl);
        } else {
            int rank = 1;
            for (ScoreManager.VictoryEntry entry : topVictories) {
                VBox card = createRecordCard(
                        rank + ". " + entry.getHeroName(),
                        "Tempo impiegato: " + entry.getFormattedTime(),
                        "#C8E6C9"
                );
                victoriesList.getChildren().add(card);
                rank++;
            }
        }
        ScrollPane victoriesScroll = createCardScrollContainer(victoriesList, "SALA DEGLI EROI (Vittorie)");

        // --- LINEA DIVISORIA VERTICALE ---
        VBox divider = new VBox();
        divider.setMinWidth(2);
        divider.setPrefHeight(320);
        divider.setStyle("-fx-background-color: rgba(129, 212, 250, 0.3);");

        // 2. Sezione Tentativi (Caduti)
        VBox attemptsList = new VBox(8);
        attemptsList.setPadding(new Insets(5));
        List<ScoreManager.AttemptEntry> recentAttempts = ScoreManager.getAttempts();
        if (recentAttempts.isEmpty()) {
            Label emptyLbl = new Label("Nessun tentativo registrato.");
            emptyLbl.setStyle("-fx-text-fill: #90CAF9; -fx-font-size: 11px; -fx-padding: 10px;");
            attemptsList.getChildren().add(emptyLbl);
        } else {
            for (ScoreManager.AttemptEntry entry : recentAttempts) {
                VBox card = createRecordCard(
                        entry.getHeroName() + "  —  Rune: " + entry.getRunesCollected() + "/5 💎",
                        "Esito: " + entry.getStatus(),
                        "#FFCCBC"
                );
                attemptsList.getChildren().add(card);
            }
        }
        ScrollPane attemptsScroll = createCardScrollContainer(attemptsList, "CRONACHE DEI CADUTI (Tentativi)");

        columnsContainer.getChildren().addAll(victoriesScroll, divider, attemptsScroll);

        Button closeBtn = createStyledButton("CHIUDI", 140);
        closeBtn.setOnAction(e -> root.getChildren().remove(overlay));

        dialogBox.getChildren().addAll(titleLabel, columnsContainer, closeBtn);
        overlay.getChildren().add(dialogBox);
        StackPane.setAlignment(dialogBox, Pos.CENTER);

        root.getChildren().add(overlay);
    }

    private VBox createRecordCard(String mainText, String subText, String accentColor) {
        VBox card = new VBox(4);
        card.setPadding(new Insets(8, 12, 8, 12));
        card.setPrefWidth(350);
        card.setStyle(
                "-fx-background-color: rgba(5, 15, 30, 0.6); " +
                        "-fx-background-radius: 6px; " +
                        "-fx-border-color: rgba(129, 212, 250, 0.2); " +
                        "-fx-border-radius: 6px; " +
                        "-fx-border-width: 1px;"
        );

        Label mainLbl = new Label(mainText);
        mainLbl.setWrapText(true);
        mainLbl.setStyle("-fx-text-fill: " + accentColor + "; -fx-font-size: 12px; -fx-font-weight: bold;");

        Label subLbl = new Label(subText);
        subLbl.setWrapText(true);
        subLbl.setStyle("-fx-text-fill: #B3E5FC; -fx-font-size: 11px;");

        card.getChildren().addAll(mainLbl, subLbl);
        return card;
    }

    private ScrollPane createCardScrollContainer(VBox content, String sectionTitle) {
        VBox outerWrapper = new VBox(8);
        outerWrapper.setPadding(new Insets(10));
        outerWrapper.setStyle(
                "-fx-background-color: rgba(5, 15, 30, 0.4); " +
                        "-fx-background-radius: 10px; " +
                        "-fx-border-color: rgba(129, 212, 250, 0.3); " +
                        "-fx-border-radius: 10px; " +
                        "-fx-border-width: 1px;"
        );

        Label title = new Label(sectionTitle);
        title.setStyle("-fx-text-fill: #81D4FA; -fx-font-size: 12px; -fx-font-weight: bold;");

        ScrollPane scroll = new ScrollPane(content);
        scroll.setPrefWidth(375);
        scroll.setPrefHeight(270);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setStyle(
                "-fx-background: transparent; " +
                        "-fx-background-color: transparent; " +
                        "-fx-border-color: transparent;"
        );
        content.setStyle("-fx-background-color: transparent;");

        outerWrapper.getChildren().addAll(title, scroll);

        ScrollPane finalScroll = new ScrollPane(outerWrapper);
        finalScroll.setPrefWidth(390);
        finalScroll.setPrefHeight(320);
        finalScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        finalScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        finalScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");
        return finalScroll;
    }

    private Button createStyledButton(String text, double width) {
        Button btn = new Button(text);
        btn.setMinWidth(width);
        String style =
                "-fx-background-color: rgba(2, 136, 209, 0.8); " +
                        "-fx-text-fill: #E1F5FE; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 7px 0px; " +
                        "-fx-background-radius: 6px; " +
                        "-fx-border-color: #81D4FA; " +
                        "-fx-border-radius: 6px; " +
                        "-fx-border-width: 1.5px; " +
                        "-fx-cursor: hand;";
        btn.setStyle(style);
        btn.setOnMouseEntered(e -> btn.setStyle(style + "-fx-background-color: rgba(3, 169, 244, 1);"));
        btn.setOnMouseExited(e -> btn.setStyle(style));
        return btn;
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