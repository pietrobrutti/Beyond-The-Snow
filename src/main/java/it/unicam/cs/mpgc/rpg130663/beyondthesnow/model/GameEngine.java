package it.unicam.cs.mpgc.rpg130663.beyondthesnow.model;

import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.CellType;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.GridMap;
import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.Position;

public class GameEngine {
    private final GridMap map;
    private final Player player;
    private final long startTime;
    private int campsRemaining = 3;

    public GameEngine(String heroName, int width, int height) {
        this.map = new GridMap(width, height);
        this.player = new Player(heroName, 0, 0);
        this.startTime = System.currentTimeMillis();
    }

    public Player getPlayer() { return player; }
    public GridMap getMap() { return map; }
    public int getCampsRemaining() { return campsRemaining; }

    public boolean canRest() {
        return campsRemaining > 0 && !(player.getHp() >= player.getMaxHp() && player.getFrostResistance() >= player.getMaxFrost());
    }

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
        return player.getRunesCollected() >= 5;
    }

    public long getElapsedSeconds() {
        return (System.currentTimeMillis() - startTime) / 1000;
    }

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
