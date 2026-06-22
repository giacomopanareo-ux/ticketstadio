# `relazione_esame/` — Indice del contenuto

> Directory con tutto il materiale per l'esame di **Ingegneria del Software**.

---

## Cosa c'è qui dentro

| File | Cosa fa | Stato |
|------|---------|:-:|
| `Relazione.pptx` | Presentazione 10 slide in PowerPoint editabile | ✅ Pronta |
| `Relazione.pdf` | Stessa presentazione in PDF (formato consegna) | ✅ Pronta |
| `demo.mp4` | Video demo registrato con OBS | ⚠️ DA REGISTRARE |
| `Dockerfile` | Container del backend Spring Boot | ✅ Pronto |
| `docker-compose.yml` | Stack completo backend + MySQL | ✅ Pronto |
| `ticketstadio_dump.sql` | Dump DB per ricreare tabelle in locale | ✅ Pronto |
| `LINKS.txt` | Link a Trello, Miro, Figma | ⚠️ DA COMPILARE |
| `README_REPO_TEMPLATE.md` | Template README da mettere alla root del repo GitHub | ✅ Pronto |
| `SETUP_STRUMENTI.md` | Guida passo passo per GitHub, Trello, Miro, Figma | 📖 Da leggere |
| `SCRIPT_DEMO_VIDEO.md` | Storyboard del video demo | 📖 Da seguire |
| `MIGLIORAMENTI_CODICE.md` | Suggerimenti per commenti, accessibilità, usabilità | 📖 Da applicare |

---

## Ordine operativo consigliato

> Hai pochi giorni. Segui questo ordine:

### Giorno 1 (oggi) — Setup GitHub
1. Leggi `SETUP_STRUMENTI.md` sezione "GitHub"
2. Crea il repo
3. Copia `README_REPO_TEMPLATE.md` → `README.md` alla root del repo
4. Aggiungi la directory `relazione_esame/` (questa)
5. Push iniziale

### Giorno 2 — Trello e Miro
1. Leggi `SETUP_STRUMENTI.md` sezioni "Trello" e "Miro"
2. Crea board Trello pubblica con le 36 card consigliate (~ 90 minuti)
3. Crea lavagna Miro con flowchart (~ 60 minuti)
4. Aggiorna `LINKS.txt` con gli URL reali
5. Fai uno **screenshot del flowchart Miro** e sostituiscilo nella slide 5 (vedi sotto)

### Giorno 3 — Migliora il codice
1. Apri `MIGLIORAMENTI_CODICE.md`
2. Applica le modifiche sulla **checklist sezione D** (~ 2-3 ore)
3. Commit + push: `git commit -m "docs: improved JavaDoc and accessibility"`

### Giorno 4 — Registra la demo
1. Leggi `SCRIPT_DEMO_VIDEO.md` da cima a fondo (lo conoscerai a memoria)
2. Setup OBS come descritto
3. Avvia backend + frontend, ripeti il flow una volta SENZA registrare
4. Registra in un take (~ 4 minuti)
5. Salva il file come `demo.mp4` in questa directory
6. Commit + push

### Giorno 5 — Personalizza le slide
1. Apri `Relazione.pptx` in PowerPoint
2. Sostituisci nella **slide 5** il diagramma placeholder con uno screenshot del tuo flowchart Miro
3. Sostituisci nella **slide 6** la rappresentazione Trello con uno screenshot della tua board reale
4. Eventualmente cambia il tuo email/GitHub username nella slide 10
5. Esporta in PDF: `File → Esporta → Crea PDF` → sovrascrivi `Relazione.pdf`
6. Commit + push

### Giorno 6 — Ripasso esposizione
1. Leggi tutte le **note relatore** delle 10 slide (`Visualizza → Note` in PowerPoint)
2. Esponi a voce alta davanti allo specchio
3. Cronometra: deve durare 8-12 minuti
4. Identifica le 2-3 slide su cui esiti di più → ripassale

### Giorno 7 — Esame
1. Apri il PDF della relazione + il browser con repository, Trello, Miro pronti
2. Verifica che TUTTI i link in `LINKS.txt` funzionino in incognito
3. Avvia backend + frontend prima di entrare in aula
4. Rilassati: hai fatto un lavoro completo

---

## File da sostituire/personalizzare PRIMA dell'esame

| File | Cosa cambiare |
|------|---------------|
| `LINKS.txt` | Sostituisci tutti gli `XXXXXXXX` con gli URL reali |
| `Relazione.pptx` slide 5 | Inserisci screenshot del tuo flowchart Miro |
| `Relazione.pptx` slide 6 | Inserisci screenshot della tua board Trello |
| `Relazione.pptx` slide 10 | Verifica che l'URL GitHub sia corretto |
| `README_REPO_TEMPLATE.md` | Spostalo alla root del repo come `README.md` |

---

## Materiale di riferimento

Se durante l'esame il prof ti chiede teoria su:
- **Pattern architetturali** → slide 8 e `MIGLIORAMENTI_CODICE.md` sezione "B"
- **Test** → slide 7 e `backend/src/test/`
- **Sicurezza** → `MIGLIORAMENTI_CODICE.md` + slide 10
- **Concorrenza** → slide 5 (eccezioni gestite) + il vincolo UNIQUE nel dump SQL
- **Accessibilità** → `MIGLIORAMENTI_CODICE.md` sezione "B"

---

## Note importanti

**Il PDF della consegna chiede esplicitamente:**
> "Tutto il materiale deve essere inserito nel repository del progetto di backend (tranne il frontend)"

Significa che `relazione_esame/` va dentro la cartella del backend. Il tuo layout sarà:
```
ticketstadio/                  ← repo GitHub
├── README.md
├── .gitignore
├── backend/                   ← progetto Spring Boot
│   ├── pom.xml
│   ├── src/...
│   └── relazione_esame/       ← QUI dentro
│       ├── Relazione.pdf
│       ├── demo.mp4
│       └── ...
└── frontend/                  ← separato, NON in backend
    └── ...
```

**Tuttavia,** può anche valere se metti `relazione_esame/` direttamente alla root del repo — chiedi al prof per sicurezza, o seguila lettera per lettera la consegna.
