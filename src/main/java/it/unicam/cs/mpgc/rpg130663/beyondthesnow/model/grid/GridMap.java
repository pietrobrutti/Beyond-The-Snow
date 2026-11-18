package it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Gestisce la griglia di gioco, la struttura delle celle e la generazione procedurale
 * di ostacoli, rune ed nemici all'interno delle terre di Niflheim.
 */
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

    /**
     * Genera in modo procedurale la mappa distribuendo ostacoli, rune e nemici
     * e garantendo che le posizioni chiave siano sempre raggiungibili.
     */
    private void generateMap() {
        Random random = new Random();

        // 1. Inizializza tutte le celle della griglia come vuote (sentieri di neve)
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells.put(new Position(x, y), CellType.EMPTY);
            }
        }

        // Zona di spawn iniziale dell'eroe (0,0) rigorosamente lasciata libera
        cells.put(new Position(0, 0), CellType.EMPTY);

        // 2. Posizionamento degli Ostacoli di ghiaccio (bloccanti)
        int obstaclesPlaced = 0;
        while (obstaclesPlaced < 32) {
            Position pos = new Position(random.nextInt(width), random.nextInt(height));
            boolean isNearStart = (pos.getX() <= 1 && pos.getY() <= 1);

            // Evito di bloccare lo start e posiziono solo su celle vuote
            if (!isNearStart && cells.get(pos) == CellType.EMPTY) {
                cells.put(pos, CellType.OBSTACLE);
                obstaclesPlaced++;
            }
        }

        // 3. Posizionamento delle 5 Rune Magiche (obiettivi di gioco raggiungibili)
        int runesPlaced = 0;
        while (runesPlaced < 5) {
            Position pos = new Position(random.nextInt(width), random.nextInt(height));
            boolean isNearStart = (pos.getX() <= 1 && pos.getY() <= 1);

            // Controllo che la cella sia libera e che abbia almeno un vicino accessibile
            if (!isNearStart && cells.get(pos) == CellType.EMPTY) {
                if (hasAtLeastOneFreeNeighbor(pos)) {
                    cells.put(pos, CellType.RUNE);
                    runesPlaced++;
                }
            }
        }

        // 4. Posizionamento dei Nemici nascosti (pericoli interattivi)
        int enemiesPlaced = 0;
        while (enemiesPlaced < 10) {
            Position pos = new Position(random.nextInt(width), random.nextInt(height));
            boolean isNearStart = (pos.getX() <= 1 && pos.getY() <= 1);

            if (!isNearStart && cells.get(pos) == CellType.EMPTY) {
                if (hasAtLeastOneFreeNeighbor(pos)) {
                    cells.put(pos, CellType.ENEMY);
                    enemiesPlaced++;
                }
            }
        }
    }

    /**
     * Metodo di supporto per verificare che una posizione non sia completamente intrappolata
     * o circondata da ostacoli insormontabili, garantendo la giocabilità.
     */
    private boolean hasAtLeastOneFreeNeighbor(Position pos) {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int freeNeighbors = 0;

        for (int[] dir : directions) {
            Position neighbor = new Position(pos.getX() + dir[0], pos.getY() + dir[1]);
            if (isValidPosition(neighbor)) {
                CellType type = cells.get(neighbor);
                if (type == CellType.EMPTY || type == CellType.RUNE || type == CellType.ENEMY) {
                    freeNeighbors++;
                }
            }
        }
        return freeNeighbors > 0;
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