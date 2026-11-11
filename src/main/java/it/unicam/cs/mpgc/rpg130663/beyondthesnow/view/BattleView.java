package it.unicam.cs.mpgc.rpg130663.beyondthesnow.view;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.Random;
import java.util.function.BiConsumer;

public class BattleView {
    private final StackPane overlay;
    private final Random random = new Random();

    public BattleView(String heroName, int heroCurrentHp, int heroMaxHp, int heroCurrentCold,
                      Image heroImage, Image goblinImage, Image wraithImage, Image trollImage, Image thrymImage,
                      BiConsumer<Integer, Integer> onPerformAttack, Runnable onWin, Runnable onRun) {

        this.overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(10, 15, 25, 0.95);");

        // Selezione casuale del nemico
        int roll = random.nextInt(100);
        String enemyName;
        Image enemySelectedImage;
        double enemyMaxHp;

        if (roll < 40) {
            enemyName = "FROST GOBLIN   L.5";
            enemySelectedImage = goblinImage;
            enemyMaxHp = 60.0;
        } else if (roll < 70) {
            enemyName = "ICE WRAITH   L.10";
            enemySelectedImage = wraithImage;
            enemyMaxHp = 80.0;
        } else if (roll < 90) {
            enemyName = "FROST TROLL   L.15";
            enemySelectedImage = trollImage;
            enemyMaxHp = 120.0;
        } else {
            enemyName = "THRYM, THE ICE KING   L.20";
            enemySelectedImage = thrymImage;
            enemyMaxHp = 160.0;
        }

        BorderPane battleLayout = new BorderPane();
        battleLayout.setMaxSize(720, 480);
        battleLayout.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #4A709C, #A4C2E0); " +
                        "-fx-border-color: #1D3450; -fx-border-width: 4px; -fx-border-radius: 8px; -fx-background-radius: 8px;"
        );

        // Area superiore (Nemico)
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

        // Immagine Eroe (Centro)
        ImageView heroImageView = new ImageView();
        if (heroImage != null) {
            heroImageView.setImage(heroImage);
            heroImageView.setFitWidth(150);
            heroImageView.setFitHeight(150);
            heroImageView.setPreserveRatio(true);
            heroImageView.setSmooth(false);
        }
        StackPane heroImageWrapper = new StackPane(heroImageView);
        heroImageWrapper.setAlignment(Pos.BOTTOM_LEFT);
        heroImageWrapper.setPadding(new Insets(70, 0, 5, 45));
        battleLayout.setCenter(heroImageWrapper);

        // Area inferiore (Azioni e Statistiche Eroe)
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
            dialogueLabel.setText("Scegli la mossa:\n1. Fendente (Danno medio)\n2. Ardente (Danno forte)");

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
                String attackMsg = switch (attackType) {
                    case 0 -> { coldDmg = 10; hpDmg = 6; yield "Il nemico scaglia una raffica di ghiaccio!"; }
                    case 1 -> { coldDmg = 18; hpDmg = 10; yield "Il nemico scatena una tempesta gelida!"; }
                    case 2 -> { coldDmg = 12; hpDmg = 15; yield "Il nemico sferra un colpo brutale!"; }
                    default -> { coldDmg = 22; hpDmg = 10; yield "Il nemico drena la tua energia vitale!"; }
                };

                currentCold[0] = Math.max(0, currentCold[0] - coldDmg);
                currentHp[0] = Math.max(0, currentHp[0] - hpDmg);

                battleHeroHpBar.setProgress((double) currentHp[0] / heroMaxHp);
                heroHpText.setText("PS: " + currentHp[0] + " / " + heroMaxHp + "  |  Gelo: " + currentCold[0] + "%");

                onPerformAttack.accept(coldDmg, hpDmg);
                dialogueLabel.setText(attackMsg + "\nPerdi " + coldDmg + "% Gelo e " + hpDmg + " HP.");

                PauseTransition p = new PauseTransition(Duration.seconds(1.8));
                p.setOnFinished(x -> {
                    if (currentHp[0] <= 0 || currentCold[0] <= 0) {
                        // Chiusura gestita all'esterno
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
                    p.setOnFinished(x -> onWin.run());
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
                    p.setOnFinished(x -> onWin.run());
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
            pause.setOnFinished(ev -> onRun.run());
            pause.play();
        });

        overlay.getChildren().add(battleLayout);
        StackPane.setAlignment(battleLayout, Pos.CENTER);
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

    public StackPane getOverlay() {
        return overlay;
    }
}
