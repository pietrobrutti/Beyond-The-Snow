package it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid;

public class GridMap {
    private final int width;
    private final int height;

    public GridMap(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
