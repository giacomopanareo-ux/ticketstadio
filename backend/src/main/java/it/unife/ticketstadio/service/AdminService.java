package it.unife.ticketstadio.service;

import it.unife.ticketstadio.dto.StatisticheResponse;
import it.unife.ticketstadio.entity.*;
import it.unife.ticketstadio.exception.ResourceNotFoundException;
import it.unife.ticketstadio.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service usato dall'area amministrativa per calcolare le statistiche di vendita
 * (biglietti venduti, incassi, abbonamenti, dettaglio per partita).
 */
@Service
@RequiredArgsConstructor
public class AdminService {

    private final BigliettoRepository bigliettoRepo;
    private final AbbonamentoRepository abbRepo;
    private final PartitaRepository partitaRepo;

    /**
     * Calcola le statistiche complessive di vendita + il dettaglio partita per partita.
     */
    @Transactional(readOnly = true)
    public StatisticheResponse getStatisticheVendite() {
        StatisticheResponse r = new StatisticheResponse();

        // Numeri aggregati sull'intero sistema.
        r.setTotaleBiglietti(bigliettoRepo.countValidi());
        r.setIncassoTotale(bigliettoRepo.sumIncasso());
        r.setAbbonamenti(abbRepo.count());
        r.setPartiteProgrammate(
                partitaRepo.findByStatoOrderByDataOraAsc(Partita.Stato.PROGRAMMATA).size());

        // Dettaglio: per ogni partita costruisco un DTO con venduti e incasso.
        r.setPerPartita(
                partitaRepo.findAll().stream()
                        .map(this::buildVenditaPartitaDto)
                        .toList());

        return r;
    }

    /**
     * Statistiche di vendita per una singola partita.
     */
    @Transactional(readOnly = true)
    public StatisticheResponse.VenditaPartitaDto getStatistichePartita(Long id) {
        Partita p = partitaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partita"));
        return buildVenditaPartitaDto(p);
    }

    /**
     * Metodo di supporto: costruisce il DTO con i dati di vendita di una partita.
     * (Estratto per evitare di ripetere lo stesso codice nei due metodi sopra.)
     */
    private StatisticheResponse.VenditaPartitaDto buildVenditaPartitaDto(Partita p) {
        var d = new StatisticheResponse.VenditaPartitaDto();

        // Dati anagrafici della partita.
        d.setPartitaId(p.getId());
        d.setSquadraCasa(p.getSquadraCasa().getNome());
        d.setSquadraOspite(p.getSquadraOspite().getNome());
        d.setDataOra(p.getDataOra().toString());
        d.setStato(p.getStato().name());

        // Considero solo i biglietti VALIDI (esclusi annullati/rimborsati).
        var biglietti = bigliettoRepo.findAllByPartitaId(p.getId());
        d.setBigliettiVenduti(
                biglietti.stream()
                        .filter(b -> b.getStato() == Biglietto.Stato.VALIDO)
                        .count());
        d.setIncasso(
                biglietti.stream()
                        .filter(b -> b.getStato() == Biglietto.Stato.VALIDO)
                        .mapToDouble(b -> b.getPrezzoPagato().doubleValue())
                        .sum());

        return d;
    }
}
