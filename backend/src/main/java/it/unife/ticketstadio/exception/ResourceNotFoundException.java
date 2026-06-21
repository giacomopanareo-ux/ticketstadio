package it.unife.ticketstadio.exception;
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String r){super(r+" non trovato/a");}
}
