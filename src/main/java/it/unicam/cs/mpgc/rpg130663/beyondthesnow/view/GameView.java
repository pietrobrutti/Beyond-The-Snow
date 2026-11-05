package it.unicam.cs.mpgc.rpg130663.beyondthesnow.view;

import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.CellType;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.GridMap;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.Position;
import javafx.animation.PauseTransition;
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

import java.util.Random;

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

    // Cache delle immagini (Eroe, Rune, Ostacoli e i 4 Nemici)
    private static final Image HERO_IMAGE = loadCachedImage("/images/hero.png", 300, 300);
    private static final Image RUNE_IMAGE = loadCachedImage("/images/rune.png", 38, 38);
    private static final Image OBSTACLE_IMAGE = loadCachedImage("/images/obstacle.png", 38, 38);

    // Immagini dei nemici
    private static final Image GOBLIN_IMAGE = loadCachedImage("/images/frost_goblin.png", 300, 300);
    private static final Image WRAITH_IMAGE = loadCachedImage("/images/ice_wraith.png", 300, 300);
    private static final Image TROLL_IMAGE = loadCachedImage("/images/frost_troll.png", 300, 300);
    private static final Image THRYM_IMAGE = loadCachedImage("/images/thrym_ice_king.png", 400, 400);

    private final Random random = new Random();

    public GameView(String heroName) {
        this.root = new StackPane();
        this.mainLayout = new BorderPane();
        this.gridPane = new GridPane();
        this.cellPanes = new StackPane[GRID_SIZE][GRID_SIZE];
        this.heroNameLabel = new Label("⚔ Eroe: " + heroName);
        this.campButton = new Button("⛺ Accampati (Riposa)");

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
                    cell.getChildren().add(getNode(HERO_IMAGE, "⚔", "#C62828", 38, false));

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
                            cell.getChildren().add(getNode(RUNE_IMAGE, "🔮", "#E1BEE7", 30, false));
                            break;

                        case OBSTACLE:
                            cell.setStyle(
                                    "-fx-background-color: linear-gradient(to bottom, rgba(96, 125, 139, 0.85), rgba(69, 90, 100, 0.85)); " +
                                            "-fx-border-color: #CFD8DC; " +
                                            "-fx-border-width: 1.5px; " +
                                            "-fx-border-radius: 6px; " +
                                            "-fx-background-radius: 6px;"
                            );
                            cell.getChildren().add(getNode(OBSTACLE_IMAGE, "🧊", "#CFD8DC", 30, false));
                            break;

                        case ENEMY: // I nemici sono nascosti e appaiono uguali alle celle vuote finché non ci si calpesta sopra!
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
        fallbackLabel.setStyle("-fx-font-size: " + (size > 30 ? "18px;" : "14px;") +
                " -fx-text-fill: " + textColor + "; -fx-font-weight: bold;");
        return fallbackLabel;
    }

    // --- Schermata di Combattimento con estrazione variata e bilanciata dei nemici ---
    public void showClassicPokemonBattleOverlay(String heroName, int heroCurrentHp, int heroMaxHp, int heroCurrentCold,
                                                java.util.function.BiConsumer<Integer, Integer> onPerformAttack, Runnable onWin, Runnable onRun) {

        // Estrazione casuale pesata: Goblin (40%), Wraith (30%), Troll (20%), Thrym Boss (10%)
        int roll = random.nextInt(100);
        String enemyName;
        Image enemySelectedImage;
        double enemyMaxHp;

        if (roll < 40) {
            enemyName = "FROST GOBLIN   L.5";
            enemySelectedImage = GOBLIN_IMAGE;
            enemyMaxHp = 60.0;
        } else if (roll < 70) {
            enemyName = "ICE WRAITH   L.10";
            enemySelectedImage = WRAITH_IMAGE;
            enemyMaxHp = 80.0;
        } else if (roll < 90) {
            enemyName = "FROST TROLL   L.15";
            enemySelectedImage = TROLL_IMAGE;
            enemyMaxHp = 120.0;
        } else {
            enemyName = "THRYM, THE ICE KING   L.20";
            enemySelectedImage = THRYM_IMAGE;
            enemyMaxHp = 160.0;
        }

        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(10, 15, 25, 0.95);");

        BorderPane battleLayout = new BorderPane();
        battleLayout.setMaxSize(720, 480);
        battleLayout.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #4A709C, #A4C2E0); " +
                        "-fx-border-color: #1D3450; -fx-border-width: 4px; -fx-border-radius: 8px; -fx-background-radius: 8px;"
        );

        BorderPane topArea = new BorderPane();

        VBox enemyInfoBox = new VBox(4);
        enemyInfoBox.setPadding(new Insets(12));
        enemyInfoBox.setMaxWidth(280);
        enemyInfoBox.setStyle(
                "-fx-background-color: #E8F0F8; -fx-border-color: #7090B0; -fx-border-width: 2px; " +
                        "-fx-border-radius: 6px; -fx-background-radius: 6px;"
        );

        Label enemyNameLabel = new Label(enemyName);
        enemyNameLabel.setStyle("-fx-font-family: 'Consolas'; -fx-font-weight: bold; -fx-font-size: 11px; -fx-text-fill: #202020;");

        ProgressBar enemyHpBar = new ProgressBar(1.0);
        enemyHpBar.setPrefWidth(250);
        enemyHpBar.setStyle("-fx-accent: #50C878;");
        enemyInfoBox.getChildren().addAll(enemyNameLabel, enemyHpBar);

        topArea.setLeft(enemyInfoBox);
        BorderPane.setMargin(enemyInfoBox, new Insets(15, 0, 0, 15));

        ImageView enemyImageView = new ImageView();
        if (enemySelectedImage != null) {
            enemyImageView.setImage(enemySelectedImage);
            enemyImageView.setFitWidth(220);
            enemyImageView.setFitHeight(220);
            enemyImageView.setPreserveRatio(true);
            enemyImageView.setSmooth(false);
        }
        StackPane enemyImageWrapper = new StackPane(enemyImageView);
        enemyImageWrapper.setAlignment(Pos.BOTTOM_RIGHT);
        enemyImageWrapper.setPadding(new Insets(10, 30, 0, 0));
        topArea.setRight(enemyImageWrapper);

        battleLayout.setTop(topArea);

        ImageView heroImageView = new ImageView();
        if (HERO_IMAGE != null) {
            heroImageView.setImage(HERO_IMAGE);
            heroImageView.setFitWidth(150);
            heroImageView.setFitHeight(150);
            heroImageView.setPreserveRatio(true);
            heroImageView.setSmooth(false);
        }
        StackPane heroImageWrapper = new StackPane(heroImageView);
        heroImageWrapper.setAlignment(Pos.BOTTOM_LEFT);
        heroImageWrapper.setPadding(new Insets(70, 0, 5, 45));
        battleLayout.setCenter(heroImageWrapper);

        VBox bottomContainer = new VBox(0);

        VBox heroInfoBox = new VBox(4);
        heroInfoBox.setPadding(new Insets(10, 15, 10, 15));
        heroInfoBox.setMaxWidth(280);
        heroInfoBox.setStyle(
                "-fx-background-color: #E8F0F8; -fx-border-color: #7090B0; -fx-border-width: 2px; " +
                        "-fx-border-radius: 6px; -fx-background-radius: 6px;"
        );

        Label heroInfoLabel = new Label(heroName.toUpperCase() + "   L. 15");
        heroInfoLabel.setStyle("-fx-font-family: 'Consolas'; -fx-font-weight: bold; -fx-font-size: 11px; -fx-text-fill: #202020;");

        ProgressBar battleHeroHpBar = new ProgressBar((double) heroCurrentHp / heroMaxHp);
        battleHeroHpBar.setPrefWidth(250);
        battleHeroHpBar.setStyle("-fx-accent: #50C878;");

        Label heroHpText = new Label("PS: " + heroCurrentHp + " / " + heroMaxHp + "  |  Gelo: " + heroCurrentCold + "%");
        heroHpText.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 10px; -fx-text-fill: #404040;");

        heroInfoBox.getChildren().addAll(heroInfoLabel, battleHeroHpBar, heroHpText);

        VBox heroInfoWrapper = new VBox(heroInfoBox);
        heroInfoWrapper.setAlignment(Pos.TOP_RIGHT);
        heroInfoWrapper.setPadding(new Insets(0, 15, 5, 0));

        BorderPane actionPanel = new BorderPane();
        actionPanel.setPrefHeight(135);
        actionPanel.setStyle(
                "-fx-background-color: #F8F8F8; -fx-border-color: #304870 #203050 #203050 #304870; " +
                        "-fx-border-width: 4px; -fx-border-radius: 2px; -fx-padding: 10px;"
        );

        Label dialogueLabel = new Label("Un nemico nascosto ti tende un'imboscata!\nCosa deve fare " + heroName + "?");
        dialogueLabel.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #202020;");
        actionPanel.setLeft(dialogueLabel);

        VBox actionButtonsBox = new VBox(10);
        actionButtonsBox.setAlignment(Pos.CENTER);

        Button btnFight = createPokemonMenuButton("⚔ LOTTA");
        Button btnRun = createPokemonMenuButton("🏃 FUGA");

        actionButtonsBox.getChildren().addAll(btnFight, btnRun);
        actionPanel.setRight(actionButtonsBox);

        bottomContainer.getChildren().addAll(heroInfoWrapper, actionPanel);
        battleLayout.setBottom(bottomContainer);

        final double[] currentBossHp = { enemyMaxHp };
        final double initialBossHp = enemyMaxHp;
        final int[] currentHp = { heroCurrentHp };
        final int[] currentCold = { heroCurrentCold };

        btnFight.setOnAction(e -> {
            dialogueLabel.setText("Scegli la mossa:\n1. Fendente Furente (Danno medio)\n2. Affondo Ardente (Danno forte)");

            Button move1 = createPokemonMenuButton("1. Fendente");
            Button move2 = createPokemonMenuButton("2. Ardente");
            Button backBtn = createPokemonMenuButton("Indietro");

            VBox movesBox = new VBox(6);
            movesBox.setAlignment(Pos.CENTER);
            movesBox.getChildren().addAll(move1, move2, backBtn);
            actionPanel.setRight(movesBox);

            Runnable executeEnemyTurn = () -> {
                int attackType = random.nextInt(4);
                int coldDmg = 0;
                int hpDmg = 0;
                String attackMsg = "";

                switch (attackType) {
                    case 0:
                        attackMsg = "Il nemico scaglia una raffica di ghiaccio!";
                        coldDmg = 10;
                        hpDmg = 6;
                        break;
                    case 1:
                        attackMsg = "Il nemico scatena una tempesta gelida!";
                        coldDmg = 18;
                        hpDmg = 10;
                        break;
                    case 2:
                        attackMsg = "Il nemico sferra un colpo brutale!";
                        coldDmg = 12;
                        hpDmg = 15;
                        break;
                    case 3:
                    default:
                        attackMsg = "Il nemico drena la tua energia vitale!";
                        coldDmg = 22;
                        hpDmg = 10;
                        break;
                }

                currentCold[0] = Math.max(0, currentCold[0] - coldDmg);
                currentHp[0] = Math.max(0, currentHp[0] - hpDmg);

                battleHeroHpBar.setProgress((double) currentHp[0] / heroMaxHp);
                heroHpText.setText("PS: " + currentHp[0] + " / " + heroMaxHp + "  |  Gelo: " + currentCold[0] + "%");

                onPerformAttack.accept(coldDmg, hpDmg);

                dialogueLabel.setText(attackMsg + "\nPerdi " + coldDmg + "% Gelo e " + hpDmg + " HP.");

                PauseTransition p = new PauseTransition(Duration.seconds(1.8));
                p.setOnFinished(x -> {
                    if (currentHp[0] <= 0 || currentCold[0] <= 0) {
                        root.getChildren().remove(overlay);
                    } else {
                        dialogueLabel.setText("Scegli la tua mossa.");
                        actionPanel.setRight(actionButtonsBox);
                    }
                });
                p.play();
            };

            move1.setOnAction(ev -> {
                currentBossHp[0] -= 25.0;
                enemyHpBar.setProgress(Math.max(0.0, currentBossHp[0] / initialBossHp));

                if (currentBossHp[0] <= 0) {
                    dialogueLabel.setText("Colpo vincente! Nemico sconfitto!");
                    PauseTransition p = new PauseTransition(Duration.seconds(1.5));
                    p.setOnFinished(x -> {
                        root.getChildren().remove(overlay);
                        onWin.run();
                    });
                    p.play();
                } else {
                    dialogueLabel.setText(heroName + " usa Fendente! Il nemico barcolla.");
                    PauseTransition p = new PauseTransition(Duration.seconds(1.2));
                    p.setOnFinished(x -> executeEnemyTurn.run());
                    p.play();
                }
            });

            move2.setOnAction(ev -> {
                currentBossHp[0] -= 45.0;
                enemyHpBar.setProgress(Math.max(0.0, currentBossHp[0] / initialBossHp));

                if (currentBossHp[0] <= 0) {
                    dialogueLabel.setText("Attacco devastante! Nemico abbattuto!");
                    PauseTransition p = new PauseTransition(Duration.seconds(1.5));
                    p.setOnFinished(x -> {
                        root.getChildren().remove(overlay);
                        onWin.run();
                    });
                    p.play();
                } else {
                    dialogueLabel.setText(heroName + " usa Affondo Ardente! Devastante!");
                    PauseTransition p = new PauseTransition(Duration.seconds(1.2));
                    p.setOnFinished(x -> executeEnemyTurn.run());
                    p.play();
                }
            });

            backBtn.setOnAction(ev -> {
                dialogueLabel.setText("Il nemico ti osserva in cagnesco.");
                actionPanel.setRight(actionButtonsBox);
            });
        });

        btnRun.setOnAction(e -> {
            dialogueLabel.setText("Fuggi rocambolescamente dalla battaglia!");
            PauseTransition pause = new PauseTransition(Duration.seconds(1.0));
            pause.setOnFinished(ev -> {
                root.getChildren().remove(overlay);
                onRun.run();
            });
            pause.play();
        });

        overlay.getChildren().add(battleLayout);
        StackPane.setAlignment(battleLayout, Pos.CENTER);
        root.getChildren().add(overlay);
    }

    private Button createPokemonMenuButton(String text) {
        Button btn = new Button(text);
        btn.setPrefSize(140, 32);
        btn.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #F0D870, #D09830); " +
                        "-fx-text-fill: #202020; -fx-font-family: 'Consolas'; -fx-font-weight: bold; -fx-font-size: 11px; " +
                        "-fx-border-color: #704010; -fx-border-width: 2px; -fx-border-radius: 4px; -fx-background-radius: 4px; -fx-cursor: hand;"
        );
        return btn;
    }

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
        return btn;
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

    public Button getCampButton() {
        return campButton;
    }

    public StackPane getRoot() {
        return root;
    }
}