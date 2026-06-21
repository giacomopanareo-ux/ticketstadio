USE ticketstadio;
START TRANSACTION;

UPDATE stadio SET nome='Stadio Olimpico Grande Torino', citta='Torino', capienza_totale=28000 WHERE id=1;
UPDATE squadra SET nome='Torino', citta='Torino' WHERE id=1;

INSERT INTO stadio(nome,citta,capienza_totale) VALUES
  ('Stadio Renato Dall''Ara','Bologna',38279),
  ('Allianz Stadium','Torino',45000),
  ('Stadio Giuseppe Meazza','Milano',75923),
  ('Stadio Diego Armando Maradona','Napoli',54726),
  ('Gewiss Stadium','Bergamo',24950),
  ('Stadio Artemio Franchi','Firenze',43147),
  ('Stadio Olimpico','Roma',70634);

UPDATE squadra s JOIN stadio st ON st.nome='Stadio Olimpico Grande Torino'    SET s.home_stadium_id=st.id WHERE s.nome='Torino';
UPDATE squadra s JOIN stadio st ON st.nome='Stadio Renato Dall''Ara'           SET s.home_stadium_id=st.id WHERE s.nome='Bologna FC';
UPDATE squadra s JOIN stadio st ON st.nome='Allianz Stadium'                   SET s.home_stadium_id=st.id WHERE s.nome='Juventus';
UPDATE squadra s JOIN stadio st ON st.nome='Stadio Giuseppe Meazza'            SET s.home_stadium_id=st.id WHERE s.nome IN ('AC Milan','Inter');
UPDATE squadra s JOIN stadio st ON st.nome='Stadio Diego Armando Maradona'     SET s.home_stadium_id=st.id WHERE s.nome='Napoli';
UPDATE squadra s JOIN stadio st ON st.nome='Gewiss Stadium'                    SET s.home_stadium_id=st.id WHERE s.nome='Atalanta';
UPDATE squadra s JOIN stadio st ON st.nome='Stadio Artemio Franchi'            SET s.home_stadium_id=st.id WHERE s.nome='Fiorentina';
UPDATE squadra s JOIN stadio st ON st.nome='Stadio Olimpico'                   SET s.home_stadium_id=st.id WHERE s.nome IN ('AS Roma','Lazio');

UPDATE promozione SET descrizione='Sconto derby Torino vs Bologna 20%' WHERE codice='DERBY20';

COMMIT;

SELECT s.id, s.nome AS squadra, s.citta, st.nome AS stadio FROM squadra s LEFT JOIN stadio st ON s.home_stadium_id=st.id ORDER BY s.id;
