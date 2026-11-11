package it.unicam.cs.mpgc.rpg130663.beyondthesnow.view;

import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.CellType;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.GridMap;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.Position;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class GameView {
    private final StackPane root;
    private final BorderPane mainLayout;
    private final GridPane gridPane;
    private static final int GRID_SIZE = 10;
    private final StackPane[][] cellPanes;

    private final Label heroNameLabel;
    private ProgressBar hpBar;
    private ProgressBar coldBar;
    private Label runesLabel;
    private TextArea logArea;
    private final Button campButton;
    private final Button meltButton;

    // Cache delle immagini pubbliche o accessibili per le altre viste
    public static final Image HERO_IMAGE = loadCachedImage("/images/hero.png", 300, 300);
    private static final Image RUNE_IMAGE = loadCachedImage("/images/rune.png", 38, 38);
    private static final Image OBSTACLE_IMAGE = loadCachedImage("/images/obstacle.png", 38, 38);

    public static final Image GOBLIN_IMAGE = loadCachedImage("/images/frost_goblin.png", 300, 300);
    public static final Image WRAITH_IMAGE = loadCachedImage("/images/ice_wraith.png", 300, 300);
    public static final Image TROLL_IMAGE = loadCachedImage("/images/frost_troll.png", 300, 300);
    public static final Image THRYM_IMAGE = loadCachedImage("/images/thrym_ice_king.png", 400, 400);

    private Position highlightedIcePos = null;

    public GameView(String heroName) {
        this.root = new StackPane();
        this.mainLayout = new BorderPane();
        this.gridPane = new GridPane();
        this.cellPanes = new StackPane[GRID_SIZE][GRID_SIZE];
        this.heroNameLabel = new Label("⚔ Eroe: " + heroName);
        this.campButton = new Button("⛺ Accampati (Riposa)");
        this.meltButton = new Button("🔥 Sciogli Ghiaccio (-15 HP)");

        initUI();
        this.root.getChildren().add(mainLayout);
    }

    private static Image loadCachedImage(String path, double width, double height) {
        try {
            var resource = GameView.class.getResource(path);
            if (resource != null) {
                return new Image(resource.toExternalForm(), width, height, true, false);
            }
        } catch (Exception e) {
            // Ignora se non trovato
        }
        return null;
    }

    private void initUI() {
        try {
            String imagePath = getClass().getResource("/images/WelcomePhoto.png").toExternalForm();
            mainLayout.setStyle(
                    "-fx-background-image: url('" + imagePath + "'); " +
                            "-fx-background-size: cover; " +
                            "-fx-background-repeat: no-repeat; " +
                            "-fx-background-position: center center;"
            );
        } catch (Exception e) {
            mainLayout.setStyle("-fx-background-color: #1E272C;");
        }

        Label headerLabel = new Label("❄ BEYOND THE SNOW: NIFLHEIM ❄");
        headerLabel.setStyle(
                "-fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 15px; -fx-text-fill: #E3F2FD; " +
                        "-fx-background-color: rgba(13, 71, 161, 0.75); -fx-border-color: rgba(179, 229, 252, 0.3); -fx-border-width: 0 0 1px 0;"
        );
        BorderPane.setAlignment(headerLabel, Pos.CENTER);
        mainLayout.setTop(headerLabel);

        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(4);
        gridPane.setVgap(4);

        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                StackPane cell = new StackPane();
                cell.setMinSize(45, 45);
                cell.setPrefSize(45, 45);
                cellPanes[x][y] = cell;
                gridPane.add(cell, x, y);
            }
        }

        StackPane gridContainer = new StackPane(gridPane);
        gridContainer.setAlignment(Pos.CENTER);
        gridContainer.setPadding(new Insets(20, 25, 30, 25));
        gridContainer.setStyle(
                "-fx-background-color: rgba(20, 30, 36, 0.65); -fx-background-radius: 12px; " +
                        "-fx-border-color: rgba(179, 229, 252, 0.25); -fx-border-radius: 12px;"
        );
        BorderPane.setAlignment(gridContainer, Pos.CENTER);
        mainLayout.setCenter(gridContainer);

        VBox statsPanel = new VBox(15);
        statsPanel.setStyle(
                "-fx-padding: 20px; -fx-background-color: rgba(20, 30, 36, 0.75); " +
                        "-fx-border-color: rgba(179, 229, 252, 0.25); -fx-border-width: 0 0 0 1px;"
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

        runesLabel = new Label("🔮 Rune raccolte: 0 / 5");
        runesLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #B39DDB;");

        campButton.setMaxWidth(Double.MAX_VALUE);
        campButton.setStyle(
                "-fx-background-color: rgba(25, 118, 210, 0.85); -fx-text-fill: white; -fx-font-weight: bold; " +
                        "-fx-padding: 10px; -fx-background-radius: 6px; -fx-cursor: hand;"
        );

        meltButton.setMaxWidth(Double.MAX_VALUE);
        meltButton.setStyle(
                "-fx-background-color: rgba(211, 47, 47, 0.85); -fx-text-fill: white; -fx-font-weight: bold; " +
                        "-fx-padding: 10px; -fx-background-radius: 6px; -fx-cursor: hand;"
        );

        statsPanel.getChildren().addAll(heroNameLabel, new Label(), hpTitle, hpBar, coldTitle, coldBar, runesLabel, new Label(), campButton, meltButton);
        mainLayout.setRight(statsPanel);

        logArea = new TextArea();
        logArea.setPrefHeight(90);
        logArea.setEditable(false);
        logArea.setWrapText(true);
        logArea.setStyle(
                "-fx-control-inner-background: rgba(15, 23, 28, 0.85); -fx-text-fill: #CFD8DC; " +
                        "-fx-font-family: 'Consolas', monospace; -fx-font-size: 12px;"
        );
        logArea.setText("[SISTEMA] Benvenuto in Beyond the Snow. Il gelo di Niflheim mette a dura prova la tua tempra...");
        mainLayout.setBottom(logArea);
    }

    public void setHighlightedIcePos(Position pos) {
        this.highlightedIcePos = pos;
    }

    public void triggerScreenShake() {
        TranslateTransition tt = new TranslateTransition(Duration.millis(50), root);
        tt.setFromX(0);
        tt.setByX(6);
        tt.setCycleCount(4);
        tt.setAutoReverse(true);
        tt.play();
    }

    public void renderMap(GridMap map, Position heroPos) {
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                StackPane cell = cellPanes[x][y];
                Position currentPos = new Position(x, y);
                cell.getChildren().clear();

                if (heroPos != null && currentPos.equals(heroPos)) {
                    cell.setStyle(
                            "-fx-background-color: #FFFFFF; -fx-border-color: #FF5252; " +
                                    "-fx-border-width: 2.5px; -fx-border-radius: 6px; -fx-background-radius: 6px;"
                    );
                    cell.getChildren().add(getNode(HERO_IMAGE, "⚔", "#C62828", 38, false));
                } else {
                    CellType cellType = map.getCellType(currentPos);
                    switch (cellType) {
                        case RUNE -> {
                            cell.setStyle(
                                    "-fx-background-color: linear-gradient(to bottom, rgba(126, 87, 194, 0.85), rgba(81, 45, 168, 0.85)); " +
                                            "-fx-border-color: #D1C4E9; -fx-border-width: 1.5px; -fx-border-radius: 6px; -fx-background-radius: 6px;"
                            );
                            cell.getChildren().add(getNode(RUNE_IMAGE, "🔮", "#E1BEE7", 30, false));
                        }
                        case OBSTACLE -> {
                            boolean isHighlighted = (highlightedIcePos != null && highlightedIcePos.equals(currentPos));
                            String borderColor = isHighlighted ? "#FF1744" : "#CFD8DC";
                            double borderWidth = isHighlighted ? 3.0 : 1.5;

                            cell.setStyle(
                                    "-fx-background-color: linear-gradient(to bottom, rgba(96, 125, 139, 0.85), rgba(69, 90, 100, 0.85)); " +
                                            "-fx-border-color: " + borderColor + "; -fx-border-width: " + borderWidth + "px; " +
                                            "-fx-border-radius: 6px; -fx-background-radius: 6px;"
                            );
                            cell.getChildren().add(getNode(OBSTACLE_IMAGE, "🧊", "#CFD8DC", 30, false));
                        }
                        default -> {
                            cell.setStyle(
                                    "-fx-background-color: linear-gradient(to bottom, rgba(240, 248, 255, 0.85), rgba(225, 245, 254, 0.85)); " +
                                            "-fx-border-color: #B3E5FC; -fx-border-width: 1.5px; -fx-border-radius: 6px; -fx-background-radius: 6px;"
                            );
                        }
                    }
                }
            }
        }
    }

    private Node getNode(Image cachedImage, String fallbackEmoji, String textColor, int size, boolean isSmooth) {
        if (cachedImage != null) {
            ImageView imageView = new ImageView(cachedImage);
            imageView.setFitWidth(size);
            imageView.setFitHeight(size);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(isSmooth);
            return imageView;
        }
        Label fallbackLabel = new Label(fallbackEmoji);
        fallbackLabel.setStyle("-fx-font-size: " + (size > 30 ? "18px;" : "14px;") + " -fx-text-fill: " + textColor + "; -fx-font-weight: bold;");
        return fallbackLabel;
    }

    public void updateStats(int hp, int maxHp, int cold, int maxCold, int runes) {
        hpBar.setProgress((double) hp / maxHp);
        coldBar.setProgress((double) cold / maxCold);
        runesLabel.setText("🔮 Rune raccolte: " + runes + " / 5");
    }

    public void logMessage(String message) {
        logArea.appendText("\n" + message);
        logArea.setScrollTop(Double.MAX_VALUE);
    }

    public Button getCampButton() { return campButton; }
    public Button getMeltButton() { return meltButton; }
    public StackPane getRoot() { return root; }
}