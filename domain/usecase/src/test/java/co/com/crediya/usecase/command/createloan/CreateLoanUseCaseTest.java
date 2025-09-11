package co.com.crediya.usecase.command.createloan;

import co.com.crediya.model.loan.Loan;
import co.com.crediya.model.loan.exceptions.LoanCreationForbiddenException;
import co.com.crediya.model.loan.gateways.LoanRepository;
import co.com.crediya.model.status.Status;
import co.com.crediya.model.status.gateways.StatusRepository;
import co.com.crediya.usecase.databuilder.LoanTestDataBuilder;
import co.com.crediya.usecase.databuilder.UserTestDataBuilder;
import co.com.crediya.usecase.gateways.UserGateway;
import co.com.crediya.usecase.service.LoanValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateLoanUseCaseTest {

    @Mock
    private LoanValidator validator;
    @Mock
    private LoanRepository loanRepository;
    @Mock
    private StatusRepository statusRepository;
    @Mock
    private UserGateway userGateway;

    @InjectMocks
    private CreateLoanUseCase createLoanUseCase;

    @Test
    void shouldCreateLoanSuccessfully() {
        // Arrange
        var loanToCreate = new LoanTestDataBuilder().withIdentityDocument(12345L).build();
        var authenticatedUser = new UserTestDataBuilder().withIdentityDocument(12345L).build();
        var savedLoan = new LoanTestDataBuilder().withIdentityDocument(12345L).build();
        var pendingStatus = Status.builder().idStatus(1L).name("Pendiente de revisión").build();
        var authenticatedUserId = 100L;

        when(userGateway.findById(authenticatedUserId)).thenReturn(Mono.just(authenticatedUser));
        when(validator.validate(any(Loan.class))).thenReturn(Mono.just(loanToCreate));
        when(statusRepository.findByName(anyString())).thenReturn(Mono.just(pendingStatus));
        when(loanRepository.save(any(Loan.class))).thenReturn(Mono.just(savedLoan));

        // Act
        Mono<Loan> result = createLoanUseCase.execute(loanToCreate, authenticatedUserId);

        // Assert
        StepVerifier.create(result)
                .expectNext(savedLoan)
                .verifyComplete();
    }

    @Test
    void shouldFailWhenIdentityDocumentDoesNotMatch() {
        // Arrange
        var loanToCreate = new LoanTestDataBuilder().withIdentityDocument(54321L).build(); // Documento diferente
        var authenticatedUser = new UserTestDataBuilder().withIdentityDocument(12345L).build();
        var authenticatedUserId = 100L;

        when(userGateway.findById(authenticatedUserId)).thenReturn(Mono.just(authenticatedUser));

        // Act
        Mono<Loan> result = createLoanUseCase.execute(loanToCreate, authenticatedUserId);

        // Assert
        StepVerifier.create(result)
                .expectError(LoanCreationForbiddenException.class)
                .verify();
    }
}
