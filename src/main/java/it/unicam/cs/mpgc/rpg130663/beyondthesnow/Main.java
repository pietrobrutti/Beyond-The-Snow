package it.unicam.cs.mpgc.rpg130663.beyondthesnow;

import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.ScoreManager;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.CellType;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.GridMap;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.Position;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.view.GameOverView;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.view.GameView;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.view.MainMenuView;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.view.VictoryView;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        this.primaryStage.setTitle("Beyond the Snow: Niflheim");
        showMainMenu();
    }

    // 1. Mostra il Menu Principale
    private void showMainMenu() {
        MainMenuView menuView = new MainMenuView();
        Scene menuScene = new Scene(menuView.getRoot(), 900, 700);

        menuView.getStartButton().setOnAction(e -> {
            String heroName = menuView.getNameInput().getText();

            // Controllo obbligatorietà del nome con animazione di vibrazione e alert sul campo
            if (heroName == null || heroName.trim().isEmpty()) {
                // Effetto vibrazione (Shake) sul campo di testo
                TranslateTransition shake = new TranslateTransition(Duration.millis(50), menuView.getNameInput());
                shake.setByX(10f);
                shake.setCycleCount(6);
                shake.setAutoReverse(true);
                shake.play();

                // Salva lo stile originale e applica lo stile di errore (rosso)
                String originalStyle = menuView.getNameInput().getStyle();
                menuView.getNameInput().setStyle(
                        "-fx-background-color: rgba(40, 10, 15, 0.9); " +
                                "-fx-text-fill: #FF8A80; " +
                                "-fx-prompt-text-fill: #FF5252; " +
                                "-fx-font-weight: bold; " +
                                "-fx-alignment: center; " +
                                "-fx-padding: 7px; " +
                                "-fx-background-radius: 6px; " +
                                "-fx-border-color: #FF5252; " +
                                "-fx-border-radius: 6px; " +
                                "-fx-border-width: 2px;"
                );
                menuView.getNameInput().setPromptText("DEVI INSERIRE PER FORZA UN NOME!");

                // Ripristina lo stile normale appena l'utente comincia a scrivere
                menuView.getNameInput().setOnKeyTyped(eventKey -> {
                    menuView.getNameInput().setStyle(originalStyle);
                    menuView.getNameInput().setPromptText("Inserisci il tuo nome...");
                    menuView.getNameInput().setOnKeyTyped(null);
                });

            } else {
                showGameScene(heroName.trim());
            }
        });

        menuView.getExitButton().setOnAction(e -> primaryStage.close());

        primaryStage.setScene(menuScene);
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    // 2. Mostra la schermata di gioco principale con i controlli da tastiera
    private void showGameScene(String heroName) {
        GameView gameView = new GameView(heroName);
        Scene gameScene = new Scene(gameView.getRoot(), 950, 700);

        primaryStage.setScene(gameScene);
        primaryStage.centerOnScreen();

        gameView.logMessage("[INFO] L'eroe " + heroName + " è entrato nelle terre ghiacciate di Niflheim.");

        final long startTime = System.currentTimeMillis();
        GridMap map = new GridMap(10, 10);

        final Position[] heroPos = { new Position(0, 0) };
        final int[] hp = { 100 };
        final int maxHp = 100;
        final int[] cold = { 0 };
        final int[] runesCollected = { 0 };

        gameView.renderMap(map, heroPos[0]);
        gameView.updateStats(hp[0], maxHp, cold[0], runesCollected[0]);

        gameScene.setOnKeyPressed(event -> {
            int newX = heroPos[0].getX();
            int newY = heroPos[0].getY();

            switch (event.getCode()) {
                case UP:
                case W:
                    newY--;
                    break;
                case DOWN:
                case S:
                    newY++;
                    break;
                case LEFT:
                case A:
                    newX--;
                    break;
                case RIGHT:
                case D:
                    newX++;
                    break;
                default:
                    return;
            }

            Position targetPos = new Position(newX, newY);

            if (map.isValidPosition(targetPos)) {
                CellType cellType = map.getCellType(targetPos);

                if (cellType != CellType.OBSTACLE) {
                    heroPos[0] = targetPos;

                    if (cellType == CellType.RUNE) {
                        map.setCellType(targetPos, CellType.EMPTY);
                        runesCollected[0]++;
                        gameView.logMessage("[EVENTO] Hai raccolto una runa! (" + runesCollected[0] + " / 5)");

                        if (runesCollected[0] >= 5) {
                            gameView.logMessage("[VITTORIA] Hai recuperato tutte le rune di Niflheim!");

                            long elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000;
                            ScoreManager.addScore(heroName, elapsedSeconds);

                            PauseTransition pause = new PauseTransition(Duration.seconds(1.0));
                            pause.setOnFinished(ev -> showVictoryScene(heroName, elapsedSeconds));
                            pause.play();
                        }
                    }

                    gameView.renderMap(map, heroPos[0]);
                    gameView.updateStats(hp[0], maxHp, cold[0], runesCollected[0]);

                } else {
                    gameView.logMessage("[ATTENZIONE] Un blocco di ghiaccio sbarra la strada!");
                }
            } else {
                gameView.logMessage("[ATTENZIONE] Sei ai confini della mappa!");
            }
        });

        Platform.runLater(() -> gameView.getRoot().requestFocus());
        gameView.getRoot().setOnMouseClicked(e -> gameView.getRoot().requestFocus());

        gameView.getCampButton().setOnAction(e -> showGameOverScene(heroName));
    }

    private void showGameOverScene(String heroName) {
        GameOverView gameOverView = new GameOverView(heroName);
        Scene gameOverScene = new Scene(gameOverView.getRoot(), 800, 600);

        gameOverView.getRestartButton().setOnAction(e -> showGameScene(heroName));
        gameOverView.getMenuButton().setOnAction(e -> showMainMenu());

        primaryStage.setScene(gameOverScene);
        primaryStage.centerOnScreen();
    }

    private void showVictoryScene(String heroName, long elapsedSeconds) {
        VictoryView victoryView = new VictoryView(heroName, elapsedSeconds);
        Scene victoryScene = new Scene(victoryView.getRoot(), 800, 600);

        victoryView.getMenuButton().setOnAction(e -> showMainMenu());
        victoryView.getExitButton().setOnAction(e -> primaryStage.close());

        primaryStage.setScene(victoryScene);
        primaryStage.centerOnScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }
}