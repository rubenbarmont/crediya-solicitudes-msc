package co.com.crediya.usecase.databuilder;

import java.math.BigDecimal;

public class LoanBuilder {

    private Long idLoanRequest;
    private Long identityDocument;
    private BigDecimal amount;
    private Long term;
    private Long idStatus;
    private Long idLoanType;

    public LoanBuilder() {
        this.identityDocument = 1037123456L;
        this.amount = new BigDecimal("5000000");
        this.term = 24L;
        this.idLoanType = 1L;
    }

    public LoanBuilder withIdLoanRequest(Long idLoanRequest) {
        this.idLoanRequest = idLoanRequest;
        return this;
    }

    public LoanBuilder withIdentityDocument(Long identityDocument) {
        this.identityDocument = identityDocument;
        return this;
    }

    public LoanBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public LoanBuilder withTerm(Long term) {
        this.term = term;
        return this;
    }

    public LoanBuilder withIdLoanType(Long idLoanType) {
        this.idLoanType = idLoanType;
        return this;
    }

    public LoanBuilder withIdStatus(Long idStatus) {
        this.idStatus = idStatus;
        return this;
    }

    public co.com.crediya.model.loan.Loan build() {
        return co.com.crediya.model.loan.Loan.builder()
                .idLoan(this.idLoanRequest)
                .identityDocument(this.identityDocument)
                .amount(this.amount)
                .term(this.term)
                .idStatus(this.idStatus)
                .idLoanType(this.idLoanType)
                .build();
    }
}