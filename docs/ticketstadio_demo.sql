-- TicketStadio — Script SQL demo
-- Eseguire con: mysql -u root -p < ticketstadio_demo.sql
CREATE DATABASE IF NOT EXISTS ticketstadio CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE ticketstadio;
SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS pagamento,biglietto,abbonamento,promozione,posto,settore,partita,squadra,stadio,utente;

CREATE TABLE utente(id BIGINT NOT NULL AUTO_INCREMENT,nome VARCHAR(50) NOT NULL,cognome VARCHAR(50) NOT NULL,email VARCHAR(150) NOT NULL,password_hash VARCHAR(255) NOT NULL,ruolo ENUM('TIFOSO','ADMIN') NOT NULL DEFAULT 'TIFOSO',data_registrazione DATE NOT NULL,PRIMARY KEY(id),UNIQUE KEY uq_email(email))ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE stadio(id BIGINT NOT NULL AUTO_INCREMENT,nome VARCHAR(100) NOT NULL,citta VARCHAR(100) NOT NULL,capienza_totale INT NOT NULL,PRIMARY KEY(id),UNIQUE KEY uq_nome(nome))ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE settore(id BIGINT NOT NULL AUTO_INCREMENT,nome VARCHAR(50) NOT NULL,capienza INT NOT NULL,prezzo_base DECIMAL(8,2) NOT NULL,stadio_id BIGINT NOT NULL,PRIMARY KEY(id),UNIQUE KEY uq_settore(stadio_id,nome),CONSTRAINT fk_set_sta FOREIGN KEY(stadio_id)REFERENCES stadio(id)ON DELETE CASCADE)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE posto(id BIGINT NOT NULL AUTO_INCREMENT,fila CHAR(2) NOT NULL,numero SMALLINT NOT NULL,settore_id BIGINT NOT NULL,PRIMARY KEY(id),UNIQUE KEY uq_posto(settore_id,fila,numero),CONSTRAINT fk_pos_set FOREIGN KEY(settore_id)REFERENCES settore(id)ON DELETE CASCADE)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE squadra(id BIGINT NOT NULL AUTO_INCREMENT,nome VARCHAR(100) NOT NULL,citta VARCHAR(100) NOT NULL,logo_url VARCHAR(255),home_stadium_id BIGINT NULL,PRIMARY KEY(id),UNIQUE KEY uq_squadra(nome),CONSTRAINT fk_squ_sta FOREIGN KEY(home_stadium_id)REFERENCES stadio(id))ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE partita(id BIGINT NOT NULL AUTO_INCREMENT,squadra_casa_id BIGINT NOT NULL,squadra_ospite_id BIGINT NOT NULL,data_ora DATETIME NOT NULL,stadio_id BIGINT NOT NULL,stato ENUM('PROGRAMMATA','IN_CORSO','CONCLUSA','ANNULLATA') NOT NULL DEFAULT 'PROGRAMMATA',PRIMARY KEY(id),CONSTRAINT chk_sq CHECK(squadra_casa_id<>squadra_ospite_id),CONSTRAINT fk_par_cas FOREIGN KEY(squadra_casa_id)REFERENCES squadra(id),CONSTRAINT fk_par_osp FOREIGN KEY(squadra_ospite_id)REFERENCES squadra(id),CONSTRAINT fk_par_sta FOREIGN KEY(stadio_id)REFERENCES stadio(id))ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE biglietto(id BIGINT NOT NULL AUTO_INCREMENT,partita_id BIGINT NOT NULL,posto_id BIGINT NOT NULL,utente_id BIGINT NOT NULL,prezzo_pagato DECIMAL(8,2) NOT NULL,data_acquisto DATETIME NOT NULL,stato ENUM('VALIDO','USATO','ANNULLATO') NOT NULL DEFAULT 'VALIDO',PRIMARY KEY(id),UNIQUE KEY uq_big(partita_id,posto_id),CONSTRAINT fk_big_par FOREIGN KEY(partita_id)REFERENCES partita(id),CONSTRAINT fk_big_pos FOREIGN KEY(posto_id)REFERENCES posto(id),CONSTRAINT fk_big_ute FOREIGN KEY(utente_id)REFERENCES utente(id))ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE abbonamento(id BIGINT NOT NULL AUTO_INCREMENT,utente_id BIGINT NOT NULL,settore_id BIGINT NOT NULL,stagione CHAR(9) NOT NULL,data_inizio DATE NOT NULL,data_fine DATE NOT NULL,prezzo DECIMAL(8,2) NOT NULL,PRIMARY KEY(id),UNIQUE KEY uq_abb(utente_id,settore_id,stagione),CONSTRAINT fk_abb_ute FOREIGN KEY(utente_id)REFERENCES utente(id),CONSTRAINT fk_abb_set FOREIGN KEY(settore_id)REFERENCES settore(id))ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE promozione(id BIGINT NOT NULL AUTO_INCREMENT,codice VARCHAR(20) NOT NULL,descrizione VARCHAR(255),sconto_percentuale DECIMAL(5,2) NOT NULL,data_inizio DATE NOT NULL,data_fine DATE NOT NULL,partita_id BIGINT NULL,PRIMARY KEY(id),UNIQUE KEY uq_codice(codice),CONSTRAINT fk_pro_par FOREIGN KEY(partita_id)REFERENCES partita(id))ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE pagamento(id BIGINT NOT NULL AUTO_INCREMENT,biglietto_id BIGINT NOT NULL,metodo ENUM('CARTA','PAYPAL','BONIFICO') NOT NULL,importo DECIMAL(8,2) NOT NULL,data DATETIME NOT NULL,stato ENUM('COMPLETATO','FALLITO','RIMBORSATO') NOT NULL,PRIMARY KEY(id),UNIQUE KEY uq_pag(biglietto_id),CONSTRAINT fk_pag_big FOREIGN KEY(biglietto_id)REFERENCES biglietto(id))ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS=1;

