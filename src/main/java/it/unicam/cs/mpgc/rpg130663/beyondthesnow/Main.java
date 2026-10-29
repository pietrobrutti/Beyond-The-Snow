package it.unicam.cs.mpgc.rpg130663.beyondthesnow;

import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.CellType;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.GridMap;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.Position;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.view.GameOverView;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.view.GameView;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.view.WelcomeView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        this.primaryStage.setTitle("Beyond the Snow: Niflheim");
        showWelcomeScene();
    }

    // 1. Mostra la schermata di benvenuto iniziale
    private void showWelcomeScene() {
        WelcomeView welcomeView = new WelcomeView();
        Scene welcomeScene = new Scene(welcomeView.getRoot(), 800, 600);

        welcomeView.getStartButton().setOnAction(e -> {
            String heroName = welcomeView.getHeroName();
            showGameScene(heroName);
        });

        welcomeView.getExitButton().setOnAction(e -> primaryStage.close());

        primaryStage.setScene(welcomeScene);
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

        // Inizializzazione della mappa 10x10 con rune e ostacoli casuali
        GridMap map = new GridMap(10, 10);

        // Stato dell'eroe
        final Position[] heroPos = { new Position(0, 0) };
        final int[] hp = { 100 };
        final int maxHp = 100;
        final int[] cold = { 0 };
        final int[] runesCollected = { 0 };

        // Renderizziamo la mappa iniziale e le statistiche
        gameView.renderMap(map, heroPos[0]);
        gameView.updateStats(hp[0], maxHp, cold[0], runesCollected[0]);

        // === GESTIONE CONTROLLI DA TASTIERA (WASD / Frecce) ===
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
                    return; // Ignora altri tasti
            }

            Position targetPos = new Position(newX, newY);

            // Verifica confini 10x10
            if (map.isValidPosition(targetPos)) {
                CellType cellType = map.getCellType(targetPos);

                // Verifica che non sia un ostacolo di ghiaccio
                if (cellType != CellType.OBSTACLE) {
                    heroPos[0] = targetPos; // Aggiorna posizione

                    // Raccolta Runa
                    if (cellType == CellType.RUNE) {
                        map.setCellType(targetPos, CellType.EMPTY);
                        runesCollected[0]++;
                        gameView.logMessage("[EVENTO] Hai raccolto una runa! (" + runesCollected[0] + " / 5)");

                        if (runesCollected[0] >= 5) {
                            gameView.logMessage("[VITTORIA] Hai recuperato tutte le rune di Niflheim!");
                        }
                    }

                    // Aggiorna grafica e statistiche
                    gameView.renderMap(map, heroPos[0]);
                    gameView.updateStats(hp[0], maxHp, cold[0], runesCollected[0]);

                } else {
                    gameView.logMessage("[ATTENZIONE] Un blocco di ghiaccio sbarra la strada!");
                }
            } else {
                gameView.logMessage("[ATTENZIONE] Sei ai confini della mappa!");
            }
        });

        // Forza il focus sulla schermata di gioco per abilitare subito i tasti
        Platform.runLater(() -> gameView.getRoot().requestFocus());
        gameView.getRoot().setOnMouseClicked(e -> gameView.getRoot().requestFocus());

        // Pulsante Accampati (collegato al Game Over per test)
        gameView.getCampButton().setOnAction(e -> showGameOverScene(heroName));
    }

    // 3. Mostra la schermata di Game Over quando l'eroe muore
    private void showGameOverScene(String heroName) {
        GameOverView gameOverView = new GameOverView(heroName);
        Scene gameOverScene = new Scene(gameOverView.getRoot(), 800, 600);

        gameOverView.getRestartButton().setOnAction(e -> showGameScene(heroName));
        gameOverView.getMenuButton().setOnAction(e -> showWelcomeScene());

        primaryStage.setScene(gameOverScene);
        primaryStage.centerOnScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }
}