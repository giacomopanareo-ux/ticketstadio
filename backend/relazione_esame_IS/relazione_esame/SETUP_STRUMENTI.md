# Guida setup degli strumenti esterni

> Tutto quello che devi configurare PRIMA dell'esame. Segui l'ordine.

---

## 1. GitHub — Repository pubblico

### Crea il repo
1. Vai su https://github.com/new
2. Nome: `ticketstadio`
3. Descrizione: `Sistema vendita biglietti calcio — Progetto IS/BD/ISW UniFe`
4. Visibilità: **Public**
5. NON aggiungere README/gitignore/license (lo facciamo da local)
6. Click "Create repository"

### Push del codice
```bash
cd /percorso/del/tuo/progetto/TicketStadio

# Inizializza git (se non già fatto)
git init
git branch -M main

# Crea .gitignore
cat > .gitignore << 'EOF'
# Build
target/
*.class
*.jar

# IDE
.idea/
.vscode/
*.iml
.classpath
.project
.settings/

# OS
.DS_Store
Thumbs.db

# Logs
*.log

# Env locali
*.env
application-local.properties
EOF

# Aggiungi tutto e committa
git add .
git commit -m "Initial commit: TicketStadio progetto completo"

# Collega remote (sostituisci con il tuo username)
git remote add origin https://github.com/giacomopanareo/ticketstadio.git
git push -u origin main
```

### Organizzazione dei commit (consigliato)
Se hai tempo, **non fare un singolo commit "Initial"**. Fai commit storicizzati che raccontano lo sviluppo:

```bash
# Esempio di flusso ideale dopo init:
git add docs/ README.md
git commit -m "docs: aggiunta analisi requisiti e schema ER"

git add backend/pom.xml backend/src/main/resources/
git commit -m "build: setup Spring Boot 3.2 + Maven"

git add backend/src/main/java/it/unife/ticketstadio/entity/
git commit -m "feat(model): entità JPA — Utente, Partita, Biglietto, ..."

git add backend/src/main/java/it/unife/ticketstadio/repository/
git commit -m "feat(repository): interfacce Spring Data JPA"

# ... e così via fino a:
git add backend/src/test/
git commit -m "test: 23 test JUnit (unit + slice + integration)"

git add frontend/
git commit -m "feat(frontend): UI HTML/CSS/JS vanilla — 9 pagine"

git add relazione_esame/
git commit -m "docs: materiale per esame IS (slide, demo, Dockerfile)"

git push
```

### Aggiungi il README principale
Il README del repository è il **biglietto da visita**. Va in `README.md` ALLA ROOT, non in `relazione_esame/`. Vedi `README_REPO_TEMPLATE.md` in questa directory per un template completo.

---

## 2. Trello — Board pubblica

