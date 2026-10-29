package it.unicam.cs.mpgc.rpg130663.beyondthesnow.controller;

import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.CellType;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.GridMap;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.Position;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.view.GameView;
import javafx.scene.Scene;

public class GameController {
    private final GridMap map;
    private final GameView view;
    private Position heroPos;

    // Statistiche dell'eroe
    private int hp = 100;
    private final int maxHp = 100;
    private int cold = 0;
    private int runesCollected = 0;

    public GameController(String heroName) {
        // Inizializziamo mappa 10x10 e vista
        this.map = new GridMap(10, 10);
        this.view = new GameView(heroName);

        // Posizione iniziale dell'eroe (es. in alto a sinistra a coordinate 0,0)
        this.heroPos = new Position(0, 0);

        // Primo rendering e aggiornamento statistiche
        this.view.renderMap(map, heroPos);
        this.view.updateStats(hp, maxHp, cold, runesCollected);
        this.view.logMessage("[SISTEMA] L'eroe " + heroName + " è entrato nelle terre ghiacciate di Niflheim.");
    }

    /**
     * Collega gli eventi della tastiera alla Scene principale di JavaFX
     */
    public void initControls(Scene scene) {
        scene.setOnKeyPressed(event -> {
            int newX = heroPos.getX();
            int newY = heroPos.getY();

            // Gestione dei tasti direzionali (Frecce o W, A, S, D)
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
                    return; // Ignora gli altri tasti
            }

            Position targetPos = new Position(newX, newY);

            // 1. Verifica se la posizione rientra nei confini 10x10
            if (map.isValidPosition(targetPos)) {
                CellType cellType = map.getCellType(targetPos);

                // 2. Verifica che non ci sia un ostacolo di ghiaccio
                if (cellType != CellType.OBSTACLE) {
                    heroPos = targetPos; // Aggiorna la posizione dell'eroe

                    // 3. Controlla se la cella contiene una runa
                    if (cellType == CellType.RUNE) {
                        map.setCellType(targetPos, CellType.EMPTY); // Rimuovi la runa dalla mappa
                        runesCollected++;
                        view.logMessage("[EVENTO] Hai raccolto una runa! (" + runesCollected + " / 5)");

                        if (runesCollected >= 5) {
                            view.logMessage("[VITTORIA] Hai recuperato tutte le rune di Niflheim!");
                        }
                    }

                    // 4. Ridisegna la mappa e aggiorna la barra laterale
                    view.renderMap(map, heroPos);
                    view.updateStats(hp, maxHp, cold, runesCollected);

                } else {
                    view.logMessage("[ATTENZIONE] Un blocco di ghiaccio impenetrabile sbarra la strada!");
                }
            } else {
                view.logMessage("[ATTENZIONE] Sei ai confini della mappa!");
            }
        });
    }

    public GameView getView() {
        return view;
    }
}
