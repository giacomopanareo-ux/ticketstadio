package it.unife.ticketstadio.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * DTO in ingresso per l'acquisto di un biglietto.
 */
@Data
public class AcquistoBigliettoRequest {

    @NotNull
    private Long partitaId;

    @NotNull
    private Long postoId;

    /** Codice promozione opzionale: se assente, si paga il prezzo pieno. */
    private String codicePromo;

    @NotBlank
    private String metodoPagamento; // "CARTA", "PAYPAL" o "BONIFICO"
}
