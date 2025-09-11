package co.com.crediya.usecase.service;

import co.com.crediya.model.loan.Loan;
import co.com.crediya.model.loantype.LoanType;
import co.com.crediya.model.loantype.exceptions.LoanTypeNotFoundException;
import co.com.crediya.model.loantype.gateways.LoanTypeRepository;
import co.com.crediya.usecase.databuilder.LoanTestDataBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StandardLoanValidatorTest {

    @Mock
    private LoanTypeRepository loanTypeRepository;

    @InjectMocks
    private StandardLoanValidator validator;

    @Test
    void shouldPassValidationForValidLoan() {
        // Arrange
        var validLoan = new LoanTestDataBuilder().build();
        var loanType = LoanType.builder().idLoanType(1L).build();
        when(loanTypeRepository.findById(anyLong())).thenReturn(Mono.just(loanType));

        // Act
        Mono<Loan> result = validator.validate(validLoan);

        // Assert
        StepVerifier.create(result)
                .expectNext(validLoan)
                .verifyComplete();
    }

    @Test
    void shouldFailWhenLoanTypeIsNotFound() {
        // Arrange
        var validLoan = new LoanTestDataBuilder().build();
        when(loanTypeRepository.findById(anyLong())).thenReturn(Mono.empty()); // No se encuentra el tipo de préstamo

        // Act
        Mono<Loan> result = validator.validate(validLoan);

        // Assert
        StepVerifier.create(result)
                .expectError(LoanTypeNotFoundException.class)
                .verify();
    }
}