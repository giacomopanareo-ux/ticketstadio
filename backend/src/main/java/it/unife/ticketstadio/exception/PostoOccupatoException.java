package it.unife.ticketstadio.exception;

/**
 * Eccezione lanciata quando si prova ad acquistare un posto già venduto per quella partita.
 * Viene tradotta in una risposta HTTP 409 (Conflict) dal GlobalExceptionHandler.
 */
public class PostoOccupatoException extends RuntimeException {

    public PostoOccupatoException(String m) {
        super(m);
    }
}
