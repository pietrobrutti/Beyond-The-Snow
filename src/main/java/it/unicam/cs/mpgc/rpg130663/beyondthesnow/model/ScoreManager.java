package it.unicam.cs.mpgc.rpg130663.beyondthesnow.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ScoreManager {

    // Classe interna per rappresentare un singolo record
    public static class ScoreEntry {
        private final String heroName;
        private final long elapsedSeconds;

        public ScoreEntry(String heroName, long elapsedSeconds) {
            this.heroName = heroName;
            this.elapsedSeconds = elapsedSeconds;
        }

        public String getHeroName() {
            return heroName;
        }

        public long getElapsedSeconds() {
            return elapsedSeconds;
        }

        // Formatta il tempo in minuti e secondi (es. "1m 12s" o "45s")
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

    private static final List<ScoreEntry> scores = new ArrayList<>();

    // Aggiunge un nuovo punteggio e riordina la classifica (dal tempo minore al maggiore)
    public static void addScore(String heroName, long elapsedSeconds) {
        scores.add(new ScoreEntry(heroName, elapsedSeconds));
        scores.sort(Comparator.comparingLong(ScoreEntry::getElapsedSeconds));

        // Mantiene solo i migliori 5 punteggi per pulizia
        if (scores.size() > 5) {
            scores.remove(scores.size() - 1);
        }
    }

    // Restituisce la lista dei punteggi attuali
    public static List<ScoreEntry> getScores() {
        return Collections.unmodifiableList(scores);
    }
}