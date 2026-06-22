# TicketStadio

> Sistema di vendita biglietti calcio — Progetto congiunto Università di Ferrara

Elaborato sviluppato per tre esami:
- **Ingegneria del Software** — processo, architettura, qualità
- **Basi di Dati** — schema relazionale, vincoli, transazioni
- **Ingegneria dei Sistemi Web** — REST API, JWT, frontend disaccoppiato

---

## Demo rapida

| Ruolo | Email | Password |
|-------|-------|----------|
| Admin | `admin@ticketstadio.it` | `Password123!` |
| Tifoso | `mario.rossi@email.it` | `Password123!` |

**Codici promo demo:**
- `WELCOME10` — sconto 10% su tutte le partite
- `DERBY20` — sconto 20% solo per SPAL vs Bologna

---

## Stack tecnologico

| Livello | Tecnologia |
|---------|------------|
| Linguaggio backend | Java 17 |
| Framework backend | Spring Boot 3.2.5 |
| ORM | Spring Data JPA + Hibernate |
| Database | MySQL 8.0 |
| Autenticazione | JWT (jjwt 0.12.5) |
| Documentazione API | SpringDoc OpenAPI (Swagger UI) |
| Frontend | HTML5 + CSS3 + JavaScript vanilla |
| Test | JUnit 5 + Mockito + H2 in-memory |
| Build | Maven |

---

## Setup locale (5 minuti)

### Opzione A — Docker (consigliato)

```bash
git clone https://github.com/giacomopanareo/ticketstadio.git
cd ticketstadio/backend
docker compose up -d
# attendi ~30 secondi che MySQL si inizializzi
```

Apri `frontend/index.html` con un Live Server (es. estensione VS Code) o con un semplice:
```bash
cd frontend && python3 -m http.server 5500
```

### Opzione B — Manuale

1. **Database:**
   ```bash
   mysql -u root -p < docs/ticketstadio_demo.sql
   ```

2. **Backend:**
   ```bash
   cd backend
   # Modifica src/main/resources/application.properties:
   #   spring.datasource.password = <la tua password>
   #   jwt.secret = $(openssl rand -base64 32)
   mvn spring-boot:run
   ```

3. **Frontend:** apri `frontend/index.html` con Live Server.

---

## URL utili

| Cosa | URL |
|------|-----|
| Frontend | http://localhost:5500 (Live Server) |
| Backend API | http://localhost:8080/api |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs |

---

## Architettura

```
┌──────────────┐  HTTP/JSON   ┌──────────────────────┐   JDBC   ┌──────────┐
│   CLIENT     │  ───────────>│   SERVER             │ ───────> │  MySQL   │
│              │              │   Spring Boot        │          │          │
│  HTML/CSS/JS │              │                      │          │ 10 tab.  │
│  vanilla     │  <────────── │  Controller          │ <─────── │ InnoDB   │
└──────────────┘    JWT Auth  │  Service             │          └──────────┘
                              │  Repository          │
                              │  Security (JWT)      │
                              └──────────────────────┘
```

### Pattern adottati
- **MVC** server-side
- **Service Layer** per la business logic
- **Repository** pattern (Domain-Driven Design)
- **DTO** per disaccoppiare API da entità
- **Dependency Injection** via Spring
- **REST** resource-oriented
- **Stateless authentication** via JWT

---

## Test

```bash
cd backend
mvn test
```

23 test su 3 livelli:
- **Unit** (Mockito): BigliettoServiceTest, PromozioneIsValidaTest
- **Slice** (@DataJpaTest): RepositoryTest
- **Integration** (@SpringBootTest + MockMvc): AuthControllerTest

---

## Funzionalità principali

### Tifoso
- Registrazione e login con JWT
- Visualizzazione partite future e passate, con filtri
- Selezione posto numerato sulla mappa del settore
- Applicazione codici promozionali
- Sottoscrizione abbonamenti stagionali
- Storico acquisti

### Admin
- Dashboard statistiche vendite (aggregati real-time)
- CRUD partite + annullamento con invalidazione biglietti
- Gestione codici promo
- Gestione settori dello stadio

---

## Sicurezza

- Password salvate con **BCrypt** (cost factor 12, salt automatico)
- Autenticazione **JWT** (HS256, scadenza 24h)
- Autorizzazione granulare con `@PreAuthorize` (ROLE_TIFOSO, ROLE_ADMIN)
- **CORS** configurato per sviluppo locale
- Validazione input server-side con Bean Validation (`@Valid`)
- Vincoli DB enforced (`UNIQUE`, `CHECK`, `FOREIGN KEY`)

---

## Struttura del progetto

```
ticketstadio/
├── backend/                 # Spring Boot application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/it/unife/ticketstadio/
│   │   │   │   ├── entity/        # JPA entities (10 classi)
│   │   │   │   ├── repository/    # Spring Data interfaces (10)
│   │   │   │   ├── service/       # Business logic (8)
│   │   │   │   ├── controller/    # REST endpoints (8)
│   │   │   │   ├── dto/           # Data Transfer Objects (12)
│   │   │   │   ├── security/      # JWT filter, UserDetails
│   │   │   │   ├── config/        # Security, CORS, OpenAPI
│   │   │   │   └── exception/     # Global handler + custom
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/                  # 23 test JUnit
│   └── pom.xml
├── frontend/                # HTML/CSS/JS vanilla
│   ├── index.html
│   ├── partita.html
│   ├── login.html · register.html
│   ├── miei-biglietti.html
│   ├── abbonamenti.html
│   ├── admin/               # Pannello admin
│   ├── css/style.css
│   └── js/
│       ├── api.js           # Wrapper fetch + JWT
│       ├── auth.js          # Login/logout, decode JWT
│       └── utils.js         # Formattazione, toast
├── docs/
│   ├── TicketStadio_AnalisiRequisiti.docx
│   └── ticketstadio_demo.sql   # Schema + dati demo
├── relazione_esame/         # Materiale esame IS
│   ├── Relazione.pdf
│   ├── Relazione.pptx
│   ├── demo.mp4
│   ├── Dockerfile · docker-compose.yml
│   └── LINKS.txt
└── README.md
```

---

## Autore

**Giacomo Panareo**  
Università degli Studi di Ferrara — A.A. 2025/2026  
giacomo.panareo@edu.unife.it
