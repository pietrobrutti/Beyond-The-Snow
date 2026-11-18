package it.unicam.cs.mpgc.rpg130663.beyondthesnow;

import it.unicam.cs.mpgc.rpg130663.beyondthesnow.controller.GameController;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.view.MainMenuView;
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
        showMainMenu();
    }

    // 1. Mostra il Menu Principale
    private void showMainMenu() {
        MainMenuView menuView = new MainMenuView();
        Scene menuScene = new Scene(menuView.getRoot(), 900, 700);

        menuView.getStartButton().setOnAction(e -> {
            String heroName = menuView.getNameInput().getText();

            // Validazione del nome vuoto sfruttando i metodi dedicati della View
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

    // 2. Avvia la schermata di gioco delegando la logica al GameController
    private void showGameScene(String heroName) {
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