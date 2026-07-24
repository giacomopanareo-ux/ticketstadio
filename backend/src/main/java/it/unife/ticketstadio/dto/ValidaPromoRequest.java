package it.unife.ticketstadio.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * DTO in ingresso per verificare la validità di un codice promo su una partita.
 */
@Data
public class ValidaPromoRequest {

    @NotBlank
    private String codice;

    @NotNull
    private Long partitaId;
}
