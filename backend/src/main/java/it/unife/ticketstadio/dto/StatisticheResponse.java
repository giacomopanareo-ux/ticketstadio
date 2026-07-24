package it.unife.ticketstadio.dto;

import lombok.Data;

import java.util.List;

/**
 * DTO in uscita con le statistiche di vendita mostrate nell'area amministrativa.
 * Contiene i totali complessivi e, tramite la classe interna VenditaPartitaDto,
 * il dettaglio partita per partita.
 */
@Data
public class StatisticheResponse {

    private long totaleBiglietti;
    private double incassoTotale;
    private long abbonamenti, partiteProgrammate;
    private List<VenditaPartitaDto> perPartita;

    /** Dettaglio di vendita relativo a una singola partita. */
    @Data
    public static class VenditaPartitaDto {
        private Long partitaId;
        private String squadraCasa, squadraOspite, dataOra, stato;
        private long bigliettiVenduti;
        private double incasso;
    }
}
