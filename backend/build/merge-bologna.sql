USE ticketstadio;
START TRANSACTION;

UPDATE partita SET squadra_casa_id=9   WHERE squadra_casa_id=2;
UPDATE partita SET squadra_ospite_id=9 WHERE squadra_ospite_id=2;

UPDATE promozione SET descrizione='Sconto derby Torino vs Fiorentina 20%' WHERE codice='DERBY20';

DELETE FROM squadra WHERE id=2;

COMMIT;

SELECT 'Partite che coinvolgono Bologna (atteso: 0)' AS check_msg, COUNT(*) AS qty FROM partita WHERE squadra_casa_id=2 OR squadra_ospite_id=2;
SELECT id, nome FROM squadra ORDER BY id;
SELECT p.id, casa.nome AS casa, osp.nome AS ospite, p.stato
FROM partita p JOIN squadra casa ON casa.id=p.squadra_casa_id JOIN squadra osp ON osp.id=p.squadra_ospite_id
ORDER BY p.id;
