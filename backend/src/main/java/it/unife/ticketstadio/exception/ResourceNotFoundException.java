package it.unife.ticketstadio.exception;

/**
 * Eccezione lanciata quando una risorsa richiesta non esiste nel database
 * (es. utente, partita, biglietto...). Viene tradotta in una risposta HTTP 404
 * dal GlobalExceptionHandler.
 */
public class ResourceNotFoundException extends RuntimeException {

    /** @param r nome della risorsa non trovata (es. "Partita"); il messaggio diventa "Partita non trovato/a". */
    public ResourceNotFoundException(String r) {
        super(r + " non trovato/a");
    }
}