-- Utenti: admin password = "provaData123" | tifosi password = "Password123!" (hash BCrypt)
INSERT INTO utente(nome,cognome,email,password_hash,ruolo,data_registrazione)VALUES
('Admin','Sistema','admin@ticketstadio.it','$2a$10$n2AGrrE04d1KFnghWm0n3uHW80nvqSAP63m1RybvL1OaukPlGW5Cu','ADMIN',CURDATE()),
('Mario','Rossi','mario.rossi@email.it','$2a$10$dXa1Pe7BhCHsglNwSKYF7.vKxc5/Ho.wHeGkjuexypm1seGmeRCSi','TIFOSO',CURDATE()-INTERVAL 30 DAY),
('Giulia','Bianchi','giulia.bianchi@email.it','$2a$10$dXa1Pe7BhCHsglNwSKYF7.vKxc5/Ho.wHeGkjuexypm1seGmeRCSi','TIFOSO',CURDATE()-INTERVAL 15 DAY),
('Luca','Ferrari','luca.ferrari@email.it','$2a$10$dXa1Pe7BhCHsglNwSKYF7.vKxc5/Ho.wHeGkjuexypm1seGmeRCSi','TIFOSO',CURDATE()-INTERVAL 7 DAY);

INSERT INTO stadio(nome,citta,capienza_totale)VALUES
('Stadio Olimpico Grande Torino','Torino',28000),
('Stadio Renato Dall''Ara','Bologna',38279),
('Allianz Stadium','Torino',45000),
('Stadio Giuseppe Meazza','Milano',75923),
('Stadio Diego Armando Maradona','Napoli',54726),
('Gewiss Stadium','Bergamo',24950),
('Stadio Artemio Franchi','Firenze',43147),
('Stadio Olimpico','Roma',70634);
INSERT INTO settore(nome,capienza,prezzo_base,stadio_id)
SELECT t.nome,t.capienza,t.prezzo_base,st.id FROM
(SELECT 'Curva Nord' nome,3000 capienza,15.00 prezzo_base UNION SELECT 'Curva Sud',3000,15.00 UNION SELECT 'Tribuna Ovest',4000,25.00 UNION SELECT 'Tribuna Est',4000,25.00 UNION SELECT 'Settore VIP',200,75.00) t
CROSS JOIN stadio st;

-- Posti: fila A-E, numeri 1-10 per ogni settore
INSERT INTO posto(fila,numero,settore_id)SELECT f.fila,n.numero,s.id FROM(SELECT 'A' fila UNION SELECT 'B' UNION SELECT 'C' UNION SELECT 'D' UNION SELECT 'E')f,(SELECT 1 numero UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10)n,(SELECT id FROM settore)s;

