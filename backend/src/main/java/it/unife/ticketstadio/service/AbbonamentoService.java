package it.unife.ticketstadio.service;

import it.unife.ticketstadio.dto.AbbonamentoRequest;
import it.unife.ticketstadio.entity.*;
import it.unife.ticketstadio.exception.ResourceNotFoundException;
import it.unife.ticketstadio.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service che gestisce gli abbonamenti stagionali:
 * sottoscrizione, rinnovo e recupero degli abbonamenti dell'utente.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AbbonamentoService {

    private final AbbonamentoRepository abbRepo;
    private final SettoreRepository settoreRepo;
    private final UtenteRepository utenteRepo;

    /**
     * Sottoscrive un nuovo abbonamento per un utente in un dato settore/stagione.
     */
    public Abbonamento sottoscrivi(AbbonamentoRequest req, String email) {
        // Recupero utente e settore; se non esistono lancio 404.
        Utente u = utenteRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utente"));
        Settore s = settoreRepo.findById(req.getSettoreId())
                .orElseThrow(() -> new ResourceNotFoundException("Settore"));

        // Un utente non può avere due abbonamenti nello stesso settore e stagione.
        if (abbRepo.existsByUtenteAndSettoreAndStagione(u, s, req.getStagione())) {
            throw new IllegalStateException("Abbonamento già attivo per questa stagione e settore");
        }

        // Prezzo abbonamento = prezzo base del settore * 20 (equivalente di ~20 partite).
        BigDecimal prezzo = s.getPrezzoBase().multiply(BigDecimal.valueOf(20));

        return abbRepo.save(
                Abbonamento.builder()
                        .utente(u)
                        .settore(s)
                        .stagione(req.getStagione())
                        .dataInizio(req.getDataInizio())
                        .dataFine(req.getDataFine())
                        .prezzo(prezzo)
                        .build());
    }

    /**
     * Rinnova un abbonamento esistente creando quello della stagione successiva.
     */
    public Abbonamento rinnova(Long id, String email) {
        Abbonamento v = abbRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Abbonamento"));

        // Controllo di sicurezza: solo il proprietario può rinnovare.
        if (!v.getUtente().getEmail().equals(email)) {
            throw new SecurityException("Non autorizzato");
        }

        // Calcolo la stagione successiva a partire dalla stagione attuale (es. "2024/2025" -> "2025/2026").
        String[] p = v.getStagione().split("/");
        int annoFine = Integer.parseInt(p[1]);
        String nuovaStagione = annoFine + "/" + (annoFine + 1);

        // Evito rinnovi doppi per la stessa stagione.
        if (abbRepo.existsByUtenteAndSettoreAndStagione(v.getUtente(), v.getSettore(), nuovaStagione)) {
            throw new IllegalStateException("Rinnovo già effettuato");
        }

        return abbRepo.save(
                Abbonamento.builder()
                        .utente(v.getUtente())
                        .settore(v.getSettore())
                        .stagione(nuovaStagione)
                        .dataInizio(v.getDataFine().plusDays(1))   // parte il giorno dopo la scadenza del vecchio
                        .dataFine(v.getDataFine().plusYears(1))    // dura un altro anno
                        .prezzo(v.getPrezzo())                     // stesso prezzo del precedente
                        .build());
    }

    /**
     * Restituisce tutti gli abbonamenti dell'utente indicato.
     */
    @Transactional(readOnly = true)
    public List<Abbonamento> getMiei(String email) {
        return abbRepo.findByUtenteEmail(email);
    }
}
