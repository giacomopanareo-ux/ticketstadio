package it.unife.ticketstadio.dto;

import it.unife.ticketstadio.entity.Biglietto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO in uscita con i dati di un biglietto "appiattiti" (partita, posto, prezzo...),
 * pronti da mostrare al frontend senza dover navigare le relazioni tra entità.
 */
@Data
public class BigliettoResponse {

    private Long id;
    private Long partitaId;
    private String squadraCasaNome, squadraOspiteNome;
    private LocalDateTime dataPartita;
    private String stadioNome, settoreNome, filaP, stato;
    private Integer numeroPosto;
    private BigDecimal prezzoPagato;
    private LocalDateTime dataAcquisto;

    /**
     * Crea il DTO a partire dall'entità Biglietto, estraendo i dati dalle entità collegate
     * (partita, squadre, stadio, posto, settore).
     */
    public static BigliettoResponse from(Biglietto b) {
        BigliettoResponse r = new BigliettoResponse();
        r.setId(b.getId());
        r.setPartitaId(b.getPartita().getId());
        r.setSquadraCasaNome(b.getPartita().getSquadraCasa().getNome());
        r.setSquadraOspiteNome(b.getPartita().getSquadraOspite().getNome());
        r.setDataPartita(b.getPartita().getDataOra());
        r.setStadioNome(b.getPartita().getStadio().getNome());
        r.setSettoreNome(b.getPosto().getSettore().getNome());
        r.setFilaP(b.getPosto().getFila());
        r.setNumeroPosto(b.getPosto().getNumero());
        r.setPrezzoPagato(b.getPrezzoPagato());
        r.setDataAcquisto(b.getDataAcquisto());
        r.setStato(b.getStato().name());
        return r;
    }
}
