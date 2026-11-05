package it.unicam.cs.mpgc.rpg130663.beyondthesnow.view;

import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.CellType;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.GridMap;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.Position;
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

public class GameView {
    private final StackPane root; // Contenitore principale per supportare gli overlay
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

    // Cache delle immagini
    private static final Image HERO_IMAGE = loadCachedImage("/images/hero.png");
    private static final Image RUNE_IMAGE = loadCachedImage("/images/rune.png");
    private static final Image OBSTACLE_IMAGE = loadCachedImage("/images/obstacle.png");
    private static final Image ENEMY_IMAGE = loadCachedImage("/images/enemy.png");

    public GameView(String heroName) {
        this.root = new StackPane();
        this.mainLayout = new BorderPane();
        this.gridPane = new GridPane();
        this.cellPanes = new StackPane[GRID_SIZE][GRID_SIZE];
        this.heroNameLabel = new Label("⚔ Eroe: " + heroName);
        this.campButton = new Button("⛺ Accampati (Riposa)");

        initUI();

        // Inseriamo il BorderPane dentro lo StackPane radice
        this.root.getChildren().add(mainLayout);
    }

    private static Image loadCachedImage(String path) {
        try {
            var resource = GameView.class.getResource(path);
            if (resource != null) {
                return new Image(resource.toExternalForm(), 38, 38, true, true);
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
                "-fx-font-size: 18px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 15px; " +
                        "-fx-text-fill: #E3F2FD; " +
                        "-fx-background-color: rgba(13, 71, 161, 0.75); " +
                        "-fx-border-color: rgba(179, 229, 252, 0.3); " +
                        "-fx-border-width: 0 0 1px 0;"
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
                "-fx-background-color: rgba(20, 30, 36, 0.65); " +
                        "-fx-background-radius: 12px; " +
                        "-fx-border-color: rgba(179, 229, 252, 0.25); " +
                        "-fx-border-radius: 12px;"
        );

        BorderPane.setAlignment(gridContainer, Pos.CENTER);
        mainLayout.setCenter(gridContainer);

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

        runesLabel = new Label("🔮 Rune raccolte: 0 / 5");
        runesLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #B39DDB;");

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
        mainLayout.setRight(statsPanel);

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
        mainLayout.setBottom(logArea);
    }

    public void renderMap(GridMap map, Position heroPos) {
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                StackPane cell = cellPanes[x][y];
                Position currentPos = new Position(x, y);
                cell.getChildren().clear();

                if (heroPos != null && currentPos.equals(heroPos)) {
                    cell.setStyle(
                            "-fx-background-color: #FFFFFF; " +
                                    "-fx-border-color: #FF5252; " +
                                    "-fx-border-width: 2.5px; " +
                                    "-fx-border-radius: 6px; " +
                                    "-fx-background-radius: 6px;"
                    );
                    cell.getChildren().add(getNode(HERO_IMAGE, "⚔", "#C62828", 38));

                } else {
                    CellType cellType = map.getCellType(currentPos);

                    switch (cellType) {
                        case RUNE:
                            cell.setStyle(
                                    "-fx-background-color: linear-gradient(to bottom, rgba(126, 87, 194, 0.85), rgba(81, 45, 168, 0.85)); " +
                                            "-fx-border-color: #D1C4E9; " +
                                            "-fx-border-width: 1.5px; " +
                                            "-fx-border-radius: 6px; " +
                                            "-fx-background-radius: 6px;"
                            );
                            cell.getChildren().add(getNode(RUNE_IMAGE, "🔮", "#E1BEE7", 30));
                            break;

                        case OBSTACLE:
                            cell.setStyle(
                                    "-fx-background-color: linear-gradient(to bottom, rgba(96, 125, 139, 0.85), rgba(69, 90, 100, 0.85)); " +
                                            "-fx-border-color: #CFD8DC; " +
                                            "-fx-border-width: 1.5px; " +
                                            "-fx-border-radius: 6px; " +
                                            "-fx-background-radius: 6px;"
                            );
                            cell.getChildren().add(getNode(OBSTACLE_IMAGE, "🧊", "#CFD8DC", 30));
                            break;

                        case ENEMY:
                            cell.setStyle(
                                    "-fx-background-color: linear-gradient(to bottom, rgba(183, 28, 28, 0.85), rgba(127, 0, 0, 0.85)); " +
                                            "-fx-border-color: #FF8A80; " +
                                            "-fx-border-width: 1.5px; " +
                                            "-fx-border-radius: 6px; " +
                                            "-fx-background-radius: 6px;"
                            );
                            cell.getChildren().add(getNode(ENEMY_IMAGE, "👹", "#FFCDD2", 30));
                            break;

                        case EMPTY:
                        default:
                            cell.setStyle(
                                    "-fx-background-color: linear-gradient(to bottom, rgba(240, 248, 255, 0.85), rgba(225, 245, 254, 0.85)); " +
                                            "-fx-border-color: #B3E5FC; " +
                                            "-fx-border-width: 1.5px; " +
                                            "-fx-border-radius: 6px; " +
                                            "-fx-background-radius: 6px;"
                            );
                            break;
                    }
                }
            }
        }
    }

    private Node getNode(Image cachedImage, String fallbackEmoji, String textColor, int size) {
        if (cachedImage != null) {
            ImageView imageView = new ImageView(cachedImage);
            imageView.setFitWidth(size);
            imageView.setFitHeight(size);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(false);
            return imageView;
        }

        Label fallbackLabel = new Label(fallbackEmoji);
        fallbackLabel.setStyle("-fx-font-size: " + (size > 30 ? "18px;" : "14px;") +
                " -fx-text-fill: " + textColor + "; -fx-font-weight: bold;");
        return fallbackLabel;
    }

    // --- Overlay per la schermata di duello tattico ---
    public void showDuelOverlay(Runnable onAttack, Runnable onDefend, Runnable onFlee) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(5, 15, 30, 0.85);");

        VBox duelBox = new VBox(15);
        duelBox.setAlignment(Pos.CENTER);
        duelBox.setMaxWidth(420);
        duelBox.setPadding(new Insets(20));
        duelBox.setStyle(
                "-fx-background-color: rgba(10, 30, 60, 0.95); " +
                        "-fx-background-radius: 12px; " +
                        "-fx-border-color: #FF5252; " +
                        "-fx-border-radius: 12px; " +
                        "-fx-border-width: 2px; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(255, 82, 82, 0.4), 20, 0, 0, 0);"
        );

        Label titleLabel = new Label("⚔ DUELLO NEI GHIACCI ⚔");
        titleLabel.setStyle("-fx-text-fill: #FF8A80; -fx-font-size: 15px; -fx-font-weight: bold;");

        Label descLabel = new Label("Un Guardiano dei Ghiacci ti sbarra la strada! Scegli la tua tattica:");
        descLabel.setWrapText(true);
        descLabel.setStyle("-fx-text-fill: #E1F5FE; -fx-font-size: 12px; -fx-text-alignment: center;");

        Button btnAttack = createDuelButton("⚔ ATTACCO FURENTE (-15 HP, Elimina il Nemico)");
        Button btnDefend = createDuelButton("🛡 DIFESA ATTENTA (-15 Gelo, Preserva la Salute)");
        Button btnFlee = createDuelButton("🏃 SCHIVATA E FUGA (-5 HP, -5 Gelo)");

        btnAttack.setOnAction(e -> {
            root.getChildren().remove(overlay);
            onAttack.run();
        });

        btnDefend.setOnAction(e -> {
            root.getChildren().remove(overlay);
            onDefend.run();
        });

        btnFlee.setOnAction(e -> {
            root.getChildren().remove(overlay);
            onFlee.run();
        });

        duelBox.getChildren().addAll(titleLabel, descLabel, new Label(), btnAttack, btnDefend, btnFlee);
        overlay.getChildren().add(duelBox);
        StackPane.setAlignment(duelBox, Pos.CENTER);

        root.getChildren().add(overlay);
    }

    // --- Overlay di Vittoria ---
    public void showVictoryOverlay(long elapsedSeconds, Runnable onRestart, Runnable onExit) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(5, 15, 30, 0.9);");

        VBox box = new VBox(15);
        box.setAlignment(Pos.CENTER);
        box.setMaxWidth(450);
        box.setPadding(new Insets(25));
        box.setStyle(
                "-fx-background-color: rgba(10, 30, 60, 0.95); " +
                        "-fx-background-radius: 12px; " +
                        "-fx-border-color: #FFE082; " +
                        "-fx-border-radius: 12px; " +
                        "-fx-border-width: 2px;"
        );

        Label title = new Label("🏆 VITTORIA LEGGENDARIA 🏆");
        title.setStyle("-fx-text-fill: #FFE082; -fx-font-size: 16px; -fx-font-weight: bold;");

        Label desc = new Label("Hai recuperato tutte e 5 le Rune Magiche di Niflheim in " + elapsedSeconds + " secondi!");
        desc.setWrapText(true);
        desc.setStyle("-fx-text-fill: #E1F5FE; -fx-font-size: 12px; -fx-text-alignment: center;");

        Button btnRestart = createDuelButton("🔄 Gioca Ancora");
        Button btnExit = createDuelButton("🚪 Torna al Menu");

        btnRestart.setOnAction(e -> onRestart.run());
        btnExit.setOnAction(e -> onExit.run());

        box.getChildren().addAll(title, desc, new Label(), btnRestart, btnExit);
        overlay.getChildren().add(box);
        StackPane.setAlignment(box, Pos.CENTER);

        root.getChildren().add(overlay);
    }

    // --- Overlay di Game Over ---
    public void showGameOverOverlay(Runnable onRestart, Runnable onExit) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(20, 5, 5, 0.9);");

        VBox box = new VBox(15);
        box.setAlignment(Pos.CENTER);
        box.setMaxWidth(450);
        box.setPadding(new Insets(25));
        box.setStyle(
                "-fx-background-color: rgba(40, 10, 10, 0.95); " +
                        "-fx-background-radius: 12px; " +
                        "-fx-border-color: #E53935; " +
                        "-fx-border-radius: 12px; " +
                        "-fx-border-width: 2px;"
        );

        Label title = new Label("💀 SEI MORTO 💀");
        title.setStyle("-fx-text-fill: #FF8A80; -fx-font-size: 16px; -fx-font-weight: bold;");

        Label desc = new Label("Il gelo implacabile di Niflheim ha avuto la meglio sul tuo eroe...");
        desc.setWrapText(true);
        desc.setStyle("-fx-text-fill: #FFCDD2; -fx-font-size: 12px; -fx-text-alignment: center;");

        Button btnRestart = createDuelButton("🔄 Riprova");
        Button btnExit = createDuelButton("🚪 Torna al Menu");

        btnRestart.setOnAction(e -> onRestart.run());
        btnExit.setOnAction(e -> onExit.run());

        box.getChildren().addAll(title, desc, new Label(), btnRestart, btnExit);
        overlay.getChildren().add(box);
        StackPane.setAlignment(box, Pos.CENTER);

        root.getChildren().add(overlay);
    }

    private Button createDuelButton(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle(
                "-fx-background-color: rgba(2, 136, 209, 0.85); " +
                        "-fx-text-fill: #E1F5FE; " +
                        "-fx-font-size: 11px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 9px; " +
                        "-fx-background-radius: 6px; " +
                        "-fx-border-color: #81D4FA; " +
                        "-fx-border-radius: 6px; " +
                        "-fx-cursor: hand;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(btn.getStyle() + "-fx-background-color: rgba(3, 169, 244, 1);"));
        btn.setOnMouseExited(e -> btn.setStyle(btn.getStyle().replace("-fx-background-color: rgba(3, 169, 244, 1);", "")));
        return btn;
    }

    // CORRETTO: adesso riceve anche maxHp e maxFrost per calcolare le percentuali in modo corretto
    public void updateStats(int hp, int maxHp, int cold, int maxCold, int runes) {
        hpBar.setProgress((double) hp / maxHp);
        coldBar.setProgress((double) cold / maxCold);
        runesLabel.setText("🔮 Rune raccolte: " + runes + " / 5");
    }

    public void logMessage(String message) {
        logArea.appendText("\n" + message);
        logArea.setScrollTop(Double.MAX_VALUE);
    }

    public Button getCampButton() {
        return campButton;
    }

    public StackPane getRoot() {
        return root;
    }
}