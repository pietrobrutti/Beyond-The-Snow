package it.unicam.cs.mpgc.rpg130663.beyondthesnow.view;

import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.GridMap;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.Position;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class GameView {
    private final BorderPane root;
    private final GridPane gridPane;
    private static final int GRID_SIZE = 12;
    private final Pane[][] cellPanes;

    private final Label heroNameLabel;
    private ProgressBar hpBar;
    private ProgressBar coldBar;
    private Label runesLabel;
    private TextArea logArea;

    public GameView(String heroName) {
        this.root = new BorderPane();
        this.gridPane = new GridPane();
        this.cellPanes = new Pane[GRID_SIZE][GRID_SIZE];
        this.heroNameLabel = new Label("⚔ Eroe: " + heroName);
        initUI();
    }

    private void initUI() {
        // --- SFONDO: Immagine unica a schermo intero senza ripetizioni ---
        try {
            String imagePath = getClass().getResource("/images/WelcomePhoto.png").toExternalForm();
            root.setStyle(
                    "-fx-background-image: url('" + imagePath + "'); " +
                            "-fx-background-size: cover; " +
                            "-fx-background-repeat: no-repeat; " +
                            "-fx-background-position: center center;"
            );
        } catch (Exception e) {
            root.setStyle("-fx-background-color: #1E272C;");
        }

        // --- TOP: Header elegante e semitrasparente ---
        Label headerLabel = new Label("❄ BEYOND THE SNOW: NIFLHEIM ❄");
        headerLabel.setStyle(
                "-fx-font-size: 18px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 15px; " +
                        "-fx-text-fill: #E3F2FD; " +
                        "-fx-background-color: rgba(13, 71, 161, 0.75); " +
                        "-fx-border-color: rgba(179, 229, 252, 0.3); " +
                        "-fx-border-width: 0 0 1px 0;"
        );
        BorderPane.setAlignment(headerLabel, Pos.CENTER);
        root.setTop(headerLabel);

        // --- CENTER: Griglia di gioco ---
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(4);
        gridPane.setVgap(4);

        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                Pane cell = new Pane();
                cell.setMinSize(45, 45);
                cell.setPrefSize(45, 45);
                cell.setStyle(
                        "-fx-background-color: linear-gradient(to bottom, rgba(240, 248, 255, 0.85), rgba(225, 245, 254, 0.85)); " +
                                "-fx-border-color: #B3E5FC; " +
                                "-fx-border-width: 1.5px; " +
                                "-fx-border-radius: 6px; " +
                                "-fx-background-radius: 6px;"
                );
                cellPanes[x][y] = cell;
                gridPane.add(cell, x, y);
            }
        }

        // Contenitore della griglia con effetto "vetro scuro"
        StackPane gridContainer = new StackPane(gridPane);
        gridContainer.setAlignment(Pos.CENTER);
        gridContainer.setPadding(new Insets(20, 25, 30, 25));
        gridContainer.setStyle(
                "-fx-background-color: rgba(20, 30, 36, 0.65); " +
                        "-fx-background-radius: 12px; " +
                        "-fx-border-color: rgba(179, 229, 252, 0.25); " +
                        "-fx-border-radius: 12px;"
        );

        BorderPane.setAlignment(gridContainer, Pos.CENTER);
        root.setCenter(gridContainer);

        // --- RIGHT: Pannello Statistiche (Sidebar in stile vetro scuro coordinato) ---
        VBox statsPanel = new VBox(15);
        statsPanel.setStyle(
                "-fx-padding: 20px; " +
                        "-fx-background-color: rgba(20, 30, 36, 0.75); " +
                        "-fx-border-color: rgba(179, 229, 252, 0.25); " +
                        "-fx-border-width: 0 0 0 1px;"
        );
        statsPanel.setPrefWidth(260);

        heroNameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #E3F2FD;");

        Label hpTitle = new Label("❤ Salute (HP):");
        hpTitle.setStyle("-fx-text-fill: #CFD8DC; -fx-font-weight: bold;");
        hpBar = new ProgressBar(1.0);
        hpBar.setPrefWidth(220);
        hpBar.setStyle("-fx-accent: #E53935;");

        Label coldTitle = new Label("❄ Resistenza al Gelo:");
        coldTitle.setStyle("-fx-text-fill: #CFD8DC; -fx-font-weight: bold;");
        coldBar = new ProgressBar(1.0);
        coldBar.setPrefWidth(220);
        coldBar.setStyle("-fx-accent: #00ACC1;");

        runesLabel = new Label("🔮 Rune raccolte: 0 / 3");
        runesLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #B39DDB;");

        Button campButton = new Button("⛺ Accampati (Riposa)");
        campButton.setMaxWidth(Double.MAX_VALUE);
        campButton.setStyle(
                "-fx-background-color: rgba(25, 118, 210, 0.85); " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10px; " +
                        "-fx-background-radius: 6px; " +
                        "-fx-cursor: hand;"
        );

        statsPanel.getChildren().addAll(heroNameLabel, new Label(), hpTitle, hpBar, coldTitle, coldBar, runesLabel, new Label(), campButton);
        root.setRight(statsPanel);

        // --- BOTTOM: Console di Log (Stile terminale ghiacciato semitrasparente) ---
        logArea = new TextArea();
        logArea.setPrefHeight(90);
        logArea.setEditable(false);
        logArea.setWrapText(true);
        logArea.setStyle(
                "-fx-control-inner-background: rgba(15, 23, 28, 0.85); " +
                        "-fx-text-fill: #CFD8DC; " +
                        "-fx-font-family: 'Consolas', monospace; " +
                        "-fx-font-size: 12px;"
        );
        logArea.setText("[SISTEMA] Benvenuto in Beyond the Snow. Il gelo di Niflheim mette a dura prova la tua tempra...");
        root.setBottom(logArea);
    }

    public void renderMap(GridMap map, Position heroPos) {
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                Pane cell = cellPanes[x][y];
                Position currentPos = new Position(x, y);

                if (heroPos != null && currentPos.equals(heroPos)) {
                    cell.setStyle(
                            "-fx-background-color: linear-gradient(to bottom, #EF5350, #C62828); " +
                                    "-fx-border-color: #B71C1C; " +
                                    "-fx-border-width: 2px; " +
                                    "-fx-border-radius: 6px; " +
                                    "-fx-background-radius: 6px;"
                    );
                } else {
                    cell.setStyle(
                            "-fx-background-color: linear-gradient(to bottom, rgba(240, 248, 255, 0.85), rgba(225, 245, 254, 0.85)); " +
                                    "-fx-border-color: #B3E5FC; " +
                                    "-fx-border-width: 1.5px; " +
                                    "-fx-border-radius: 6px; " +
                                    "-fx-background-radius: 6px;"
                    );
                }
            }
        }
    }

    public void updateStats(int hp, int maxHp, int cold, int runes) {
        hpBar.setProgress((double) hp / maxHp);
        coldBar.setProgress((double) cold / 100.0);
        runesLabel.setText("🔮 Rune raccolte: " + runes + " / 3");
    }

    public void logMessage(String message) {
        logArea.appendText("\n" + message);
        logArea.setScrollTop(Double.MAX_VALUE);
    }

    public BorderPane getRoot() {
        return root;
    }
}