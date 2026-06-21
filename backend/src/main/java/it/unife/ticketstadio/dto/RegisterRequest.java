package it.unife.ticketstadio.dto;
import jakarta.validation.constraints.*;
import lombok.Data;
@Data public class RegisterRequest {
    @NotBlank private String nome;
    @NotBlank private String cognome;
    @NotBlank @Email private String email;
    @NotBlank @Size(min=6) private String password;
}
