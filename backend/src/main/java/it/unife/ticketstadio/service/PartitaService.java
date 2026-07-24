package it.unife.ticketstadio.service;

import it.unife.ticketstadio.dto.*;
import it.unife.ticketstadio.entity.*;
import it.unife.ticketstadio.exception.ResourceNotFoundException;
import it.unife.ticketstadio.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service che gestisce le partite: elenco, dettaglio, creazione,
 * aggiornamento e annullamento.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PartitaService {

    private final PartitaRepository partitaRepo;
    private final SquadraRepository squadraRepo;
    private final StadioRepository stadioRepo;
    private final SettoreRepository settoreRepo;
    private final BigliettoRepository bigliettoRepo;

    /** Elenco di tutte le partite, ciascuna con il prezzo minimo del biglietto. */
    @Transactional(readOnly = true)
    public List<PartitaResponse> getAll() {
        return partitaRepo.findAll().stream()
                .map(p -> PartitaResponse.from(p, getPrezzoMin(p)))
                .toList();
    }

    /** Dettaglio di una singola partita. */
    @Transactional(readOnly = true)
    public PartitaResponse getById(Long id) {
        Partita p = partitaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partita"));
        return PartitaResponse.from(p, getPrezzoMin(p));
    }

    /**
     * Crea una nuova partita tra due squadre.
     */
    public PartitaResponse crea(PartitaRequest req) {
        // Una squadra non può giocare contro sé stessa.
        if (req.getSquadraCasaId().equals(req.getSquadraOspiteId())) {
            throw new IllegalArgumentException("Le due squadre non possono essere uguali");
        }

        Squadra casa = squadraRepo.findById(req.getSquadraCasaId())
                .orElseThrow(() -> new ResourceNotFoundException("Squadra casa"));
        Squadra ospite = squadraRepo.findById(req.getSquadraOspiteId())
                .orElseThrow(() -> new ResourceNotFoundException("Squadra ospite"));

        // Lo stadio è quello di casa della squadra ospitante; se non definito, prendo il primo disponibile.
        Stadio stadio = casa.getHomeStadium() != null
                ? casa.getHomeStadium()
                : stadioRepo.findAll().stream().findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("Stadio"));

        Partita p = partitaRepo.save(
                Partita.builder()
                        .squadraCasa(casa)
                        .squadraOspite(ospite)
                        .dataOra(req.getDataOra())
                        .stadio(stadio)
                        .stato(Partita.Stato.valueOf(req.getStato()))
                        .build());

        return PartitaResponse.from(p, getPrezzoMin(p));
    }

    /**
     * Aggiorna una partita esistente (squadre, stadio, data/ora, stato).
     */
    public PartitaResponse aggiorna(Long id, PartitaRequest req) {
        Partita p = partitaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partita"));

        if (req.getSquadraCasaId().equals(req.getSquadraOspiteId())) {
            throw new IllegalArgumentException("Le due squadre non possono essere uguali");
        }

        Squadra newCasa = squadraRepo.findById(req.getSquadraCasaId())
                .orElseThrow(() -> new ResourceNotFoundException("Squadra"));
        p.setSquadraCasa(newCasa);
        p.setSquadraOspite(squadraRepo.findById(req.getSquadraOspiteId())
                .orElseThrow(() -> new ResourceNotFoundException("Squadra")));

        // Se la nuova squadra di casa ha uno stadio proprio, aggiorno anche lo stadio.
        if (newCasa.getHomeStadium() != null) {
            p.setStadio(newCasa.getHomeStadium());
        }
        p.setDataOra(req.getDataOra());
        p.setStato(Partita.Stato.valueOf(req.getStato()));

        return PartitaResponse.from(partitaRepo.save(p), getPrezzoMin(p));
    }

    /**
     * Annulla una partita e, a cascata, annulla tutti i biglietti venduti per essa.
     */
    public void annulla(Long id) {
        Partita p = partitaRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partita"));
        p.setStato(Partita.Stato.ANNULLATA);
        partitaRepo.save(p);

        // Ogni biglietto della partita passa allo stato ANNULLATO.
        bigliettoRepo.findAllByPartitaId(id).forEach(b -> {
            b.setStato(Biglietto.Stato.ANNULLATO);
            bigliettoRepo.save(b);
        });
    }

    /**
     * Metodo di supporto: calcola il prezzo minimo tra i settori dello stadio della partita.
     * Serve a mostrare il "prezzo a partire da..." nella lista partite.
     */
    private Double getPrezzoMin(Partita p) {
        return settoreRepo.findAll().stream()
                .filter(s -> s.getStadio().getId().equals(p.getStadio().getId()))
                .mapToDouble(s -> s.getPrezzoBase().doubleValue())
                .min()
                .orElse(0.0); // nessun settore -> 0
    }
}
