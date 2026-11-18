package it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid;

import java.util.Objects;

/**
 * Rappresenta una coordinata bidimensionale (x, y) immutabile all'interno della griglia di gioco.
 * Implementa correttamente equals e hashCode per consentire l'utilizzo come chiave nelle collezioni (HashMap).
 */
public class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}