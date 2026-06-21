package it.unife.ticketstadio.dto;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;
@Data public class PartitaRequest {
    @NotNull private Long squadraCasaId;
    @NotNull private Long squadraOspiteId;
    @NotNull private LocalDateTime dataOra;
    @NotBlank private String stato;
}
