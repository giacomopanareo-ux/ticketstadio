package it.unife.ticketstadio.service;

import it.unife.ticketstadio.dto.*;
import it.unife.ticketstadio.entity.*;
import it.unife.ticketstadio.exception.ResourceNotFoundException;
import it.unife.ticketstadio.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Service che gestisce le promozioni (codici sconto):
 * elenco, creazione, disattivazione e validazione.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PromozioneService {

    private final PromozioneRepository promoRepo;
    private final PartitaRepository partitaRepo;

    /** Restituisce tutte le promozioni presenti. */
    @Transactional(readOnly = true)
    public List<Promozione> getAll() {
        return promoRepo.findAll();
    }

    /**
     * Crea una nuova promozione.
     */
    public Promozione crea(PromozioneRequest req) {
        // Il codice promo deve essere univoco.
        if (promoRepo.findByCodice(req.getCodice()).isPresent()) {
            throw new IllegalArgumentException("Codice già esistente");
        }
        // La data di fine non può precedere quella di inizio.
        if (req.getDataFine().isBefore(req.getDataInizio())) {
            throw new IllegalArgumentException("Data fine precedente a data inizio");
        }

        // La promo può essere legata a una partita specifica (opzionale): se manca resta null.
        Partita partita = req.getPartitaId() != null
                ? partitaRepo.findById(req.getPartitaId())
                        .orElseThrow(() -> new ResourceNotFoundException("Partita"))
                : null;

        return promoRepo.save(
                Promozione.builder()
                        .codice(req.getCodice().toUpperCase()) // codice sempre in maiuscolo
                        .descrizione(req.getDescrizione())
                        .scontoPercentuale(req.getScontoPercentuale())
                        .dataInizio(req.getDataInizio())
                        .dataFine(req.getDataFine())
                        .partita(partita)
                        .build());
    }

    /**
     * Disattiva una promozione impostando la data di fine a ieri,
     * così da renderla immediatamente scaduta.
     */
    public void disattiva(Long id) {
        Promozione p = promoRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promozione"));
        p.setDataFine(LocalDate.now().minusDays(1));
        promoRepo.save(p);
    }

    /**
     * Verifica che un codice promo sia valido e applicabile a una data partita.
     * Usato prima dell'acquisto per mostrare all'utente lo sconto disponibile.
     */
    @Transactional(readOnly = true)
    public Promozione valida(ValidaPromoRequest req) {
        Promozione p = promoRepo.findByCodice(req.getCodice().toUpperCase())
                .orElseThrow(() -> new IllegalArgumentException("Codice promo non valido"));
        Partita pt = partitaRepo.findById(req.getPartitaId())
                .orElseThrow(() -> new ResourceNotFoundException("Partita"));

        if (!p.isValida(pt)) {
            throw new IllegalArgumentException("Promozione non applicabile a questa partita");
        }
        return p;
    }
}
