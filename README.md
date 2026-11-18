# 📌 Beyond the Snow: Niflheim

**Beyond the Snow: Niflheim** è un videogioco RPG tattico 2D a griglia sviluppato in **Java** e **JavaFX** con ambientazione fantasy nordica. Il giocatore interpreta un eroe che deve esplorare le lande ghiacciate di Niflheim, sopravvivere al gelo estremo e affrontare creature mitologiche per recuperare le 5 Rune Magiche e spezzare il maleficio di Thrym, il Re dei Ghiacci.

Il progetto è stato realizzato applicando rigorosamente i principi della programmazione orientata agli oggetti (**OOP**), i 5 principi architetturali **SOLID**, il pattern **MVC (Model-View-Controller)** e la persistenza dei dati su file.

- **Autore:** Pietro Brutti 
- **Matricola:** 130663 
- **Esame:** Progetto sviluppato per l'esame di *Programmazione Avanzata / Modellazione e Gestione della Conoscenza*
- **Anno Accademico:** 2025/2026 - Università di Camerino (UNICAM)

---

## 🎮 Caratteristiche principali

-  **Mappa procedurale a griglia:** generazione dinamica del territorio con posizionamento casuale di ostacoli, accampamenti, creature ostili e delle 5 Rune Magiche.
-  **Gestione della sopravvivenza (HP e Gelo):** sistema a doppia statistica in cui il giocatore deve monitorare sia i Punti Vita che la Resistenza al Freddo, sfruttando gli accampamenti e la magia del fuoco per non congelare.
-  **Sistema di combattimento strategico a turni:** schermata dedicata (`BattleView`) con gestione degli attacchi, abilità difensive/magiche e animazioni visive.
-  **Guida e regole integrate:** schermata interattiva accessibile direttamente dal menu principale per consultare meccaniche, comandi e obiettivi di gioco.
-  **Classifica e persistenza dei record:** salvataggio automatico su file delle partite completate (`ScoreManager`), con distinzione tra **Vittorie** (ordinate per tempo impiegato) e **Tentativi** (con conteggio delle rune raccolte).
-  **Interfaccia grafica tematizzata:** styling personalizzato tramite CSS JavaFX con estetica nordica/glaciale, transizioni fluide e asset grafici dedicati.
-  **Controlli intuitivi:** movimento fluido dell'eroe sulla griglia tramite tastiera (Frecce direzionali / WASD) e pulsanti dedicati nell'HUD.

---

## 🛠️ Tecnologie utilizzate

- **Java 25 (LTS)**
- **JavaFX** (UI, Layouts, CSS inline, Transitions)
- **Gradle** (Gestione delle dipendenze e build)

---

## 🚀 Come eseguire il progetto

### Prerequisiti
- **JDK 25 (LTS)**
- **Gradle**

### Istruzioni

1. **Clonazione del repository**
   ```bash
   git clone [https://github.com/pietrobrutti/Beyond-The-Snow.git](https://github.com/pietrobrutti/Beyond-The-Snow.git)
   cd BeyondTheSnow
   
2. **Build del progetto**
    ```bash
    ./gradlew build
   
3. **Esecuzione**
    ```bash
    ./gradlew run

## 🤖 Uso di strumenti di AI

Durante lo sviluppo di questo progetto è stato utilizzato un assistente AI (**Gemini** di Google) come supporto tecnico, di revisione e di co-progettazione, mantenendo il pieno controllo progettuale e la totale comprensione di ogni componente implementato.

Nello specifico, l'AI è stata impiegata per:
- **Revisione architetturale e principi SOLID**:
    - Verifica della corretta separazione delle responsabilità tra i package `model`, `view` e `controller`.
    - Applicazione del principio di inversione delle dipendenze tramite l'uso di callback (`Runnable`, `BiConsumer`) e `Supplier` per disaccoppiare le viste dal modello dati.
- **Logica di gioco e algoritmi**:
    - Supporto nella strutturazione della generazione procedurale della mappa a griglia.
    - Gestione degli stati vitali del personaggio (HP e Resistenza al Gelo) e del sistema di combattimento a turni.
- **Supporto su JavaFX e styling**:
    - Risoluzione di problematiche legate al layout responsivo (`StackPane`, `BorderPane`, `GridPane`).
    - Gestione delle animazioni (`TranslateTransition`) e definizione degli stili CSS inline.
- **Creazione degli asset grafici**:
    - Generazione delle immagini e degli sfondi utilizzati all'interno dell'interfaccia di gioco (menu principale, schermata di vittoria, game over e sprite delle creature).

Ogni parte del codice suggerita o revisionata è stata analizzata, testata, integrata e validata personalmente affinché rispettasse le specifiche del progetto.

---

## ⚠️Nota
Progetto universitario sviluppato per l'esame presso l'Università di Camerino (UNICAM).

📌 Per un'analisi approfondita delle scelte architetturali, dell'applicazione dei principi SOLID e dei dettagli sull'uso dell'AI, consultare la [**Wiki del repository**](https://github.com/pietrobrutti/Beyond-The-Snow/wiki/Pagina-1).