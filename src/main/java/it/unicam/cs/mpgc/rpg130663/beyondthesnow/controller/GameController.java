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
                            view.logMessage("[DUELLO] Un Guardiano dei Ghiacci ti sbarra la strada!");
                            view.showDuelOverlay(
                                    () -> resolveAttack(targetPos),
                                    () -> resolveDefense(targetPos),
                                    () -> resolveFlee(targetPos)
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

    private void resolveAttack(Position targetPos) {
        player.takeDamage(15);
        map.setCellType(targetPos, CellType.EMPTY);
        view.logMessage("[COMBATTIMENTO] Sconfiggi il mostro con un Attacco Furente (-15 HP). Via libera!");
        checkGameOver();
        updateView();
    }

    private void resolveDefense(Position targetPos) {
        player.reduceFrostResistance(15);
        map.setCellType(targetPos, CellType.EMPTY);
        view.logMessage("[DUELLO] Resisti dietro le tue difese, ma il gelo ti consuma (-15 Gelo).");
        checkGameOver();
        updateView();
    }

    private void resolveFlee(Position targetPos) {
        player.takeDamage(5);
        player.reduceFrostResistance(5);
        view.logMessage("[FUGA] Eviti lo scontro diretto subendo lievi danni (-5 HP, -5 Gelo).");
        checkGameOver();
        updateView();
    }

    private void handleRest() {
        if (player.isDead()) return;

        if (player.getHp() >= player.getMaxHp() && player.getFrostResistance() >= player.getMaxFrost()) {
            view.logMessage("[ACCAMPAMENTO] Sei già al massimo delle tue forze!");
            return;
        }

        player.healHp(25);
        player.restoreFrostResistance(30);
        view.logMessage("[ACCAMPAMENTO] Ti riposi al fuoco dell'accampamento (+25 HP, +30 Gelo).");
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
        // Aggiornato con tutti e 5 i parametri richiesti dalla GameView
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