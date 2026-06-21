package it.unife.ticketstadio.dto;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
@Data public class SettoreRequest {
    @NotBlank private String nome;
    @NotNull @Min(1) private Integer capienza;
    @NotNull @DecimalMin("0.0") private BigDecimal prezzoBase;
    @NotNull private Long stadioId;
}
