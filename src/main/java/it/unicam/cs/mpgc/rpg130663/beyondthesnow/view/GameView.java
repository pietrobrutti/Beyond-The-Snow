package it.unicam.cs.mpgc.rpg130663.beyondthesnow.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

    public GameView() {
        this.root = new BorderPane();
        this.gridPane = new GridPane();
        this.cellPanes = new Pane[GRID_SIZE][GRID_SIZE];
        initUI();
    }

    private void initUI() {
        // --- TOP: Titolo del gioco ---
        Label headerLabel = new Label("BEYOND THE SNOW - Esplorazione di Niflheim (Anteprima Estetica)");
        headerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 12px; -fx-text-fill: #263238;");
        BorderPane.setAlignment(headerLabel, Pos.CENTER_LEFT);
        root.setTop(headerLabel);

        // --- CENTER: Griglia 12x12 ---
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(3);
        gridPane.setVgap(3);
        gridPane.setPadding(new Insets(5));

        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                Pane cell = new Pane();
                cell.setMinSize(42, 42);
                // Effetto neve iniziale con bordi delicati
                cell.setStyle("-fx-background-color: #f0f8ff; -fx-border-color: #90caf9; -fx-border-radius: 4px; -fx-background-radius: 4px;");

                // Coloriamo una cella centrale (es. 5,5) per simulare l'eroe
                if (x == 5 && y == 5) {
                    cell.setStyle("-fx-background-color: #d32f2f; -fx-border-color: #b71c1c; -fx-border-radius: 4px; -fx-background-radius: 4px;");
                }

                cellPanes[x][y] = cell;
                gridPane.add(cell, x, y);
            }
        }

        StackPane gridContainer = new StackPane(gridPane);
        gridContainer.setAlignment(Pos.CENTER);
        gridContainer.setStyle("-fx-background-color: #cfd8dc; -fx-padding: 25px;");
        root.setCenter(gridContainer);

        // --- RIGHT: Pannello Statistiche Laterale ---
        VBox statsPanel = new VBox(15);
        statsPanel.setStyle("-fx-padding: 20px; -fx-background-color: #e0f7fa; -fx-border-color: #b0bec5;");
        statsPanel.setPrefWidth(240);

        Label heroName = new Label("Eroe: Ragnar");
        heroName.setStyle("-fx-font-weight: bold; -fx-font-size: 15px; -fx-text-fill: #006064;");

        Label hpTitle = new Label("Salute (HP):");
        ProgressBar hpBar = new ProgressBar(1.0);
        hpBar.setPrefWidth(200);
        hpBar.setStyle("-fx-accent: #e53935;");

        Label coldTitle = new Label("Resistenza Freddo:");
        ProgressBar coldBar = new ProgressBar(1.0);
        coldBar.setPrefWidth(200);
        coldBar.setStyle("-fx-accent: #00acc1;");

        Label runesLabel = new Label("Rune raccolte: 0 / 3");
        runesLabel.setStyle("-fx-font-weight: bold;");

        statsPanel.getChildren().addAll(heroName, new Label(), hpTitle, hpBar, coldTitle, coldBar, runesLabel);
        root.setRight(statsPanel);

        // --- BOTTOM: Console di Log ---
        TextArea logArea = new TextArea();
        logArea.setPrefHeight(90);
        logArea.setEditable(false);
        logArea.setStyle("-fx-control-inner-background: #263238; -fx-text-fill: #eceff1; -fx-font-family: monospace;");
        logArea.setText("[GRAFICA] Griglia 12x12 caricata correttamente. La cella rossa indica la posizione iniziale.");
        root.setBottom(logArea);
    }

    public BorderPane getRoot() {
        return root;
    }
}
