package co.com.crediya.usecase.databuilder;

import co.com.crediya.model.loan.Loan;
import java.math.BigDecimal;

public class LoanTestDataBuilder {
    private Long idLoan;
    private Long identityDocument;
    private BigDecimal amount;
    private Long term;
    private Long idLoanType;

    public LoanTestDataBuilder() {
        this.idLoan = 1L;
        this.identityDocument = 12345L;
        this.amount = new BigDecimal("500000");
        this.term = 12L;
        this.idLoanType = 1L;
    }

    public LoanTestDataBuilder withIdentityDocument(Long identityDocument) {
        this.identityDocument = identityDocument;
        return this;
    }

    public Loan build() {
        return Loan.builder()
                .idLoan(idLoan)
                .identityDocument(identityDocument)
                .amount(amount)
                .term(term)
                .idLoanType(idLoanType)
                .build();
    }
}
