package it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GridMap {
    private final int width;
    private final int height;
    private final Map<Position, CellType> gridContents;

    public GridMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.gridContents = new HashMap<>();
        generateDefaultMap();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public CellType getCellType(Position position) {
        return gridContents.getOrDefault(position, CellType.EMPTY);
    }

    public void setCellType(Position position, CellType type) {
        if (isValidPosition(position)) {
            gridContents.put(position, type);
        }
    }

    public boolean isValidPosition(Position pos) {
        return pos.getX() >= 0 && pos.getX() < width &&
                pos.getY() >= 0 && pos.getY() < height;
    }

    private void generateDefaultMap() {
        gridContents.clear();
        List<Position> availablePositions = new ArrayList<>();

        // Raccogliamo tutte le posizioni rigorosamente entro i confini, escludendo lo spawn (0,0)
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Position pos = new Position(x, y);
                if (!(x == 0 && y == 0)) {
                    availablePositions.add(pos);
                }
            }
        }

        Collections.shuffle(availablePositions, new Random());

        int index = 0;

        // Posizioniamo esattamente 5 Rune
        for (int i = 0; i < 5; i++) {
            if (index < availablePositions.size()) {
                setCellType(availablePositions.get(index++), CellType.RUNE);
            }
        }

        // Posizioniamo esattamente 32 Ostacoli
        for (int i = 0; i < 32; i++) {
            if (index < availablePositions.size()) {
                setCellType(availablePositions.get(index++), CellType.OBSTACLE);
            }
        }
    }
}