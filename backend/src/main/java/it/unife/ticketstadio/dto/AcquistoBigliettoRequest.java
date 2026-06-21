package it.unife.ticketstadio.dto;
import jakarta.validation.constraints.*;
import lombok.Data;
@Data public class AcquistoBigliettoRequest {
    @NotNull private Long partitaId;
    @NotNull private Long postoId;
    private String codicePromo;
    @NotBlank private String metodoPagamento;
}
