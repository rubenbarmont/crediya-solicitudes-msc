/*
package co.com.crediya.usecase.service;

import co.com.crediya.model.loan.Loan;
import co.com.crediya.model.loan.exceptions.InvalidLoanRequestDataException;
import co.com.crediya.model.loan.exceptions.UserNotFoundException;
import co.com.crediya.model.loantype.LoanType;
import co.com.crediya.model.loantype.exceptions.LoanTypeNotFoundException;
import co.com.crediya.model.loantype.gateways.LoanTypeRepository;
import co.com.crediya.usecase.databuilder.LoanBuilder;
import co.com.crediya.usecase.databuilder.LoanTypeBuilder;
import co.com.crediya.usecase.gateways.UserGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StandardLoanBuilderValidatorTest {

    @Mock
    private LoanTypeRepository loanTypeRepository;
    @Mock
    private UserGateway userGateway;

    private StandardLoanValidator validator;

    @BeforeEach
    void setUp() {
        validator = new StandardLoanValidator(loanTypeRepository, userGateway);
    }

    @Test
    @DisplayName("Debe validar exitosamente una solicitud con datos correctos")
    void shouldValidateSuccessfully() {
        // Arrange (Organizar)
        Loan validRequest = new LoanBuilder().build();
        LoanType existingLoanType = new LoanTypeBuilder().build();

        when(loanTypeRepository.findById(validRequest.getIdLoanType())).thenReturn(Mono.just(existingLoanType));
        when(userGateway.existsByIdentityDocument(validRequest.getIdentityDocument())).thenReturn(Mono.just(true));

        // Act (Actuar)
        Mono<Loan> result = validator.validate(validRequest);

        // Assert (Afirmar)
        StepVerifier.create(result)
                .expectNext(validRequest)
                .verifyComplete();
    }

    @Test
    @DisplayName("Debe fallar la validación de formato si los campos requeridos son nulos")
    void shouldFailFormatValidationForNullFields() {
        // Arrange
        Loan invalidRequest = new LoanBuilder()
                .withIdentityDocument(null)
                .withAmount(null)
                .build();

        // Act
        Mono<Loan> result = validator.validate(invalidRequest);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof InvalidLoanRequestDataException e &&
                                e.getErrors().size() == 2 &&
                                e.getErrors().get(0).contains("documento de identidad") &&
                                e.getErrors().get(1).contains("monto")
                )
                .verify();
    }

    @Test
    @DisplayName("Debe fallar la validación de formato si el monto es negativo")
    void shouldFailFormatValidationForNegativeAmount() {
        // Arrange
        Loan invalidRequest = new LoanBuilder().withAmount(new BigDecimal("-100")).build();

        // Act
        Mono<Loan> result = validator.validate(invalidRequest);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof InvalidLoanRequestDataException e &&
                                e.getErrors().get(0).contains("monto debe ser un número positivo")
                )
                .verify();
    }

    @Test
    @DisplayName("Debe fallar la validación de negocio si el tipo de préstamo no existe")
    void shouldFailBusinessValidationWhenLoanTypeNotFound() {
        // Arrange
        Loan request = new LoanBuilder().build();
        when(loanTypeRepository.findById(anyLong())).thenReturn(Mono.empty()); // Simula que no se encontró
        when(userGateway.existsByIdentityDocument(anyLong())).thenReturn(Mono.just(true));

        // Act
        Mono<Loan> result = validator.validate(request);

        // Assert
        StepVerifier.create(result)
                .expectError(LoanTypeNotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("Debe fallar la validación de negocio si el usuario no existe")
    void shouldFailBusinessValidationWhenUserNotFound() {
        // Arrange
        Loan request = new LoanBuilder().build();
        LoanType existingLoanType = new LoanTypeBuilder().build();
        when(loanTypeRepository.findById(anyLong())).thenReturn(Mono.just(existingLoanType));
        when(userGateway.existsByIdentityDocument(anyLong())).thenReturn(Mono.just(false)); // Simula que el usuario no existe

        // Act
        Mono<Loan> result = validator.validate(request);

        // Assert
        StepVerifier.create(result)
                .expectError(UserNotFoundException.class)
                .verify();
    }
}
*/
