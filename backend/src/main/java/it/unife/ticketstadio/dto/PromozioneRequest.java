package it.unife.ticketstadio.dto;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
@Data public class PromozioneRequest {
    @NotBlank private String codice;
    private String descrizione;
    @NotNull @DecimalMin("1") @DecimalMax("100") private BigDecimal scontoPercentuale;
    @NotNull private LocalDate dataInizio;
    @NotNull private LocalDate dataFine;
    private Long partitaId;
}
