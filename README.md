# TicketStadio
Sistema di vendita biglietti per partite di calcio.
**Progetto congiunto: Basi di Dati · Ingegneria dei Sistemi Web · Ingegneria del Software**

Università degli Studi di Ferrara — A.A. 2025/2026

---

## Setup rapido
1. `mysql -u root -p < docs/ticketstadio_demo.sql`
2. Modifica `backend/src/main/resources/application.properties` (password MySQL + jwt.secret)
3. `cd backend && mvn spring-boot:run`
4. Apri `frontend/index.html` nel browser
5. Swagger UI: http://localhost:8080/swagger-ui.html

### Setup con Docker (alternativo)
```bash
cd backend
docker compose up -d
```
Il file `docker-compose.yml` in `backend/relazione_esame/` avvia MySQL + backend in un colpo solo.

---

## Credenziali demo
| Ruolo | Email | Password |
|-------|-------|----------|
| Admin | admin@ticketstadio.it | Password123! |
| Tifoso | mario.rossi@email.it | Password123! |
| Tifoso | giulia.bianchi@email.it | Password123! |

## Codici promo demo
| Codice | Sconto | Note |
|--------|--------|------|
| WELCOME10 | 10% | Valido su tutte le partite |
| DERBY20 | 20% | Solo SPAL vs Bologna |
| ESTATE15 | 15% | Generica |
| OLD50 | 50% | Scaduta (test validazione) |

---

## Stack
- **Backend:** Spring Boot 3.2 + Java 17 + JWT
- **Database:** MySQL 8.0 (PostgreSQL per +1 punto BD)
- **Frontend:** HTML5 + CSS3 + JavaScript vanilla
- **Test:** JUnit 5 + Mockito + H2

---

## Architettura
┌──────────────┐  HTTP/JSON   ┌──────────────────────┐   JDBC   ┌──────────┐

│   CLIENT     │  ───────────>│   SERVER             │ ───────> │  MySQL   │

│              │              │   Spring Boot        │          │          │

│  HTML/CSS/JS │              │                      │          │ 10 tab.  │

│  vanilla     │  <────────── │  Controller          │ <─────── │ InnoDB   │

└──────────────┘    JWT Auth  │  Service             │          └──────────┘

│  Repository          │

│  Security (JWT)      │

└──────────────────────┘
**Pattern adottati:** MVC server-side · Service Layer · Repository · DTO · Dependency Injection · REST resource-oriented · Stateless authentication

---

## Funzionalità

### Tifoso
- Registrazione e login con JWT
- Visualizzazione partite future e passate, con filtri per squadra e stato
- Selezione posto numerato sulla mappa del settore
- Applicazione codici promozionali
- Sottoscrizione e rinnovo abbonamenti stagionali
- Storico acquisti

### Admin
- Dashboard statistiche vendite (KPI real-time)
- CRUD partite + annullamento con invalidazione biglietti
- Gestione codici promo (creazione, generatore random, disattivazione)
- Gestione settori dello stadio

---

## Test

```bash
cd backend
mvn test
```

23 test su 3 livelli:
- **Unit** (Mockito): logica di business isolata
- **Slice** (`@DataJpaTest`): query JPA su H2 in-memory
- **Integration** (`@SpringBootTest` + MockMvc): endpoint HTTP completi

---

## Materiale d'esame

| Esame | Posizione |
|-------|-----------|
| Ingegneria del Software | `backend/relazione_esame/` |
| Basi di Dati | `docs/` |
| Ingegneria dei Sistemi Web | `docs/` |

---

## Struttura del progetto
ticketstadio/

├── README.md

├── .gitignore

├── backend/                     # Spring Boot

│   ├── src/main/java/...

│   ├── src/test/...

│   ├── pom.xml

│   └── relazione_esame/         # Materiale esame IS

│       ├── Relazione.pdf

│       ├── demo.mp4

│       ├── Dockerfile

│       └── ...

├── frontend/                    # HTML/CSS/JS vanilla

└── docs/                        # Documentazione esami BD/ISW
---

## Autore

**Giacomo Panareo**
Università degli Studi di Ferrara — A.A. 2025/2026
giacomo.panareo@edu.unife.it
