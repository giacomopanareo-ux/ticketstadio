package it.unife.ticketstadio.service;

import it.unife.ticketstadio.dto.*;
import it.unife.ticketstadio.entity.*;
import it.unife.ticketstadio.exception.*;
import it.unife.ticketstadio.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service che gestisce la logica di business dei biglietti:
 * acquisto, recupero dei biglietti dell'utente e dettaglio singolo.
 *
 * - @Service        -> Spring registra la classe come componente di servizio.
 * - @RequiredArgsConstructor -> Lombok genera il costruttore con tutti i campi
 *                               "final" (usato per la dependency injection).
 * - @Transactional  -> ogni metodo pubblico gira dentro una transazione DB:
 *                      se viene lanciata un'eccezione tutto viene annullato (rollback).
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BigliettoService {

    // Repository iniettati da Spring: sono i "ponti" verso il database.
    private final BigliettoRepository bigliettoRepo;
    private final PartitaRepository partitaRepo;
    private final PostoRepository postoRepo;
    private final PromozioneRepository promoRepo;
    private final PagamentoRepository pagamentoRepo;
    private final UtenteRepository utenteRepo;

    /**
     * Acquista un biglietto per una partita e un posto specifici.
     *
     * @param req   dati dell'acquisto (id partita, id posto, metodo pagamento, eventuale promo)
     * @param email email dell'utente che sta acquistando (di solito preso dal token di login)
     * @return      il biglietto creato, in formato DTO di risposta
     */
    public BigliettoResponse acquista(AcquistoBigliettoRequest req, String email) {

        // 1) Recupero le entità coinvolte; se una non esiste lancio 404 (ResourceNotFoundException).
        Utente utente = utenteRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utente"));

        Partita partita = partitaRepo.findById(req.getPartitaId())
                .orElseThrow(() -> new ResourceNotFoundException("Partita"));

        Posto posto = postoRepo.findById(req.getPostoId())
                .orElseThrow(() -> new ResourceNotFoundException("Posto"));

        // 2) Controlli di validità: la partita deve essere ancora acquistabile
        //    e il posto non deve essere già stato venduto per quella partita.
        if (!partita.isAcquistabile()) {
            throw new IllegalStateException("Partita non disponibile per l'acquisto");
        }
        if (bigliettoRepo.existsByPartitaAndPosto(partita, posto)) {
            throw new PostoOccupatoException("Posto già occupato per questa partita");
        }

        // 3) Prezzo di partenza = prezzo base del settore in cui si trova il posto.
        BigDecimal prezzo = posto.getSettore().getPrezzoBase();

        // 4) Se è stato inserito un codice promo valido, applico lo sconto percentuale.
        if (req.getCodicePromo() != null && !req.getCodicePromo().isBlank()) {

            Promozione promo = promoRepo.findByCodice(req.getCodicePromo())
                    .orElseThrow(() -> new IllegalArgumentException("Codice promo non valido"));

            // La promo può avere vincoli (es. valida solo per certe partite/date).
            if (!promo.isValida(partita)) {
                throw new IllegalArgumentException("Promozione non applicabile");
            }

            // prezzo = prezzo * (1 - sconto/100), arrotondato a 2 decimali.
            BigDecimal fattoreSconto = BigDecimal.ONE
                    .subtract(promo.getScontoPercentuale().divide(BigDecimal.valueOf(100)));
            prezzo = prezzo.multiply(fattoreSconto).setScale(2, RoundingMode.HALF_UP);
        }

        // 5) Creo e salvo il biglietto sul database.
        Biglietto b = bigliettoRepo.save(
                Biglietto.builder()
                        .partita(partita)
                        .posto(posto)
                        .utente(utente)
                        .prezzoPagato(prezzo)
                        .dataAcquisto(LocalDateTime.now())
                        .build());

        // 6) Registro il pagamento collegato al biglietto (stato COMPLETATO = ok).
        pagamentoRepo.save(
                Pagamento.builder()
                        .biglietto(b)
                        .metodo(Pagamento.Metodo.valueOf(req.getMetodoPagamento()))
                        .importo(prezzo)
                        .data(LocalDateTime.now())
                        .stato(Pagamento.Stato.COMPLETATO)
                        .build());

        // 7) Restituisco il DTO di risposta (non l'entità, per non esporre il modello interno).
        return BigliettoResponse.from(b);
    }

    /**
     * Restituisce tutti i biglietti acquistati dall'utente indicato.
     * readOnly = true -> ottimizzazione: la transazione è di sola lettura.
     */
    @Transactional(readOnly = true)
    public List<BigliettoResponse> getMiei(String email) {
        return bigliettoRepo.findByUtenteEmail(email).stream()
                .map(BigliettoResponse::from) // converto ogni entità in DTO
                .toList();
    }

    /**
     * Restituisce il dettaglio di un singolo biglietto.
     * Verifica anche che il biglietto appartenga davvero all'utente richiedente.
     */
    @Transactional(readOnly = true)
    public BigliettoResponse getById(Long id, String email) {

        Biglietto b = bigliettoRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Biglietto"));

        // Controllo di sicurezza: un utente non può vedere i biglietti di un altro.
        if (!b.getUtente().getEmail().equals(email)) {
            throw new SecurityException("Non autorizzato");
        }

        return BigliettoResponse.from(b);
    }
}
