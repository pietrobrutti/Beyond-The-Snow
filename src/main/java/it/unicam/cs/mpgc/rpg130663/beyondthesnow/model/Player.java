package it.unicam.cs.mpgc.rpg130663.beyondthesnow.model;

import it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid.Position;

public class Player {
    private final String name;
    private Position position;

    private int hp;
    private final int maxHp;

    private int frostResistance;
    private final int maxFrost;

    private int runesCollected;

    public Player(String name, int startX, int startY) {
        this.name = name;
        this.position = new Position(startX, startY);

        this.maxHp = 100;
        this.hp = 100;

        this.maxFrost = 100;
        this.frostResistance = 100;

        this.runesCollected = 0;
    }

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setPosition(int x, int y) {
        this.position = new Position(x, y);
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void takeDamage(int amount) {
        this.hp = Math.max(0, this.hp - amount);
    }

    public void healHp(int amount) {
        this.hp = Math.min(maxHp, this.hp + amount);
    }

    public int getFrostResistance() {
        return frostResistance;
    }

    public int getMaxFrost() {
        return maxFrost;
    }

    public void reduceFrostResistance(int amount) {
        this.frostResistance = Math.max(0, this.frostResistance - amount);
        // Se la resistenza al gelo si azzera, l'assolo del freddo infligge danni diretti alla salute
        if (this.frostResistance == 0) {
            takeDamage(5);
        }
    }

    public void restoreFrostResistance(int amount) {
        this.frostResistance = Math.min(maxFrost, this.frostResistance + amount);
    }

    public int getRunesCollected() {
        return runesCollected;
    }

    public void collectRune() {
        this.runesCollected++;
    }

    public boolean isDead() {
        return hp <= 0;
    }
}
