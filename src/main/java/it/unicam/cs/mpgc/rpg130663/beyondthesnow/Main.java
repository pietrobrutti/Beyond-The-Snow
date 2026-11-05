package it.unicam.cs.mpgc.rpg130663.beyondthesnow;

import it.unicam.cs.mpgc.rpg130663.beyondthesnow.controller.GameController;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.view.MainMenuView;
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

    // 2. Avvia la schermata di gioco delegando la logica al GameController
    private void showGameScene(String heroName) {
        // Inizializziamo il controller che gestisce la vista, la mappa e le statistiche dell'eroe
        GameController gameController = new GameController(heroName);
        Scene gameScene = new Scene(gameController.getView().getRoot(), 950, 700);

        primaryStage.setScene(gameScene);
        primaryStage.centerOnScreen();

        // Inizializziamo i controlli da tastiera e i pulsanti tramite il controller
        gameController.initControls(gameScene);

        // Richiedi il focus per permettere l'ascolto dei tasti direzionali
        Platform.runLater(() -> gameController.getView().getRoot().requestFocus());
        gameController.getView().getRoot().setOnMouseClicked(e -> gameController.getView().getRoot().requestFocus());
    }

    public static void main(String[] args) {
        launch(args);
    }
}