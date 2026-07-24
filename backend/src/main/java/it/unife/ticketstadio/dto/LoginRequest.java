package it.unife.ticketstadio.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * DTO in ingresso per il login.
 */
@Data
public class LoginRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
