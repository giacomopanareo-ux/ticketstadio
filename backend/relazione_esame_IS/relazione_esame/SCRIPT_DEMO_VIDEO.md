# Script per la demo video — TicketStadio

> **Obiettivo:** registrare con OBS un video di **3-4 minuti** che mostri il percorso completo di un utente attraverso la funzionalità più rappresentativa: l'**acquisto di un biglietto**, con annessa registrazione e applicazione di un codice promo.

---

## Setup OBS prima di registrare

1. **Scena**: una sola "Display Capture" del monitor principale
2. **Risoluzione output**: 1920x1080 a 30fps (rec. → MP4)
3. **Audio**: microfono attivo + audio desktop (per sentire suoni eventuali)
4. **Bitrate**: 4000 kbps (qualità buona, file < 100MB)
5. **Formato output**: MP4 (`Impostazioni` → `Output` → `Formato registrazione: mp4`)
6. **Salva in**: `relazione_esame/demo.mp4`

### Preparazione browser
- Apri **Chrome in finestra normale** (non incognito, no estensioni visibili)
- Risoluzione **1920x1080**
- Chiudi tutte le altre tab
- Zoom: 100% (Ctrl+0)
- Cancella localStorage prima di iniziare (DevTools → Application → Local Storage → Clear)

### Preparazione backend
```bash
cd backend
mvn spring-boot:run
# attendi "Started TicketstadioApplication" prima di registrare
```

### Preparazione frontend
Apri `frontend/index.html` con Live Server di VS Code (porta 5500 di default).

---

## Storyboard — Cosa fare e cosa dire (4 minuti)

### Intro — 15 secondi

**Cosa mostro:** Schermata homepage TicketStadio (index.html) aperta nel browser

**Cosa dico:**
> "Buongiorno, in questa demo mostro il flusso completo di acquisto biglietto su TicketStadio. Partiamo dalla homepage che mostra le partite future con un filtro per squadra e stato."

**Azione:** Apri filtro "Squadra" e seleziona "Bologna FC" per mostrare il filtro in azione, poi torna a "Tutte".

---

### Step 1 — Registrazione utente — 30 secondi

**Cosa mostro:** Pagina /register.html

**Cosa dico:**
> "L'acquisto richiede autenticazione. Mi registro come nuovo utente. Notare l'indicatore di forza password e la validazione lato client."

**Azione:**
- Compila form: Nome `Demo`, Cognome `User`, Email `demo@test.it`, Password `Password123!`
- Mostra l'indicatore forza password che cambia colore (verde)
- Click "Registrati"
- Toast verde "Registrazione completata"

---

### Step 2 — Login con JWT — 25 secondi

**Cosa mostro:** Pagina /login.html → poi /index.html con utente loggato

**Cosa dico:**
> "Il login restituisce un JWT che viene salvato nel localStorage e usato come Bearer token per le richieste protette."

**Azione:**
- Compila email + password
- Apri DevTools → Network tab PRIMA del click su Login (per mostrare la chiamata POST)
- Click "Accedi"
- Mostra la response 200 con `{ "token": "..." }`
- Apri DevTools → Application → Local Storage → mostra il JWT salvato
- Apri https://jwt.io in nuova tab, incolla il token, mostra il payload decodificato: `sub`, `roles`, `exp`
- Torna su index.html, navbar mostra l'email utente

---

### Step 3 — Selezione partita e settore — 30 secondi

**Cosa mostro:** Click su una partita programmata (es. SPAL vs Bologna)

**Cosa dico:**
> "Apro la prossima partita in calendario. La pagina di dettaglio mostra una mappa interattiva dei settori. Ogni settore ha un prezzo base diverso. Seleziono la Tribuna Ovest."

**Azione:**
- Click sulla card della partita SPAL vs Bologna
- Mostra l'header con i nomi delle squadre, data, stadio
- Scorri verso il basso fino alla mappa settori
- Click su "Tribuna Ovest"
- Si carica la griglia dei posti

---

### Step 4 — Selezione posto + race condition — 35 secondi

**Cosa mostro:** Griglia posti con alcuni occupati (grigi) e altri liberi

