package it.unife.ticketstadio.dto;

import it.unife.ticketstadio.entity.Settore;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO in uscita con i dati di un settore, arricchito con info sullo stadio
 * e sulla squadra che vi gioca.
 */
@Data
public class SettoreResponse {

    private Long id;
    private String nome;
    private Integer capienza;
    private BigDecimal prezzoBase;
    private Long stadioId;
    private String stadioNome;
    private String citta;
    private String squadraNome;

    /**
     * Crea il DTO a partire dall'entità Settore.
     * @param squadraNome nome della squadra associata allo stadio, calcolato dal service.
     */
    public static SettoreResponse from(Settore s, String squadraNome) {
        SettoreResponse r = new SettoreResponse();
        r.setId(s.getId());
        r.setNome(s.getNome());
        r.setCapienza(s.getCapienza());
        r.setPrezzoBase(s.getPrezzoBase());

        // Lo stadio può essere assente: copio i suoi dati solo se presente.
        if (s.getStadio() != null) {
            r.setStadioId(s.getStadio().getId());
            r.setStadioNome(s.getStadio().getNome());
            r.setCitta(s.getStadio().getCitta());
        }
        r.setSquadraNome(squadraNome);
        return r;
    }
}
