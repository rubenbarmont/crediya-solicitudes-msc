package co.com.crediya.api.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoanRequestDTO {
    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser positivo")
    private BigDecimal amount;

    @NotNull(message = "El plazo es obligatorio")
    @Min(value = 1, message = "El plazo mínimo es de 1 mes")
    private Long term;

    @NotBlank(message = "El email del solicitante es obligatorio")
    @Email(message = "El formato del email no es válido")
    private String email;

    @NotNull(message = "El tipo de préstamo es obligatorio")
    private Long idLoanType;
}