**Cosa dico:**
> "I posti grigi sono già occupati: vengono caricati dal backend con una query che restituisce tutti i biglietti non annullati per questa partita. Seleziono il posto A5. Notare che se due utenti contemporaneamente provassero a comprare lo stesso posto, il DB blocca la seconda transazione grazie al vincolo UNIQUE(partita_id, posto_id)."

**Azione:**
- Hover su un posto occupato per mostrare che è cliccabile
- Click su un posto libero (es. A5 della Tribuna)
- Il posto diventa verde, il riepilogo si aggiorna a destra
- Apri DevTools → Network e mostra che è stata fatta una `GET /api/partite/1/posti` al caricamento

---

### Step 5 — Applicazione codice promo — 25 secondi

**Cosa mostro:** Campo codice promo nel riepilogo

**Cosa dico:**
> "Applico un codice promo. Il backend valida che il codice esista, sia nelle date di validità e si applichi a questa partita. Se valido, mostra lo sconto nel riepilogo."

**Azione:**
- Inserisci codice `WELCOME10`
- Click "Applica"
- Toast verde "Promo applicata: -10%"
- Il riepilogo mostra "Sconto -10%" e il totale aggiornato
- (Opzionale) Prova un codice errato es. `WRONG` → toast rosso "Codice non valido"

---

### Step 6 — Acquisto + transazione atomica — 30 secondi

**Cosa mostro:** Tasto "Acquista biglietto"

**Cosa dico:**
> "Clicco acquista. Il backend apre una transazione, inserisce il biglietto, crea il pagamento associato, fa commit. Se qualcosa fallisce, rollback automatico grazie all'annotazione @Transactional."

**Azione:**
- Seleziona metodo pagamento "Carta"
- DevTools Network aperto
- Click "Acquista biglietto"
- Mostra request POST /api/biglietti/acquista con body JSON
- Mostra response 201 Created con il biglietto restituito
- Toast verde "Biglietto acquistato!"
- Redirect automatico a /miei-biglietti.html

---

### Step 7 — Verifica biglietto + pannello admin — 35 secondi

**Cosa mostro:** Tabella biglietti dell'utente + (logout) → login come admin → dashboard

**Cosa dico:**
> "Il biglietto compare nello storico personale. Faccio ora logout e login come amministratore per mostrare la dashboard delle vendite, che usa query aggregate per calcolare incassi e biglietti venduti per ogni partita."

**Azione:**
- Mostra il biglietto appena acquistato nella tabella "I miei biglietti"
- Click "Esci"
- Login come `admin@ticketstadio.it` / `Password123!`
- Vai su "Admin" nella navbar → Dashboard
- Mostra le KPI in alto (totale biglietti, incasso, abbonamenti, partite)
- Scrolla fino alla tabella vendite per partita
- Mostra che la partita SPAL vs Bologna ha 1 biglietto venduto in più

---

### Outro — 10 secondi

**Cosa mostro:** Swagger UI a http://localhost:8080/swagger-ui.html

**Cosa dico:**
> "Tutte le 25 API REST sono documentate automaticamente con SpringDoc OpenAPI. Grazie per l'attenzione."

**Azione:** Apri swagger-ui.html, scrolla rapidamente, stop registrazione.

---

## Post-produzione minima

1. Apri il file `demo.mp4` con un editor leggero (es. **Shotcut** o **CapCut** gratuiti)
2. Taglia inizio/fine se necessario
3. Esporta come MP4 H.264, max 100MB
4. Salva come `relazione_esame/demo.mp4`

---

## Backup: se qualcosa va male durante la registrazione

- **Se il backend crasha:** STOP, riavvia, registra di nuovo da capo. Non tagliare in mezzo.
- **Se sbagli a digitare:** continua. Piccole esitazioni umane sono ok, non è un video di marketing.
- **Se hai voce nervosa:** registra prima un audio separato e poi sovrapponilo al video muto.

## Cosa NON fare

- ❌ Non mostrare la directory di codice mentre parli (sembra che stai cercando)
- ❌ Non aprire 10 tab del browser, distrae
- ❌ Non zoomare/scrollare velocemente, l'esaminatore deve vedere
- ❌ Non lasciare DevTools aperto su pannelli che non stai mostrando
- ❌ Non usare "ehm", "tipo", "diciamo": meglio una pausa
