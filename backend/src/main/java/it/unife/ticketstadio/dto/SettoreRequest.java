package it.unife.ticketstadio.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO in ingresso per creare o aggiornare un settore.
 */
@Data
public class SettoreRequest {

    @NotBlank
    private String nome;

    @NotNull
    @Min(1) // almeno 1 posto
    private Integer capienza;

    @NotNull
    @DecimalMin("0.0") // prezzo non negativo
    private BigDecimal prezzoBase;

    @NotNull
    private Long stadioId;
}
