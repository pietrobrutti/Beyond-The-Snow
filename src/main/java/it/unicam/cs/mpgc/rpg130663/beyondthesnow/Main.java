package it.unicam.cs.mpgc.rpg130663.beyondthesnow;

import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.GridMap;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.Position;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.view.GameOverView;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.view.GameView;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.view.WelcomeView;
import javafx.application.Application;
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

    // 2. Mostra la schermata di gioco principale e popola la griglia
    private void showGameScene(String heroName) {
        GameView gameView = new GameView(heroName);
        Scene gameScene = new Scene(gameView.getRoot(), 950, 700);

        primaryStage.setScene(gameScene);
        primaryStage.centerOnScreen();

        gameView.logMessage("[INFO] L'eroe " + heroName + " è entrato nelle terre ghiacciate di Niflheim.");

        // Inizializzazione della mappa 10x10 con rune e ostacoli casuali
        GridMap map = new GridMap(10, 10);

        // Posizione iniziale dell'eroe (es. in alto a sinistra: 0, 0)
        Position heroPos = new Position(0, 0);

        // Renderizziamo la mappa e l'eroe sulla vista
        gameView.renderMap(map, heroPos);

        // Esempio di collegamento per testare il Game Over tramite il pulsante Accampati (opzionale)
        gameView.getCampButton().setOnAction(e -> showGameOverScene(heroName));
    }

    // 3. Mostra la schermata di Game Over quando l'eroe muore
    private void showGameOverScene(String heroName) {
        GameOverView gameOverView = new GameOverView(heroName);
        Scene gameOverScene = new Scene(gameOverView.getRoot(), 800, 600);

        // Pulsante "Riprova": riavvia la partita con lo stesso eroe
        gameOverView.getRestartButton().setOnAction(e -> showGameScene(heroName));

        // Pulsante "Torna al Menu": riporta l'utente alla schermata iniziale
        gameOverView.getMenuButton().setOnAction(e -> showWelcomeScene());

        primaryStage.setScene(gameOverScene);
        primaryStage.centerOnScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }
}