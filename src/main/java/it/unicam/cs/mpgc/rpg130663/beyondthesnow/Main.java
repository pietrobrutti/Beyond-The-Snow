package it.unicam.cs.mpgc.rpg130663.beyondthesnow;

import it.unicam.cs.mpgc.rpg130663.beyondthesnow.view.GameView;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.view.WelcomeView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Beyond the Snow: Niflheim");

        // 1. Inizializzazione della schermata di benvenuto
        WelcomeView welcomeView = new WelcomeView();
        Scene welcomeScene = new Scene(welcomeView.getRoot(), 800, 600);

        // 2. Transizione al click del pulsante "Inizia Nuova Avventura"
        welcomeView.getStartButton().setOnAction(e -> {
            // Recupero del nome inserito dall'utente (es. "Pie")
            String heroName = welcomeView.getHeroName();

            // Istanziazione di GameView passando il nome selezionato
            GameView gameView = new GameView(heroName);
            Scene gameScene = new Scene(gameView.getRoot(), 950, 700);

            primaryStage.setScene(gameScene);
            primaryStage.centerOnScreen();

            gameView.logMessage("[INFO] L'eroe " + heroName + " è entrato nelle terre ghiacciate di Niflheim.");
        });

        // 3. Chiusura al click di "Esci"
        welcomeView.getExitButton().setOnAction(e -> primaryStage.close());

        primaryStage.setScene(welcomeScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}