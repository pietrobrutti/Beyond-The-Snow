package it.unicam.cs.mpgc.rpg130663.beyondthesnow.model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Gestisce la persistenza locale, il caricamento e il salvataggio dei punteggi,
 * della classifica delle vittorie (Top 5) e dello storico dei tentativi recenti.
 */
public class ScoreManager {

    /**
     * Rappresenta una singola voce all'interno della classifica delle partite vinte.
     */
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

        /**
         * Restituisce il tempo formattato in formato leggibile (secondi o minuti e secondi).
         */
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

    /**
     * Rappresenta una voce dello storico dei tentativi di gioco conclusi (es. sconfitte o ritirate).
     */
    public static class AttemptEntry {
        private final String heroName;
        private final int runesCollected;
        private final String status; // Es. "Sconfitto"

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

    // Blocco di inizializzazione statica: carica i dati da file all'avvio dell'applicazione
    static {
        loadVictories();
        loadAttempts();
    }

    /**
     * Aggiunge una nuova vittoria alla classifica, ordinandola per tempo di completamento
     * e mantenendo solo le migliori 5 posizioni.
     */
    public static void addVictory(String heroName, long elapsedSeconds) {
        victories.add(new VictoryEntry(heroName, elapsedSeconds));
        victories.sort(Comparator.comparingLong(VictoryEntry::getElapsedSeconds));

        if (victories.size() > 5) {
            victories.remove(victories.size() - 1); // Mantiene esclusivamente la Top 5
        }
        saveVictories();
    }

    /**
     * Registra un nuovo tentativo concluso, inserendolo in cima alla lista (più recente)
     * e mantenendo un limite massimo storico di 10 elementi.
     */
    public static void addAttempt(String heroName, int runesCollected, String status) {
        attempts.add(0, new AttemptEntry(heroName, runesCollected, status));

        if (attempts.size() > 10) {
            attempts.remove(attempts.size() - 1); // Mantiene gli ultimi 10 tentativi
        }
        saveAttempts();
    }

    /**
     * Restituisce una vista non modificabile (read-only) della classifica delle vittorie.
     */
    public static List<VictoryEntry> getVictories() {
        return Collections.unmodifiableList(victories);
    }

    /**
     * Restituisce una vista non modificabile (read-only) dello storico dei tentativi.
     */
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
                if(parts.length == 3) {
                    attempts.add(new AttemptEntry(parts[0], Integer.parseInt(parts[1]), parts[2]));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Errore caricamento tentativi: " + e.getMessage());
        }
    }
}