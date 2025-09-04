package co.com.crediya.model.loan;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class LoanTest {

    @Test
    void shouldUseLombokBuilder() {
        Loan loan = Loan.builder()
                .idLoan(1L)
                .identityDocument(123L)
                .amount(BigDecimal.TEN)
                .term(12L)
                .idStatus(5L)
                .idLoanType(2L)
                .build();

        assertEquals(1L, loan.getIdLoan());
        assertEquals(123L, loan.getIdentityDocument());
        assertEquals(BigDecimal.TEN, loan.getAmount());
        assertEquals(12L, loan.getTerm());
        assertEquals(5L, loan.getIdStatus());
        assertEquals(2L, loan.getIdLoanType());

        loan.setAmount(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, loan.getAmount());
    }
}
