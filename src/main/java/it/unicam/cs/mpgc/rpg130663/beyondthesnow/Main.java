package it.unicam.cs.mpgc.rpg130663.beyondthesnow;

import it.unicam.cs.mpgc.rpg130663.beyondthesnow.controller.GameController;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.ScoreManager;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.view.MainMenuView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe di avvio principale dell'applicazione (Application entry point di JavaFX).
 * Gestisce il ciclo di vita dello stage primario e la transizione tra le scene (Menu e Partita).
 */
public class Main extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        this.primaryStage.setTitle("Beyond the Snow: Niflheim");
        showMainMenu();
    }

    /**
     * 1. Mostra il Menu Principale disaccoppiato dal modello tramite Supplier (Principio SOLID - DIP).
     * Converte i dati grezzi dello ScoreManager nei record leggeri attesi dalla vista.
     */
    private void showMainMenu() {
        MainMenuView menuView = new MainMenuView(
                () -> ScoreManager.getVictories().stream()
                        .map(v -> new MainMenuView.VictoryRecord(v.getHeroName(), v.getFormattedTime()))
                        .toList(),
                () -> ScoreManager.getAttempts().stream()
                        .map(a -> new MainMenuView.AttemptRecord(a.getHeroName(), a.getRunesCollected(), a.getStatus()))
                        .toList()
        );

        Scene menuScene = new Scene(menuView.getRoot(), 900, 700);

        menuView.getStartButton().setOnAction(e -> {
            String heroName = menuView.getNameInput().getText();

            // Validazione del nome vuoto gestita direttamente tramite i metodi della View
            if (heroName == null || heroName.trim().isEmpty()) {
                menuView.showNameError("DEVI INSERIRE PER FORZA UN NOME!");
            } else {
                menuView.hideNameError();
                showGameScene(heroName.trim());
            }
        });

        menuView.getExitButton().setOnAction(e -> primaryStage.close());

        primaryStage.setScene(menuScene);
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    /**
     * 2. Avvia la schermata di gioco istanziando il GameController e associandovi i callback di navigazione.
     */
    private void showGameScene(String heroName) {
        GameController gameController = new GameController(heroName);

        // FONDAMENTALE: Collega l'azione di ritorno al menu principale tramite riferimento a metodo (DIP)
        gameController.setOnReturnToMenu(this::showMainMenu);

        Scene gameScene = new Scene(gameController.getView().getRoot(), 950, 700);

        primaryStage.setScene(gameScene);
        primaryStage.centerOnScreen();

        // Inizializza i controlli da tastiera e i listener dei pulsanti tramite il controller
        gameController.initControls(gameScene);

        // Richiede il focus asincrono per permettere l'ascolto immediato dei tasti direzionali
        Platform.runLater(() -> gameController.getView().getRoot().requestFocus());
        gameController.getView().getRoot().setOnMouseClicked(e -> gameController.getView().getRoot().requestFocus());
    }

    public static void main(String[] args) {
        launch(args);
    }
}