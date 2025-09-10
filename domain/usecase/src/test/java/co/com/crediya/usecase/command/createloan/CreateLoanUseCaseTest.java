/*
package co.com.crediya.usecase.command.createloan;

import co.com.crediya.model.loan.Loan;
import co.com.crediya.model.loan.gateways.LoanRepository;
import co.com.crediya.model.status.Status;
import co.com.crediya.model.status.gateways.StatusRepository;
import co.com.crediya.usecase.databuilder.LoanBuilder;
import co.com.crediya.usecase.databuilder.StatusBuilder;
import co.com.crediya.usecase.service.LoanValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateLoanUseCaseTest {

    @Mock
    private LoanValidator validator;
    @Mock
    private LoanRepository loanRepository;
    @Mock
    private StatusRepository statusRepository;

    private CreateLoanUseCase createLoanUseCase;

    @BeforeEach
    void setUp() {
        createLoanUseCase = new CreateLoanUseCase(validator, loanRepository, statusRepository);
    }

    @Test
    @DisplayName("Debe crear una solicitud de préstamo exitosamente")
    void shouldCreateLoanRequestSuccessfully() {
        // Arrange (Organizar)
        Loan request = new LoanBuilder().build();
        Status initialStatus = new StatusBuilder().build();

        // Creamos una copia de la solicitud con el ID y el estado que esperamos después de guardar
        Loan savedRequest = request.toBuilder()
                .idLoan(99L)
                .idStatus(initialStatus.getIdStatus())
                .build();

        when(validator.validate(request)).thenReturn(Mono.just(request));
        when(statusRepository.findByName("Pendiente de revisión")).thenReturn(Mono.just(initialStatus));
        // any() asegura que el mock responda sin importar el estado que tenga el objeto al momento de guardar
        when(loanRepository.save(any(Loan.class))).thenReturn(Mono.just(savedRequest));

        // Act (Actuar)
        Mono<Loan> result = createLoanUseCase.execute(request);

        // Assert (Afirmar)
        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getIdLoan().equals(99L) &&
                                response.getIdStatus().equals(initialStatus.getIdStatus())
                )
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe propagar el error si la validación falla")
    void shouldPropagateErrorWhenValidationFails() {
        // Arrange
        Loan request = new LoanBuilder().build();
        RuntimeException validationError = new RuntimeException("Error de validación");
        when(validator.validate(request)).thenReturn(Mono.error(validationError));

        // Act
        Mono<Loan> result = createLoanUseCase.execute(request);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable.getMessage().equals("Error de validación"))
                .verify();
    }

    @Test
    @DisplayName("Debe fallar si el estado inicial no se encuentra en la base de datos")
    void shouldFailWhenInitialStatusIsNotFound() {
        // Arrange
        Loan request = new LoanBuilder().build();
        when(validator.validate(request)).thenReturn(Mono.just(request));
        when(statusRepository.findByName(anyString())).thenReturn(Mono.empty()); // Simula que no se encontró

        // Act
        Mono<Loan> result = createLoanUseCase.execute(request);

        // Assert
        StepVerifier.create(result)
                .expectError(IllegalStateException.class)
                .verify();
    }

}
*/
