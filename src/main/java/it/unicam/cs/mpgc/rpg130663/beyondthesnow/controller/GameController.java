package it.unicam.cs.mpgc.rpg130663.beyondthesnow.controller;

import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.Player;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.ScoreManager;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.CellType;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.GridMap;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.Position;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.view.GameView;
import javafx.scene.Scene;

public class GameController {
    private final GridMap map;
    private final GameView view;
    private final Player player;
    private final long startTime;

    public GameController(String heroName) {
        this.map = new GridMap(10, 10);
        this.player = new Player(heroName, 0, 0);
        this.view = new GameView(heroName);
        this.startTime = System.currentTimeMillis();

        updateView();
        this.view.logMessage("[SISTEMA] L'eroe " + heroName + " è entrato nelle terre ghiacciate di Niflheim.");
    }

    public void initControls(Scene scene) {
        view.getCampButton().setOnAction(e -> handleRest());

        scene.setOnKeyPressed(event -> {
            if (player.isDead()) return;

            Position currentPos = player.getPosition();
            int newX = currentPos.getX();
            int newY = currentPos.getY();

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
                    player.setPosition(targetPos);
                    player.reduceFrostResistance(2);

                    // --- CONTROLLO DEL GELO CRITICO (Sotto il 20%) ---
                    if (player.getFrostResistance() < 20) {
                        view.logMessage("[PERICOLO] Il gelo è troppo intenso! Sei costretto a una ritirata d'emergenza!");
                        player.restoreFrostResistance(15); // Si riprende leggermente fuggendo
                        updateView();
                        return; // Interrompe l'azione impedendo di avanzare o ingaggiare battaglie se si è congelati
                    }

                    switch (cellType) {
                        case RUNE:
                            player.collectRune();
                            map.setCellType(targetPos, CellType.EMPTY);
                            view.logMessage("[RUNA] Hai raccolto una Runa Magica! (" + player.getRunesCollected() + " / 5)");

                            if (player.getRunesCollected() >= 5) {
                                handleVictory();
                            }
                            break;

                        case ENEMY:
                            view.logMessage("[ATTACCO] Imboscata! Una creatura dei ghiacci ti assale!");
                            map.setCellType(targetPos, CellType.EMPTY); // Libera la cella sulla mappa

                            view.showClassicPokemonBattleOverlay(
                                    player.getName(),
                                    player.getHp(),
                                    player.getMaxHp(),
                                    player.getFrostResistance(),
                                    (coldDamage, hpDamage) -> {
                                        player.reduceFrostResistance(coldDamage);
                                        player.takeDamage(hpDamage);
                                        view.logMessage("[COMBATTIMENTO] Subisci colpi: -" + coldDamage + "% Gelo, -" + hpDamage + " HP.");
                                        checkGameOver();
                                        updateView();
                                    },
                                    () -> {
                                        view.logMessage("[COMBATTIMENTO] Hai sconfitto la creatura!");
                                        checkGameOver();
                                        updateView();
                                    },
                                    () -> {
                                        player.reduceFrostResistance(15);
                                        view.logMessage("[FUGA] Fuga rocambolesca, perdi ulteriore resistenza al gelo.");
                                        checkGameOver();
                                        updateView();
                                    }
                            );
                            break;

                        case EMPTY:
                        default:
                            view.logMessage("[ESPLORAZIONE] Avanzi tra la neve e il gelo...");
                            break;
                    }

                    checkGameOver();
                    updateView();

                } else {
                    view.logMessage("[ATTENZIONE] Un blocco di ghiaccio impenetrabile sbarra la strada!");
                }
            } else {
                view.logMessage("[ATTENZIONE] Sei ai confini della mappa!");
            }
        });
    }

    private void handleRest() {
        if (player.isDead()) return;

        if (player.getHp() >= player.getMaxHp() && player.getFrostResistance() >= player.getMaxFrost()) {
            view.logMessage("[ACCAMPAMENTO] Sei già al massimo delle tue forze!");
            return;
        }

        player.healHp(30);
        player.restoreFrostResistance(35);
        view.logMessage("[ACCAMPAMENTO] Ti riposi al fuoco dell'accampamento (+30 HP, +35% Resistenza al Gelo).");
        updateView();
    }

    private void checkGameOver() {
        if (player.isDead()) {
            view.logMessage("[GAME OVER] L'eroe è caduto tra i ghiacci di Niflheim...");
            ScoreManager.addAttempt(player.getName(), player.getRunesCollected(), "Sconfitto");

            view.showGameOverOverlay(
                    () -> view.logMessage("[SISTEMA] Riavvio non ancora implementato."),
                    () -> view.logMessage("[SISTEMA] Ritorno al menu non ancora implementato.")
            );
        }
    }

    private void handleVictory() {
        long elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000;
        ScoreManager.addVictory(player.getName(), elapsedSeconds);
        view.logMessage("[VITTORIA] Hai recuperato tutte e 5 le rune di Niflheim!");

        view.showVictoryOverlay(
                elapsedSeconds,
                () -> view.logMessage("[SISTEMA] Riavvio non ancora implementato."),
                () -> view.logMessage("[SISTEMA] Ritorno al menu non ancora implementato.")
        );
    }

    private void updateView() {
        view.renderMap(map, player.getPosition());
        view.updateStats(
                player.getHp(),
                player.getMaxHp(),
                player.getFrostResistance(),
                player.getMaxFrost(),
                player.getRunesCollected()
        );
    }

    public GameView getView() {
        return view;
    }
}