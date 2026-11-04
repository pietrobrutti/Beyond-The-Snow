package it.unicam.cs.mpgc.rpg130663.beyondthesnow.model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ScoreManager {

    // Classe per le Partite Vinte (con tempo)
    public static class VictoryEntry {
        private final String heroName;
        private final long elapsedSeconds;

        public VictoryEntry(String heroName, long elapsedSeconds) {
            this.heroName = heroName;
            this.elapsedSeconds = elapsedSeconds;
        }

        public String getHeroName() {
            return heroName;
        }

        public long getElapsedSeconds() {
            return elapsedSeconds;
        }

        public String getFormattedTime() {
            if (elapsedSeconds < 60) {
                return elapsedSeconds + " secondi";
            } else {
                long minutes = elapsedSeconds / 60;
                long seconds = elapsedSeconds % 60;
                return minutes + "m " + seconds + "s";
            }
        }
    }

    // Classe per lo storico di tutte le altre partite / tentativi (con rune raccolte)
    public static class AttemptEntry {
        private final String heroName;
        private final int runesCollected;
        private final String status; // es. "Sconfitto", "Ritirato", ecc.

        public AttemptEntry(String heroName, int runesCollected, String status) {
            this.heroName = heroName;
            this.runesCollected = runesCollected;
            this.status = status;
        }

        public String getHeroName() {
            return heroName;
        }

        public int getRunesCollected() {
            return runesCollected;
        }

        public String getStatus() {
            return status;
        }
    }

    private static final List<VictoryEntry> victories = new ArrayList<>();
    private static final List<AttemptEntry> attempts = new ArrayList<>();

    private static final String VICTORY_FILE = "leaderboard_victories.txt";
    private static final String ATTEMPT_FILE = "leaderboard_attempts.txt";

    static {
        loadVictories();
        loadAttempts();
    }

    // Aggiunge una vittoria
    public static void addVictory(String heroName, long elapsedSeconds) {
        victories.add(new VictoryEntry(heroName, elapsedSeconds));
        victories.sort(Comparator.comparingLong(VictoryEntry::getElapsedSeconds));

        if (victories.size() > 5) {
            victories.remove(victories.size() - 1); // Mantieni i top 5
        }
        saveVictories();
    }

    // Aggiunge un tentativo/partita conclusa
    public static void addAttempt(String heroName, int runesCollected, String status) {
        attempts.add(0, new AttemptEntry(heroName, runesCollected, status)); // Inserisce in cima (più recente)

        if (attempts.size() > 10) {
            attempts.remove(attempts.size() - 1); // Mantieni gli ultimi 10 tentativi
        }
        saveAttempts();
    }

    public static List<VictoryEntry> getVictories() {
        return Collections.unmodifiableList(victories);
    }

    public static List<AttemptEntry> getAttempts() {
        return Collections.unmodifiableList(attempts);
    }

    // --- GESTIONE I/O FILE VITTORIE ---
    private static void saveVictories() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(VICTORY_FILE))) {
            for (VictoryEntry entry : victories) {
                writer.println(entry.getHeroName() + "," + entry.getElapsedSeconds());
            }
        } catch (IOException e) {
            System.err.println("Errore salvataggio vittorie: " + e.getMessage());
        }
    }

    private static void loadVictories() {
        Path path = Paths.get(VICTORY_FILE);
        if (!Files.exists(path)) return;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            victories.clear();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    victories.add(new VictoryEntry(parts[0], Long.parseLong(parts[1])));
                }
            }
            victories.sort(Comparator.comparingLong(VictoryEntry::getElapsedSeconds));
        } catch (IOException | NumberFormatException e) {
            System.err.println("Errore caricamento vittorie: " + e.getMessage());
        }
    }

    // --- GESTIONE I/O FILE TENTATIVI ---
    private static void saveAttempts() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ATTEMPT_FILE))) {
            for (AttemptEntry entry : attempts) {
                writer.println(entry.getHeroName() + "," + entry.getRunesCollected() + "," + entry.getStatus());
            }
        } catch (IOException e) {
            System.err.println("Errore salvataggio tentativi: " + e.getMessage());
        }
    }

    private static void loadAttempts() {
        Path path = Paths.get(ATTEMPT_FILE);
        if (!Files.exists(path)) return;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            attempts.clear();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    attempts.add(new AttemptEntry(parts[0], Integer.parseInt(parts[1]), parts[2]));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Errore caricamento tentativi: " + e.getMessage());
        }
    }
}