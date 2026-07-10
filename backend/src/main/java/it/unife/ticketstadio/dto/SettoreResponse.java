package it.unife.ticketstadio.dto;
import it.unife.ticketstadio.entity.Settore;
import lombok.Data;
import java.math.BigDecimal;
@Data public class SettoreResponse {
    private Long id;
    private String nome;
    private Integer capienza;
    private BigDecimal prezzoBase;
    private Long stadioId;
    private String stadioNome;
    private String citta;
    private String squadraNome;
    public static SettoreResponse from(Settore s,String squadraNome){
        SettoreResponse r=new SettoreResponse();
        r.setId(s.getId());
        r.setNome(s.getNome());
        r.setCapienza(s.getCapienza());
        r.setPrezzoBase(s.getPrezzoBase());
        if(s.getStadio()!=null){
            r.setStadioId(s.getStadio().getId());
            r.setStadioNome(s.getStadio().getNome());
            r.setCitta(s.getStadio().getCitta());
        }
        r.setSquadraNome(squadraNome);
        return r;
    }
}
