package it.unife.ticketstadio.dto;
import it.unife.ticketstadio.entity.Partita;
import lombok.Data;
import java.time.LocalDateTime;
@Data public class PartitaResponse {
    private Long id;
    private Long squadraCasaId,squadraOspiteId,stadioId;
    private String squadraCasaNome,squadraOspiteNome,stadioNome,stato;
    private LocalDateTime dataOra;
    private Double prezzoMinimo;
    public static PartitaResponse from(Partita p,Double pm){
        PartitaResponse r=new PartitaResponse();
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
