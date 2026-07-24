package it.unife.ticketstadio.dto;

import it.unife.ticketstadio.entity.Partita;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO in uscita con i dati di una partita, incluso il prezzo minimo del biglietto.
 */
@Data
public class PartitaResponse {

    private Long id;
    private Long squadraCasaId, squadraOspiteId, stadioId;
    private String squadraCasaNome, squadraOspiteNome, stadioNome, stato;
    private LocalDateTime dataOra;
    private Double prezzoMinimo;

    /**
     * Crea il DTO a partire dall'entità Partita.
     * @param pm prezzo minimo calcolato a parte dal service (prezzo "a partire da...").
     */
    public static PartitaResponse from(Partita p, Double pm) {
        PartitaResponse r = new PartitaResponse();
        r.setId(p.getId());
        r.setSquadraCasaId(p.getSquadraCasa().getId());
        r.setSquadraCasaNome(p.getSquadraCasa().getNome());
        r.setSquadraOspiteId(p.getSquadraOspite().getId());
        r.setSquadraOspiteNome(p.getSquadraOspite().getNome());
        r.setDataOra(p.getDataOra());
        r.setStadioId(p.getStadio().getId());
        r.setStadioNome(p.getStadio().getNome());
        r.setStato(p.getStato().name());
        r.setPrezzoMinimo(pm);
        return r;
    }
}
