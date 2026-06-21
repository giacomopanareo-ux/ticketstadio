package it.unife.ticketstadio.dto;
import it.unife.ticketstadio.entity.Utente;
import lombok.Data;
import java.time.LocalDate;
@Data public class UtenteResponse {
    private Long id;
    private String nome,cognome,email,ruolo;
    private LocalDate dataRegistrazione;
    public static UtenteResponse from(Utente u){
        UtenteResponse r=new UtenteResponse();
        r.setId(u.getId());r.setNome(u.getNome());r.setCognome(u.getCognome());
        r.setEmail(u.getEmail());r.setRuolo(u.getRuolo().name());
        r.setDataRegistrazione(u.getDataRegistrazione());
        return r;
    }
}
