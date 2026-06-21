package it.unife.ticketstadio.exception;
import org.springframework.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.stream.Collectors;
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String,String>> notFound(ResourceNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error",ex.getMessage()));}
    @ExceptionHandler(PostoOccupatoException.class)
    public ResponseEntity<Map<String,String>> postoOccupato(PostoOccupatoException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error",ex.getMessage()));}
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String,String>> badRequest(IllegalArgumentException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error",ex.getMessage()));}
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String,String>> illegalState(IllegalStateException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error",ex.getMessage()));}
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String,String>> authFailed(AuthenticationException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","Credenziali non valide"));}
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> validation(MethodArgumentNotValidException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField,FieldError::getDefaultMessage)));}
}
