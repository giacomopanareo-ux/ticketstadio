package it.unife.ticketstadio.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO in ingresso per sottoscrivere un abbonamento.
 */
@Data
public class AbbonamentoRequest {

    @NotNull
    private Long settoreId;

    @NotBlank
    private String stagione; // es. "2024/2025"

    @NotNull
    private LocalDate dataInizio;

    @NotNull
    private LocalDate dataFine;
}
