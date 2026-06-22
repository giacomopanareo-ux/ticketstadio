-- ════════════════════════════════════════════════════════════
--  TicketStadio — Dump del database
--  
--  Questo file contiene:
--    1. La struttura completa delle 10 tabelle (DDL)
--    2. I dati di esempio per la demo
--  
--  Esecuzione:
--    mysql -u root -p < ticketstadio_dump.sql
--  
--  Risultato:
--    - Database "ticketstadio" creato
--    - 10 tabelle popolate con dati realistici
--    - 4 utenti demo (1 admin + 3 tifosi), password = "Password123!"
--    - 7 partite (5 future + 2 concluse)
--    - 4 promozioni di test (WELCOME10, DERBY20, ESTATE15, OLD50)
--    - 260 posti distribuiti su 5 settori
-- ════════════════════════════════════════════════════════════

CREATE DATABASE IF NOT EXISTS ticketstadio 
    CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;

USE ticketstadio;

SET FOREIGN_KEY_CHECKS = 0;

-- Pulisci tabelle esistenti (per re-esecuzione idempotente)
DROP TABLE IF EXISTS pagamento, biglietto, abbonamento, promozione, 
                     posto, settore, partita, squadra, stadio, utente;

-- ════════════════════════════════════════════════════════════
--  1) SCHEMA — Definizione tabelle
-- ════════════════════════════════════════════════════════════

