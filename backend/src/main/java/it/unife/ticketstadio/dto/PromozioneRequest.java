package it.unife.ticketstadio.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO in ingresso per creare una promozione.
 */
@Data
public class PromozioneRequest {

    @NotBlank
    private String codice;

    private String descrizione;

    /** Percentuale di sconto compresa tra 1 e 100. */
    @NotNull
    @DecimalMin("1")
    @DecimalMax("100")
    private BigDecimal scontoPercentuale;

    @NotNull
    private LocalDate dataInizio;

    @NotNull
    private LocalDate dataFine;

    /** Partita a cui legare la promo (opzionale): se null, la promo è generica. */
    private Long partitaId;
}
