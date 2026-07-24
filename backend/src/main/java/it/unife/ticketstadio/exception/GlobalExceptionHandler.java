package it.unife.ticketstadio.exception;

import org.springframework.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Gestore centralizzato delle eccezioni per tutti i controller (@RestControllerAdvice).
 * Ogni metodo intercetta un tipo di eccezione e la trasforma nella corretta risposta HTTP,
 * evitando di dover ripetere try/catch in ogni controller.
 * Il corpo della risposta è sempre un JSON del tipo {"error": "messaggio"}.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Risorsa non trovata -> 404 Not Found. */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> notFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    /** Posto già occupato -> 409 Conflict. */
    @ExceptionHandler(PostoOccupatoException.class)
    public ResponseEntity<Map<String, String>> postoOccupato(PostoOccupatoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", ex.getMessage()));
    }

    /** Argomento non valido (es. codice promo errato) -> 400 Bad Request. */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> badRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    /** Stato non valido per l'operazione (es. partita non acquistabile) -> 409 Conflict. */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> illegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", ex.getMessage()));
    }

    /** Autenticazione fallita (credenziali errate) -> 401 Unauthorized. */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> authFailed(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Credenziali non valide"));
    }

    /**
     * Errori di validazione dei dati in ingresso (annotazioni @Valid) -> 400 Bad Request.
     * Restituisce una mappa "nomeCampo -> messaggio di errore" per ogni campo non valido.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> validation(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ex.getBindingResult().getFieldErrors().stream()
                        .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
    }
}
