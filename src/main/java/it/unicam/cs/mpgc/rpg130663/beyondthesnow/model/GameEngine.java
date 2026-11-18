package it.unicam.cs.mpgc.rpg130663.beyondthesnow.model;

import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.CellType;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.GridMap;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.Position;

/**
 * Motore di gioco (Core Model). Gestisce lo stato globale della partita, il timer di sessione,
 * la gestione degli accampamenti di emergenza e le regole di business (vittoria/sconfitta).
 */
public class GameEngine {
    private final GridMap map;
    private final Player player;
    private final long startTime;
    private int campsRemaining = 3; // Risorsa limitata di accampamenti per l'intera partita

    public GameEngine(String heroName, int width, int height) {
        this.map = new GridMap(width, height);
        this.player = new Player(heroName, 0, 0);
        this.startTime = System.currentTimeMillis();
    }

    public Player getPlayer() { return player; }
    public GridMap getMap() { return map; }
    public int getCampsRemaining() { return campsRemaining; }

    /**
     * Verifica se il giocatore può usufruire di un accampamento di emergenza
     * (controlla le scorte e che non sia già al massimo delle statistiche vitali).
     */
    public boolean canRest() {
        return campsRemaining > 0 && !(player.getHp() >= player.getMaxHp() && player.getFrostResistance() >= player.getMaxFrost());
    }

    /**
     * Esegue l'azione di riposo consumando un accampamento e ripristinando HP e resistenza al gelo.
     */
    public void rest() {
        if (!canRest()) return;
        campsRemaining--;
        player.healHp(15);
        player.restoreFrostResistance(20);
    }

    public boolean isGameOver() {
        return player.isDead();
    }

    public boolean isVictory() {
        return player.getRunesCollected() >= 5; // Obiettivo: raccogliere tutte e 5 le rune magiche
    }

    /**
     * Restituisce i secondi trascorsi dall'avvio della partita (utilizzato per il calcolo del punteggio).
     */
    public long getElapsedSeconds() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }

    /**
     * Scansiona le celle adiacenti al player per individuare eventuali ostacoli di ghiaccio
     * che possono essere distrutti tramite l'abilità 'Sciogli Ghiaccio'.
     */
    public Position findAdjacentObstacle() {
        Position heroPos = player.getPosition();
        int x = heroPos.getX();
        int y = heroPos.getY();
        Position[] adjacentPositions = {
                new Position(x, y - 1), new Position(x, y + 1),
                new Position(x - 1, y), new Position(x + 1, y)
        };
        for (Position adj : adjacentPositions) {
            if (map.isValidPosition(adj) && map.getCellType(adj) == CellType.OBSTACLE) {
                return adj;
            }
        }
        return null;
    }
}