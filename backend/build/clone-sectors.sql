USE ticketstadio;
START TRANSACTION;

INSERT INTO settore (nome, capienza, prezzo_base, stadio_id)
SELECT s.nome, s.capienza, s.prezzo_base, st.id
FROM settore s
CROSS JOIN stadio st
WHERE s.stadio_id = 1 AND st.id <> 1;

INSERT INTO posto (fila, numero, settore_id)
SELECT f.fila, n.numero, s.id
FROM settore s
CROSS JOIN (SELECT 'A' fila UNION SELECT 'B' UNION SELECT 'C' UNION SELECT 'D' UNION SELECT 'E') f
CROSS JOIN (SELECT 1 numero UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) n
WHERE s.stadio_id <> 1;

COMMIT;

SELECT st.id AS stadio_id, st.nome AS stadio,
       COUNT(DISTINCT s.id) AS settori,
       COUNT(p.id) AS posti
FROM stadio st
LEFT JOIN settore s ON s.stadio_id = st.id
LEFT JOIN posto p ON p.settore_id = s.id
GROUP BY st.id, st.nome
ORDER BY st.id;
