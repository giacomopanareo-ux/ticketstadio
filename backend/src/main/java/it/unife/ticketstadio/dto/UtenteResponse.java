package it.unife.ticketstadio.dto;

import it.unife.ticketstadio.entity.Utente;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO in uscita con i dati pubblici di un utente.
 * Serve a esporre solo i campi utili al frontend, senza mai includere
 * dati sensibili come l'hash della password.
 */
@Data
public class UtenteResponse {

    private Long id;
    private String nome, cognome, email, ruolo;
    private LocalDate dataRegistrazione;

    /** Crea il DTO a partire dall'entità Utente (mapping entità -> risposta). */
    public static UtenteResponse from(Utente u) {
        UtenteResponse r = new UtenteResponse();
        r.setId(u.getId());
        r.setNome(u.getNome());
        r.setCognome(u.getCognome());
        r.setEmail(u.getEmail());
        r.setRuolo(u.getRuolo().name());
        r.setDataRegistrazione(u.getDataRegistrazione());
        return r;
    }
}
