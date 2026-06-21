package it.unife.ticketstadio.dto;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;
@Data public class AbbonamentoRequest {
    @NotNull private Long settoreId;
    @NotBlank private String stagione;
    @NotNull private LocalDate dataInizio;
    @NotNull private LocalDate dataFine;
}