### Crea la board
1. Vai su https://trello.com → Login
2. Click "Crea" → "Crea bacheca"
3. Titolo: **TicketStadio — Sprint Backlog**
4. Sfondo: scegli un tema (suggerito: verde scuro per coerenza con il brand)
5. Visibilità: **Pubblica** (← FONDAMENTALE per l'esame!)

### Crea le 4 liste
Aggiungi in ordine queste 4 liste:
1. **Backlog** (idee future, non programmate)
2. **To Do** (sprint corrente o prossimo)
3. **In Progress** (massimo 2 card)
4. **Done** (completate)

### Card di esempio — copia/incolla queste user stories nella lista "Done"

Per ogni card, scegli "Aggiungi card" e usa questo formato:

**Titolo:** Login utente con JWT
**Descrizione:**
```markdown
**Come** tifoso non registrato
**Voglio** registrarmi e fare login
**In modo da** accedere alle funzionalità di acquisto

## Criteri di accettazione
- [x] Form di registrazione con nome, cognome, email, password
- [x] Validazione password: min 6 caratteri, indicatore forza
- [x] Hash password BCrypt lato server
- [x] Login restituisce JWT con scadenza 24h
- [x] Token salvato in localStorage del browser
- [x] Redirect automatico se token scaduto

## Note tecniche
- Backend: AuthController, AuthService, JwtUtil
- Frontend: login.html, register.html, auth.js

**Priorità:** Alta
**Commit:** `feat(auth): JWT-based authentication`
```

**Etichette:** Aggiungi un'etichetta "Sprint 1" colorata in verde.

### Le 25 user stories da creare (riassunto)

Ti elenco le 25 card che devi creare. Ognuna deve seguire il template sopra. Per velocità, ti basterà cambiare il titolo e adattare i criteri di accettazione.

**Lista "Done" (25 card):**

| # | Titolo | Sprint |
|---|--------|--------|
| 1 | Setup progetto Spring Boot + Maven | Sprint 1 |
| 2 | Schema database MySQL (10 tabelle) | Sprint 1 |
| 3 | Entità JPA + repository | Sprint 1 |
| 4 | Login utente con JWT | Sprint 1 |
| 5 | Registrazione nuovo utente | Sprint 1 |
| 6 | API CRUD partite (admin) | Sprint 2 |
| 7 | Endpoint pubblici partite (lista, dettaglio) | Sprint 2 |
| 8 | Visualizzazione mappa settori | Sprint 2 |
| 9 | Selezione posto numerato | Sprint 2 |
| 10 | Acquisto biglietto con transazione | Sprint 2 |
| 11 | Storico biglietti utente | Sprint 2 |
| 12 | Gestione codici promozionali | Sprint 3 |
| 13 | Validazione promo lato server | Sprint 3 |
| 14 | Applicazione sconto in fase di acquisto | Sprint 3 |
| 15 | Sottoscrizione abbonamento stagionale | Sprint 3 |
| 16 | Rinnovo abbonamento | Sprint 3 |
| 17 | Annullamento partita admin | Sprint 4 |
| 18 | Invalidazione biglietti su partita annullata | Sprint 4 |
| 19 | Dashboard admin con statistiche | Sprint 4 |
| 20 | Filtri partite per stato e squadra | Sprint 4 |
| 21 | Test unit per BigliettoService | Sprint 5 |
| 22 | Test integration per AuthController | Sprint 5 |
| 23 | Test slice per repository | Sprint 5 |
| 24 | Documentazione Swagger UI | Sprint 5 |
| 25 | Frontend responsive mobile | Sprint 5 |

**Lista "To Do" (3 card):**
- Dashboard admin con grafici (chart.js)
- Refactor CSS in moduli
- Documentazione API in italiano

**Lista "Backlog" (8 card):**
- Mappa interattiva 3D dello stadio
- Notifiche push per partite preferite
- Supporto multistadio
- Sistema rating partite
- Wallet utente per cashback
- Integrazione pagamento reale (Stripe)
- Export biglietto come PDF/QR code
- Versione mobile nativa

### Dopo aver creato le card
1. Click su "Mostra menu" → "Più..." → "Impostazioni"
2. Verifica "Visibilità bacheca = **Pubblica**"
3. Copia l'URL dalla barra del browser
4. Aprilo in finestra in incognito per verificare che funzioni senza login
5. Incolla l'URL in `LINKS.txt`

---

## 3. Miro — Disegno funzionale

### Crea la lavagna
1. Vai su https://miro.com → Login (account gratuito)
2. Click "Nuova lavagna" → "Lavagna vuota"
3. Titolo: **TicketStadio — User Flow**

### Cosa disegnare (15 minuti di lavoro)

Crea **3 sezioni** sulla lavagna, una sotto l'altra:

**Sezione A — User Journey (alto):**
Usa il template "User Journey Map" che Miro propone, oppure disegnalo manualmente con queste 6 fasi del tifoso:
1. Scopre il sito (homepage)
2. Si registra
3. Cerca una partita (filtri)
4. Sceglie un posto
5. Paga
6. Riceve il biglietto

Per ogni fase scrivi: **azione**, **touchpoint** (pagina), **emozione** (faccina), **dolori** (es. "deve digitare email").

**Sezione B — Flowchart acquisto biglietto (centro):**
Disegna il flowchart con questi simboli (template "Flowchart" di Miro):

```
[START] → [Login?] 
                 ↓ no → [Pagina registrazione] → [Pagina login]
                 ↓ yes
              [Lista partite]
                 ↓
              [Dettaglio partita]
                 ↓
              [Scelta settore]
                 ↓
              [Scelta posto]
                 ↓
              [Posto già occupato?] 
                                 ↓ yes → [Errore + ritorna]
                                 ↓ no
              [Codice promo?]
                              ↓ yes → [Valida promo] → [Applica sconto]
                              ↓ no
              [Riepilogo]
                 ↓
              [Conferma acquisto]
                 ↓
              [Transazione DB]
                 ↓
              [Posto già preso (race)?] → yes → [Errore 409]
                                          ↓ no
                                       [Biglietto creato]
                                          ↓
                                        [END]
```

**Sezione C — Architettura visuale (basso):**
Tre box rettangolari grandi con frecce:
```
[CLIENT browser]  ←--HTTP/JSON-->  [SERVER Spring Boot]  ←--JDBC-->  [MySQL]
```

Etichetta sotto ogni box quali tecnologie ci sono dentro.

### Condividi il link
1. Click "Share" in alto a destra
2. Imposta "Anyone with the link" → "Can view"
3. Copia il link
4. Aprilo in incognito per verificare
5. Incolla in `LINKS.txt`

### Screenshot per la slide 5
Ricordati di fare uno screenshot della Sezione B (flowchart) per metterlo come immagine nella slide 5 della relazione (sostituendo il diagramma di placeholder che ho creato).

---

## 4. Figma — Prototipo UI (FACOLTATIVO)

> Vale punti extra ma non è obbligatorio. Se hai poco tempo, salta questo step.

### Cosa fare se decidi di farlo (1-2 ore)
1. Crea un account su https://figma.com (gratuito)
2. Nuovo file → "Design"
3. Crea **5 frame mobile** (375x812 — iPhone) o desktop (1440x900):
   - Home (lista partite)
   - Dettaglio partita
   - Selezione posto
   - Pagamento
   - Biglietto acquistato

4. Per ognuno, ricrea il layout del tuo HTML in modo statico
5. Collega i frame con prototyping (click + connessione tra frame)
6. Click "Share" → "Anyone with the link" → "Can view"
7. Click "Present" → copia il link del prototipo
8. Incolla in `LINKS.txt`

### Alternativa veloce
Se Figma ti richiede troppo, crea solo i mockup nella stessa lavagna Miro nella Sezione D, usando le forme rettangolari di base. Vale come "prototipo".

---

## Checklist finale prima di consegnare

- [ ] Repo GitHub creato e pubblico
- [ ] Codice pushato con commit storicizzati (non un singolo commit)
- [ ] `.gitignore` corretto (no `target/`, no `.idea/`)
- [ ] README.md alla root del repo
- [ ] Directory `/relazione_esame/` nel repo con tutti i file
- [ ] `Relazione.pdf` nel `relazione_esame/`
- [ ] `demo.mp4` registrato e nel `relazione_esame/`
- [ ] `Dockerfile` testato (`docker build` funziona)
- [ ] `LINKS.txt` con TUTTI gli URL reali (no placeholder XXXXXXXX)
- [ ] Trello pubblico, testato in incognito
- [ ] Miro pubblico, testato in incognito
- [ ] (opzionale) Figma pubblico, testato in incognito
- [ ] Dump DB `ticketstadio_demo.sql` nella directory `db/` o `relazione_esame/`
- [ ] Backend e DB testati: `docker compose up -d` parte senza errori
- [ ] Tutti i 23 test JUnit passano: `mvn test`
- [ ] Hai provato a fare login e ad acquistare un biglietto end-to-end
