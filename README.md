# TicketStadio
Sistema di vendita biglietti per partite di calcio.
**Progetto congiunto: Basi di Dati · Ingegneria dei Sistemi Web · Ingegneria del Software**

## Setup rapido
1. `mysql -u root -p < docs/ticketstadio_demo.sql`
2. Modifica `backend/src/main/resources/application.properties` (password MySQL + jwt.secret)
3. `cd backend && mvn spring-boot:run`
4. Apri `frontend/index.html` nel browser
5. Swagger UI: http://localhost:8080/swagger-ui.html

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

## Stack
- Backend: Spring Boot 3.2 + Java 17 + JWT
- Database: MySQL 8.0 (PostgreSQL per +1 punto BD)
- Frontend: HTML5 + CSS3 + JavaScript vanilla
- Test: JUnit 5 + Mockito + H2