INSERT INTO squadra(nome,citta,home_stadium_id) VALUES
('Torino','Torino',(SELECT id FROM stadio WHERE nome='Stadio Olimpico Grande Torino')),
('Juventus','Torino',(SELECT id FROM stadio WHERE nome='Allianz Stadium')),
('AC Milan','Milano',(SELECT id FROM stadio WHERE nome='Stadio Giuseppe Meazza')),
('Inter','Milano',(SELECT id FROM stadio WHERE nome='Stadio Giuseppe Meazza')),
('AS Roma','Roma',(SELECT id FROM stadio WHERE nome='Stadio Olimpico')),
('Napoli','Napoli',(SELECT id FROM stadio WHERE nome='Stadio Diego Armando Maradona')),
('Atalanta','Bergamo',(SELECT id FROM stadio WHERE nome='Gewiss Stadium')),
('Fiorentina','Firenze',(SELECT id FROM stadio WHERE nome='Stadio Artemio Franchi')),
('Lazio','Roma',(SELECT id FROM stadio WHERE nome='Stadio Olimpico'));

INSERT INTO partita(squadra_casa_id,squadra_ospite_id,data_ora,stadio_id,stato)VALUES
(1,8,DATE_ADD(NOW(),INTERVAL 7 DAY),1,'PROGRAMMATA'),
(1,2,DATE_ADD(NOW(),INTERVAL 14 DAY),1,'PROGRAMMATA'),
(1,3,DATE_ADD(NOW(),INTERVAL 21 DAY),1,'PROGRAMMATA'),
(1,4,DATE_ADD(NOW(),INTERVAL 28 DAY),1,'PROGRAMMATA'),
(1,5,DATE_ADD(NOW(),INTERVAL 35 DAY),1,'PROGRAMMATA'),
(1,6,DATE_SUB(NOW(),INTERVAL 7 DAY),1,'CONCLUSA'),
(1,7,DATE_SUB(NOW(),INTERVAL 14 DAY),1,'CONCLUSA');

INSERT INTO promozione(codice,descrizione,sconto_percentuale,data_inizio,data_fine,partita_id)VALUES
('WELCOME10','Sconto benvenuto 10%',10.00,CURDATE(),DATE_ADD(CURDATE(),INTERVAL 365 DAY),NULL),
('DERBY20','Sconto derby Torino vs Fiorentina 20%',20.00,CURDATE(),DATE_ADD(CURDATE(),INTERVAL 8 DAY),1),
('ESTATE15','Sconto estivo 15%',15.00,CURDATE(),DATE_ADD(CURDATE(),INTERVAL 90 DAY),NULL),
('OLD50','Promo scaduta - test',50.00,DATE_SUB(CURDATE(),INTERVAL 30 DAY),DATE_SUB(CURDATE(),INTERVAL 1 DAY),NULL);

INSERT INTO abbonamento(utente_id,settore_id,stagione,data_inizio,data_fine,prezzo)VALUES(2,1,'2024/2025','2024-07-01','2025-06-30',300.00),(3,3,'2024/2025','2024-07-01','2025-06-30',500.00);

-- Biglietti demo (partita 1 = Torino vs Fiorentina, prossima)
INSERT INTO biglietto(partita_id,posto_id,utente_id,prezzo_pagato,data_acquisto,stato)VALUES(1,1,2,15.00,DATE_SUB(NOW(),INTERVAL 2 DAY),'VALIDO'),(1,2,3,12.00,DATE_SUB(NOW(),INTERVAL 1 DAY),'VALIDO'),(1,51,4,25.00,DATE_SUB(NOW(),INTERVAL 1 DAY),'VALIDO');
INSERT INTO pagamento(biglietto_id,metodo,importo,data,stato)VALUES(1,'CARTA',15.00,DATE_SUB(NOW(),INTERVAL 2 DAY),'COMPLETATO'),(2,'PAYPAL',12.00,DATE_SUB(NOW(),INTERVAL 1 DAY),'COMPLETATO'),(3,'CARTA',25.00,DATE_SUB(NOW(),INTERVAL 1 DAY),'COMPLETATO');

SELECT 'Setup completato!' AS messaggio;
SELECT COUNT(*) AS utenti FROM utente;
SELECT COUNT(*) AS posti FROM posto;
SELECT COUNT(*) AS partite FROM partita;
