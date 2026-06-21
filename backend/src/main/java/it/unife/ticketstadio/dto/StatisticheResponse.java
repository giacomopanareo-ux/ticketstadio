package it.unife.ticketstadio.dto;
import lombok.Data;
import java.util.List;
@Data public class StatisticheResponse {
    private long totaleBiglietti;
    private double incassoTotale;
    private long abbonamenti,partiteProgrammate;
    private List<VenditaPartitaDto> perPartita;
    @Data public static class VenditaPartitaDto {
        private Long partitaId;
        private String squadraCasa,squadraOspite,dataOra,stato;
        private long bigliettiVenduti;
        private double incasso;
    }
}
