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
    private int campsRemaining = 3; // Massimo 3 accampamenti a partita

    public GameController(String heroName) {
        this.map = new GridMap(10, 10);
        this.player = new Player(heroName, 0, 0);
        this.view = new GameView(heroName);
        this.startTime = System.currentTimeMillis();

        updateView();
        this.view.logMessage("[SISTEMA] L'eroe " + heroName + " è entrato nelle terre ghiacciate di Niflheim.");
        this.view.logMessage("[SISTEMA] Hai a disposizione ⛺ 3 Accampamenti di emergenza per tutta la partita.");
    }

    public void initControls(Scene scene) {
        view.getCampButton().setOnAction(e -> handleRest());
        view.getMeltButton().setOnAction(e -> handleMeltIce());

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
                        player.restoreFrostResistance(15);
                        updateView();
                        return;
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
                            map.setCellType(targetPos, CellType.EMPTY);

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
                    view.logMessage("[ATTENZIONE] Un blocco di ghiaccio impenetrabile sbarra la strada! Premi 'Sciogli Ghiaccio'.");
                }
            } else {
                view.logMessage("[ATTENZIONE] Sei ai confini della mappa!");
            }
        });
    }

    private void handleRest() {
        if (player.isDead()) return;

        if (campsRemaining <= 0) {
            view.logMessage("[ACCAMPAMENTO] Hai esaurito tutti gli accampamenti disponibili per questa partita!");
            return;
        }

        if (player.getHp() >= player.getMaxHp() && player.getFrostResistance() >= player.getMaxFrost()) {
            view.logMessage("[ACCAMPAMENTO] Sei già al massimo delle tue forze, inutile sprecare un accampamento!");
            return;
        }

        campsRemaining--;
        // Valori bilanciati per una maggiore sfida survival
        player.healHp(15);
        player.restoreFrostResistance(20);
        view.logMessage("[ACCAMPAMENTO] Ti riposi al fuoco (+15 HP, +20% Gelo). Accampamenti rimasti: " + campsRemaining);

        // Aggiorna il testo del pulsante per mostrare quanti ne restano
        view.getCampButton().setText("⛺ Accampati (" + campsRemaining + "/3)");
        if (campsRemaining == 0) {
            view.getCampButton().setDisable(true);
        }

        updateView();
    }

    private Position findAdjacentObstacle() {
        Position heroPos = player.getPosition();
        int x = heroPos.getX();
        int y = heroPos.getY();

        Position[] adjacentPositions = {
                new Position(x, y - 1),
                new Position(x, y + 1),
                new Position(x - 1, y),
                new Position(x + 1, y)
        };

        for (Position adj : adjacentPositions) {
            if (map.isValidPosition(adj) && map.getCellType(adj) == CellType.OBSTACLE) {
                return adj;
            }
        }
        return null;
    }

    private void handleMeltIce() {
        if (player.isDead()) return;

        Position targetObstacle = findAdjacentObstacle();

        if (targetObstacle != null) {
            if (player.getHp() <= 15) {
                view.logMessage("[ATTENZIONE] Non hai abbastanza salute per sopportare il calore del sacrificio!");
                return;
            }

            player.takeDamage(15);
            map.setCellType(targetObstacle, CellType.EMPTY);
            view.logMessage("[MAGIA] Bruci la tua energia vitale per sciogliere il blocco di ghiaccio! (-15 HP)");

            checkGameOver();
            updateView();
        } else {
            view.logMessage("[ACCAMPAMENTO] Nessun blocco di ghiaccio adiacente da poter sciogliere!");
        }
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
        Position adjacentObstacle = findAdjacentObstacle();
        view.setHighlightedIcePos(adjacentObstacle);

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