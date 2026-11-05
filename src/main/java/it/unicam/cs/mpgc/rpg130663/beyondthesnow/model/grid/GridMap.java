package it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GridMap {
    private final int width;
    private final int height;
    private final Map<Position, CellType> cells;

    public GridMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new HashMap<>();
        generateMap();
    }

    private void generateMap() {
        Random random = new Random();

        // 1. Inizializza tutte le celle come vuote
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells.put(new Position(x, y), CellType.EMPTY);
            }
        }

        // Posizione iniziale dell'eroe (0,0) lasciata libera
        cells.put(new Position(0, 0), CellType.EMPTY);

        // 2. Posiziona le 5 Rune Magiche
        int runesPlaced = 0;
        while (runesPlaced < 5) {
            Position pos = new Position(random.nextInt(width), random.nextInt(height));
            if ((pos.getX() != 0 || pos.getY() != 0) && cells.get(pos) == CellType.EMPTY) {
                cells.put(pos, CellType.RUNE);
                runesPlaced++;
            }
        }

        // 3. Posiziona ben 20 Ostacoli di ghiaccio (Ghiaccioli abbondanti sulla mappa)
        int obstaclesPlaced = 0;
        while (obstaclesPlaced < 27) {
            Position pos = new Position(random.nextInt(width), random.nextInt(height));
            if ((pos.getX() != 0 || pos.getY() != 0) && cells.get(pos) == CellType.EMPTY) {
                cells.put(pos, CellType.OBSTACLE);
                obstaclesPlaced++;
            }
        }

        // 4. Posiziona gli 8 Nemici nascosti (senza sovrascrivere ostacoli o rune)
        int enemiesPlaced = 0;
        while (enemiesPlaced < 10) {
            Position pos = new Position(random.nextInt(width), random.nextInt(height));
            if ((pos.getX() != 0 || pos.getY() != 0) && cells.get(pos) == CellType.EMPTY) {
                cells.put(pos, CellType.ENEMY);
                enemiesPlaced++;
            }
        }
    }

    public CellType getCellType(Position position) {
        return cells.getOrDefault(position, CellType.EMPTY);
    }

    public void setCellType(Position position, CellType type) {
        if (isValidPosition(position)) {
            cells.put(position, type);
        }
    }

    public boolean isValidPosition(Position position) {
        return position.getX() >= 0 && position.getX() < width &&
                position.getY() >= 0 && position.getY() < height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}