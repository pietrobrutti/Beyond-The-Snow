package it.unicam.cs.mpgc.rpg130663.beyondthesnow.model.grid;

/**
 * Enumerazione che definisce i possibili tipi di elementi presenti all'interno
 * delle celle della griglia di gioco di Niflheim.
 */
public enum CellType {
    EMPTY,      // Cella vuota / sentiero di neve calpestabile
    RUNE,       // Runa magica da raccogliere (obiettivo principale della missione)
    ENEMY,      // Mostro o pericolo dei ghiacci (attiva l'incontro di combattimento)
    OBSTACLE    // Blocco di ghiaccio insormontabile (richiede l'abilità 'Sciogli Ghiaccio')
}