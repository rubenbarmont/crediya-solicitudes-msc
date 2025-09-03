package co.com.crediya.model.loan;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Loan {
    private Long idLoan;
    private Long identityDocument; // <-- CAMPO AÑADIDO
    private BigDecimal amount;
    private Long term;
    private Long idStatus;
    private Long idLoanType;
}
