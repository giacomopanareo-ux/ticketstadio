package it.unife.ticketstadio.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * DTO in ingresso per la registrazione di un nuovo utente.
 * Le annotazioni di validazione (@NotBlank, @Email, @Size) vengono controllate
 * automaticamente quando il controller riceve la richiesta con @Valid.
 */
@Data
public class RegisterRequest {

    @NotBlank
    private String nome;

    @NotBlank
    private String cognome;

    @NotBlank
    @Email // deve avere il formato di un'email valida
    private String email;

    @NotBlank
    @Size(min = 6) // almeno 6 caratteri
    private String password;
}
