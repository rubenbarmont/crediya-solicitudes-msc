package co.com.crediya.usecase.databuilder;

import co.com.crediya.model.loantype.LoanType;
import java.math.BigDecimal;

public class LoanTypeBuilder {

    private Long idLoanType;
    private String name;

    public LoanTypeBuilder() {
        this.idLoanType = 1L;
        this.name = "Crédito de Libre Inversión";
    }

    public LoanTypeBuilder withIdLoanType(Long idLoanType) {
        this.idLoanType = idLoanType;
        return this;
    }

    public LoanType build() {
        return LoanType.builder()
                .idLoanType(this.idLoanType)
                .name(this.name)
                .minAmount(new BigDecimal("1000000"))
                .maxAmount(new BigDecimal("100000000"))
                .interestRate(new BigDecimal("1.5"))
                .automaticValidation(true)
                .build();
    }
}
