package it.unicam.cs.mpgc.rpg130663.beyondthesnow.controller;

import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.GameEngine;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.ScoreManager;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.CellType;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.Position;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.view.GameView;
import javafx.scene.Scene;

public class GameController {
    private final GameEngine engine;
    private final GameView view;

    public GameController(String heroName) {
        this.engine = new GameEngine(heroName, 10, 10);
        this.view = new GameView(heroName);

        updateView();
        this.view.logMessage("[SISTEMA] L'eroe " + heroName + " è entrato nelle terre ghiacciate di Niflheim.");
        this.view.logMessage("[SISTEMA] Hai a disposizione ⛺ 3 Accampamenti di emergenza per tutta la partita.");
    }

    public void initControls(Scene scene) {
        view.getCampButton().setOnAction(e -> handleRest());
        view.getMeltButton().setOnAction(e -> handleMeltIce());

        scene.setOnKeyPressed(event -> {
            if (engine.isGameOver()) return;

            Position currentPos = engine.getPlayer().getPosition();
            int newX = currentPos.getX();
            int newY = currentPos.getY();

            switch (event.getCode()) {
                case UP, W -> newY--;
                case DOWN, S -> newY++;
                case LEFT, A -> newX--;
                case RIGHT, D -> newX++;
                default -> { return; }
            }

            Position targetPos = new Position(newX, newY);

            if (engine.getMap().isValidPosition(targetPos)) {
                CellType cellType = engine.getMap().getCellType(targetPos);

                if (cellType != CellType.OBSTACLE) {
                    engine.getPlayer().setPosition(targetPos);
                    engine.getPlayer().reduceFrostResistance(2);

                    if (engine.getPlayer().getFrostResistance() < 20) {
                        view.logMessage("[PERICOLO] Il gelo è troppo intenso! Sei costretto a una ritirata d'emergenza!");
                        engine.getPlayer().restoreFrostResistance(15);
                        view.triggerScreenShake();
                        updateView();
                        return;
                    }

                    switch (cellType) {
                        case RUNE -> {
                            engine.getPlayer().collectRune();
                            engine.getMap().setCellType(targetPos, CellType.EMPTY);
                            view.logMessage("[RUNA] Hai raccolto una Runa Magica! (" + engine.getPlayer().getRunesCollected() + " / 5)");

                            if (engine.isVictory()) {
                                handleVictory();
                            }
                        }
                        case ENEMY -> {
                            view.logMessage("[ATTACCO] Imboscata! Una creatura dei ghiacci ti assale!");
                            engine.getMap().setCellType(targetPos, CellType.EMPTY);
                            view.triggerScreenShake();

                            view.showClassicPokemonBattleOverlay(
                                    engine.getPlayer().getName(),
                                    engine.getPlayer().getHp(),
                                    engine.getPlayer().getMaxHp(),
                                    engine.getPlayer().getFrostResistance(),
                                    (coldDamage, hpDamage) -> {
                                        engine.getPlayer().reduceFrostResistance(coldDamage);
                                        engine.getPlayer().takeDamage(hpDamage);
                                        view.logMessage("[COMBATTIMENTO] Subisci colpi: -" + coldDamage + "% Gelo, -" + hpDamage + " HP.");
                                        view.triggerScreenShake();
                                        checkGameOver();
                                        updateView();
                                    },
                                    () -> {
                                        view.logMessage("[COMBATTIMENTO] Hai sconfitto la creatura!");
                                        checkGameOver();
                                        updateView();
                                    },
                                    () -> {
                                        engine.getPlayer().reduceFrostResistance(15);
                                        view.logMessage("[FUGA] Fuga rocambolesca, perdi ulteriore resistenza al gelo.");
                                        checkGameOver();
                                        updateView();
                                    }
                            );
                        }
                        default -> view.logMessage("[ESPLORAZIONE] Avanzi tra la neve e il gelo...");
                    }

                    checkGameOver();
                    updateView();

                } else {
                    view.logMessage("[ATTENZIONE] Un blocco di ghiaccio impenetrabile sbarra la strada! Premi 'Sciogli Ghiaccio'.");
                }
            } else {
                view.logMessage("[ATTENZIONE] Sei ai confini della mappa!");
            }
        });
    }

    private void handleRest() {
        if (engine.isGameOver()) return;

        if (engine.getCampsRemaining() <= 0) {
            view.logMessage("[ACCAMPAMENTO] Hai esaurito tutti gli accampamenti disponibili per questa partita!");
            return;
        }

        if (!engine.canRest()) {
            view.logMessage("[ACCAMPAMENTO] Sei già al massimo delle tue forze, inutile sprecare un accampamento!");
            return;
        }

        engine.rest();
        view.logMessage("[ACCAMPAMENTO] Ti riposi al fuoco (+15 HP, +20% Gelo). Accampamenti rimasti: " + engine.getCampsRemaining());

        view.getCampButton().setText("⛺ Accampati (" + engine.getCampsRemaining() + "/3)");
        if (engine.getCampsRemaining() == 0) {
            view.getCampButton().setDisable(true);
        }

        updateView();
    }

    private void handleMeltIce() {
        if (engine.isGameOver()) return;

        Position targetObstacle = engine.findAdjacentObstacle();

        if (targetObstacle != null) {
            if (engine.getPlayer().getHp() <= 15) {
                view.logMessage("[ATTENZIONE] Non hai abbastanza salute per sopportare il calore del sacrificio!");
                return;
            }

            engine.getPlayer().takeDamage(15);
            engine.getMap().setCellType(targetObstacle, CellType.EMPTY);
            view.logMessage("[MAGIA] Bruci la tua energia vitale per sciogliere il blocco di ghiaccio! (-15 HP)");

            view.triggerScreenShake();
            checkGameOver();
            updateView();
        } else {
            view.logMessage("[ACCAMPAMENTO] Nessun blocco di ghiaccio adiacente da poter sciogliere!");
        }
    }

    private void checkGameOver() {
        if (engine.isGameOver()) {
            view.logMessage("[GAME OVER] L'eroe è caduto tra i ghiacci di Niflheim...");
            ScoreManager.addAttempt(engine.getPlayer().getName(), engine.getPlayer().getRunesCollected(), "Sconfitto");

            view.showGameOverOverlay(
                    () -> view.logMessage("[SISTEMA] Riavvio non ancora implementato."),
                    () -> view.logMessage("[SISTEMA] Ritorno al menu non ancora implementato.")
            );
        }
    }

    private void handleVictory() {
        long elapsedSeconds = engine.getElapsedSeconds();
        ScoreManager.addVictory(engine.getPlayer().getName(), elapsedSeconds);
        view.logMessage("[VITTORIA] Hai recuperato tutte e 5 le rune di Niflheim!");

        view.showVictoryOverlay(
                elapsedSeconds,
                () -> view.logMessage("[SISTEMA] Riavvio non ancora implementato."),
                () -> view.logMessage("[SISTEMA] Ritorno al menu non ancora implementato.")
        );
    }

    private void updateView() {
        Position adjacentObstacle = engine.findAdjacentObstacle();
        view.setHighlightedIcePos(adjacentObstacle);

        view.renderMap(engine.getMap(), engine.getPlayer().getPosition());
        view.updateStats(
                engine.getPlayer().getHp(),
                engine.getPlayer().getMaxHp(),
                engine.getPlayer().getFrostResistance(),
                engine.getPlayer().getMaxFrost(),
                engine.getPlayer().getRunesCollected()
        );
    }

    public GameView getView() {
        return view;
    }
}