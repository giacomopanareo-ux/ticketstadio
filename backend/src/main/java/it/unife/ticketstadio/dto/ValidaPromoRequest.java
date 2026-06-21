package it.unife.ticketstadio.dto;
import jakarta.validation.constraints.*;
import lombok.Data;
@Data public class ValidaPromoRequest {
    @NotBlank private String codice;
    @NotNull private Long partitaId;
}