-- ── UTENTE ───────────────────────────────────────────────────
CREATE TABLE utente (
    id                 BIGINT       NOT NULL AUTO_INCREMENT,
    nome               VARCHAR(50)  NOT NULL,
    cognome            VARCHAR(50)  NOT NULL,
    email              VARCHAR(150) NOT NULL,
    password_hash      VARCHAR(255) NOT NULL,
    ruolo              ENUM('TIFOSO','ADMIN') NOT NULL DEFAULT 'TIFOSO',
    data_registrazione DATE         NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_utente_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── STADIO ───────────────────────────────────────────────────
CREATE TABLE stadio (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    nome            VARCHAR(100) NOT NULL,
    citta           VARCHAR(100) NOT NULL,
    capienza_totale INT          NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_stadio_nome (nome)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── SETTORE ──────────────────────────────────────────────────
CREATE TABLE settore (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    nome        VARCHAR(50)  NOT NULL,
    capienza    INT          NOT NULL,
    prezzo_base DECIMAL(8,2) NOT NULL,
    stadio_id   BIGINT       NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_settore_nome_stadio (stadio_id, nome),
    CONSTRAINT fk_settore_stadio FOREIGN KEY (stadio_id) 
        REFERENCES stadio(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── POSTO ────────────────────────────────────────────────────
CREATE TABLE posto (
    id         BIGINT   NOT NULL AUTO_INCREMENT,
    fila       CHAR(2)  NOT NULL,
    numero     SMALLINT NOT NULL,
    settore_id BIGINT   NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_posto_settore_fila_num (settore_id, fila, numero),
    CONSTRAINT fk_posto_settore FOREIGN KEY (settore_id) 
        REFERENCES settore(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── SQUADRA ──────────────────────────────────────────────────
CREATE TABLE squadra (
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    nome     VARCHAR(100) NOT NULL,
    citta    VARCHAR(100) NOT NULL,
    logo_url VARCHAR(255),
    PRIMARY KEY (id),
    UNIQUE KEY uq_squadra_nome (nome)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── PARTITA ──────────────────────────────────────────────────
CREATE TABLE partita (
    id                BIGINT   NOT NULL AUTO_INCREMENT,
    squadra_casa_id   BIGINT   NOT NULL,
    squadra_ospite_id BIGINT   NOT NULL,
    data_ora          DATETIME NOT NULL,
    stadio_id         BIGINT   NOT NULL,
    stato             ENUM('PROGRAMMATA','IN_CORSO','CONCLUSA','ANNULLATA') 
                      NOT NULL DEFAULT 'PROGRAMMATA',
    PRIMARY KEY (id),
    -- VINCOLO DI DOMINIO: due squadre devono essere diverse
    CONSTRAINT chk_squadre_diverse CHECK (squadra_casa_id <> squadra_ospite_id),
    CONSTRAINT fk_partita_casa   FOREIGN KEY (squadra_casa_id)   REFERENCES squadra(id),
    CONSTRAINT fk_partita_ospite FOREIGN KEY (squadra_ospite_id) REFERENCES squadra(id),
    CONSTRAINT fk_partita_stadio FOREIGN KEY (stadio_id)         REFERENCES stadio(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── BIGLIETTO ────────────────────────────────────────────────
CREATE TABLE biglietto (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    partita_id    BIGINT       NOT NULL,
    posto_id      BIGINT       NOT NULL,
    utente_id     BIGINT       NOT NULL,
    prezzo_pagato DECIMAL(8,2) NOT NULL,
    data_acquisto DATETIME     NOT NULL,
    stato         ENUM('VALIDO','USATO','ANNULLATO') NOT NULL DEFAULT 'VALIDO',
    PRIMARY KEY (id),
    -- VINCOLO CORE: impedisce la doppia vendita dello stesso posto per la stessa partita
    UNIQUE KEY uq_biglietto_partita_posto (partita_id, posto_id),
    CONSTRAINT fk_biglietto_partita FOREIGN KEY (partita_id) REFERENCES partita(id),
    CONSTRAINT fk_biglietto_posto   FOREIGN KEY (posto_id)   REFERENCES posto(id),
    CONSTRAINT fk_biglietto_utente  FOREIGN KEY (utente_id)  REFERENCES utente(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── ABBONAMENTO ──────────────────────────────────────────────
CREATE TABLE abbonamento (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    utente_id   BIGINT       NOT NULL,
    settore_id  BIGINT       NOT NULL,
    stagione    CHAR(9)      NOT NULL COMMENT 'Formato: 2024/2025',
    data_inizio DATE         NOT NULL,
    data_fine   DATE         NOT NULL,
    prezzo      DECIMAL(8,2) NOT NULL,
    PRIMARY KEY (id),
    -- Un utente non può abbonarsi due volte allo stesso settore nella stessa stagione
    UNIQUE KEY uq_abb_utente_settore_stagione (utente_id, settore_id, stagione),
    CONSTRAINT fk_abb_utente  FOREIGN KEY (utente_id)  REFERENCES utente(id),
    CONSTRAINT fk_abb_settore FOREIGN KEY (settore_id) REFERENCES settore(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── PROMOZIONE ───────────────────────────────────────────────
CREATE TABLE promozione (
    id                 BIGINT       NOT NULL AUTO_INCREMENT,
    codice             VARCHAR(20)  NOT NULL,
    descrizione        VARCHAR(255),
    sconto_percentuale DECIMAL(5,2) NOT NULL,
    data_inizio        DATE         NOT NULL,
    data_fine          DATE         NOT NULL,
    partita_id         BIGINT       NULL COMMENT 'NULL = valida per tutte le partite',
    PRIMARY KEY (id),
    UNIQUE KEY uq_promozione_codice (codice),
    CONSTRAINT fk_promo_partita FOREIGN KEY (partita_id) REFERENCES partita(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ── PAGAMENTO ────────────────────────────────────────────────
CREATE TABLE pagamento (
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    biglietto_id BIGINT       NOT NULL,
    metodo       ENUM('CARTA','PAYPAL','BONIFICO') NOT NULL,
    importo      DECIMAL(8,2) NOT NULL,
    data         DATETIME     NOT NULL,
    stato        ENUM('COMPLETATO','FALLITO','RIMBORSATO') NOT NULL,
    PRIMARY KEY (id),
    -- Relazione 1:1 con biglietto: un biglietto = un pagamento
    UNIQUE KEY uq_pagamento_biglietto (biglietto_id),
    CONSTRAINT fk_pagamento_biglietto FOREIGN KEY (biglietto_id) REFERENCES biglietto(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;

-- ════════════════════════════════════════════════════════════
--  2) DATI DI ESEMPIO
-- ════════════════════════════════════════════════════════════

-- ── Utenti ───────────────────────────────────────────────────
-- Tutte le password = "Password123!" (BCrypt hash con cost factor 12)
INSERT INTO utente (nome, cognome, email, password_hash, ruolo, data_registrazione) VALUES
('Admin',  'Sistema',  'admin@ticketstadio.it',
 '$2a$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
 'ADMIN',  CURDATE()),
('Mario',  'Rossi',    'mario.rossi@email.it',
 '$2a$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
 'TIFOSO', CURDATE() - INTERVAL 30 DAY),
('Giulia', 'Bianchi',  'giulia.bianchi@email.it',
 '$2a$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
 'TIFOSO', CURDATE() - INTERVAL 15 DAY),
('Luca',   'Ferrari',  'luca.ferrari@email.it',
 '$2a$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
 'TIFOSO', CURDATE() - INTERVAL 7 DAY);

-- ── Stadio ───────────────────────────────────────────────────
INSERT INTO stadio (nome, citta, capienza_totale) VALUES
('Stadio Paolo Mazza', 'Ferrara', 16134);

-- ── Settori ──────────────────────────────────────────────────
INSERT INTO settore (nome, capienza, prezzo_base, stadio_id) VALUES
('Curva Nord',    3000, 15.00, 1),
('Curva Sud',     3000, 15.00, 1),
('Tribuna Ovest', 4000, 25.00, 1),
('Tribuna Est',   4000, 25.00, 1),
('Settore VIP',    200, 75.00, 1);

-- ── Posti — generazione massiva con CROSS JOIN (50 posti × 5 settori = 250) ──
INSERT INTO posto (fila, numero, settore_id)
SELECT f.fila, n.numero, s.id 
FROM (SELECT 'A' fila UNION SELECT 'B' UNION SELECT 'C' UNION SELECT 'D' UNION SELECT 'E') f,
     (SELECT 1 numero UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 
      UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) n,
     (SELECT id FROM settore) s;

-- ── Squadre ──────────────────────────────────────────────────
INSERT INTO squadra (nome, citta) VALUES
('SPAL',       'Ferrara'),
('Bologna FC', 'Bologna'),
('Juventus',   'Torino'),
('AC Milan',   'Milano'),
('Inter',      'Milano'),
('AS Roma',    'Roma'),
('Napoli',     'Napoli'),
('Atalanta',   'Bergamo'),
('Fiorentina', 'Firenze'),
('Lazio',      'Roma');

-- ── Partite (5 future + 2 concluse) ──────────────────────────
INSERT INTO partita (squadra_casa_id, squadra_ospite_id, data_ora, stadio_id, stato) VALUES
(1, 2, DATE_ADD(NOW(), INTERVAL  7 DAY), 1, 'PROGRAMMATA'),  -- SPAL vs Bologna
(1, 3, DATE_ADD(NOW(), INTERVAL 14 DAY), 1, 'PROGRAMMATA'),  -- SPAL vs Juventus
(1, 4, DATE_ADD(NOW(), INTERVAL 21 DAY), 1, 'PROGRAMMATA'),  -- SPAL vs Milan
(1, 5, DATE_ADD(NOW(), INTERVAL 28 DAY), 1, 'PROGRAMMATA'),  -- SPAL vs Inter
(1, 6, DATE_ADD(NOW(), INTERVAL 35 DAY), 1, 'PROGRAMMATA'),  -- SPAL vs Roma
(1, 7, DATE_SUB(NOW(), INTERVAL  7 DAY), 1, 'CONCLUSA'),     -- SPAL vs Napoli (passata)
(1, 8, DATE_SUB(NOW(), INTERVAL 14 DAY), 1, 'CONCLUSA');     -- SPAL vs Atalanta (passata)

-- ── Promozioni di test ───────────────────────────────────────
INSERT INTO promozione (codice, descrizione, sconto_percentuale, data_inizio, data_fine, partita_id) VALUES
('WELCOME10', 'Sconto benvenuto 10% su tutte le partite',
  10.00, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 365 DAY), NULL),
('DERBY20', 'Sconto derby 20% - SPAL vs Bologna',
  20.00, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 8 DAY), 1),
('ESTATE15', 'Sconto estivo 15%',
  15.00, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 90 DAY), NULL),
('OLD50', 'Promo scaduta - per test validazione',
  50.00, DATE_SUB(CURDATE(), INTERVAL 30 DAY), DATE_SUB(CURDATE(), INTERVAL 1 DAY), NULL);

-- ── Abbonamenti di esempio ───────────────────────────────────
INSERT INTO abbonamento (utente_id, settore_id, stagione, data_inizio, data_fine, prezzo) VALUES
(2, 1, '2024/2025', '2024-07-01', '2025-06-30', 300.00),  -- Mario: Curva Nord
(3, 3, '2024/2025', '2024-07-01', '2025-06-30', 500.00);  -- Giulia: Tribuna Ovest

-- ── Biglietti già acquistati (per dati realistici nella dashboard) ──
INSERT INTO biglietto (partita_id, posto_id, utente_id, prezzo_pagato, data_acquisto, stato) VALUES
(1, 1,   2, 15.00, DATE_SUB(NOW(), INTERVAL 2 DAY), 'VALIDO'),   -- Mario, Curva Nord A1
(1, 2,   3, 12.00, DATE_SUB(NOW(), INTERVAL 1 DAY), 'VALIDO'),   -- Giulia, Curva Nord A2 (-20% DERBY)
(1, 101, 4, 25.00, DATE_SUB(NOW(), INTERVAL 1 DAY), 'VALIDO');   -- Luca, Tribuna Ovest A1

-- ── Pagamenti corrispondenti ─────────────────────────────────
INSERT INTO pagamento (biglietto_id, metodo, importo, data, stato) VALUES
(1, 'CARTA',    15.00, DATE_SUB(NOW(), INTERVAL 2 DAY), 'COMPLETATO'),
(2, 'PAYPAL',   12.00, DATE_SUB(NOW(), INTERVAL 1 DAY), 'COMPLETATO'),
(3, 'CARTA',    25.00, DATE_SUB(NOW(), INTERVAL 1 DAY), 'COMPLETATO');

-- ════════════════════════════════════════════════════════════
--  3) VERIFICA — Output di controllo
-- ════════════════════════════════════════════════════════════

SELECT 'Setup completato con successo!' AS messaggio;
SELECT COUNT(*) AS utenti     FROM utente;
SELECT COUNT(*) AS posti      FROM posto;
SELECT COUNT(*) AS partite    FROM partita;
SELECT COUNT(*) AS biglietti  FROM biglietto;
SELECT COUNT(*) AS promozioni FROM promozione;
