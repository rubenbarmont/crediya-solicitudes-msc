package co.com.crediya.usecase.service;

import co.com.crediya.model.loanrequest.LoanRequest;
import co.com.crediya.model.loanrequest.exceptions.InvalidLoanRequestDataException;
import co.com.crediya.model.loanrequest.exceptions.UserNotFoundException;
import co.com.crediya.model.loantype.exceptions.LoanTypeNotFoundException;
import co.com.crediya.model.loantype.gateways.LoanTypeRepository;
import co.com.crediya.usecase.gateways.UserGateway;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static co.com.crediya.usecase.service.LoanRequestConstants.*;

public class StandardLoanRequestValidator implements LoanRequestValidator {

    private final LoanTypeRepository loanTypeRepository;
    private final UserGateway userGateway;

    // Constructor explícito para mantener la clase como un POJO puro
    public StandardLoanRequestValidator(LoanTypeRepository loanTypeRepository, UserGateway userGateway) {
        this.loanTypeRepository = loanTypeRepository;
        this.userGateway = userGateway;
    }

    @Override
    public Mono<LoanRequest> validate(LoanRequest loanRequest) {
        // Fase 1: Validación de formato de datos (síncrona y funcional)
        return validateDataFormat(loanRequest)
                // Fase 2: Si la Fase 1 es exitosa, procede con la validación de negocio (asíncrona)
                .then(validateBusinessRules(loanRequest));
    }

    /**
     * Valida el formato y la estructura de los datos de entrada de forma funcional.
     * Acumula todos los errores y falla si encuentra al menos uno.
     */
    private Mono<LoanRequest> validateDataFormat(LoanRequest loanRequest) {
        List<String> errors = Stream.of(
                        validateRequiredField(loanRequest.getIdentityDocument(), FIELD_IDENTITY_DOCUMENT),
                        validateAmount(loanRequest.getAmount()),
                        validateTerm(loanRequest.getTerm()),
                        validateRequiredField(loanRequest.getIdLoanType(), FIELD_LOAN_TYPE_ID)
                )
                .flatMap(Optional::stream)
                .toList();

        if (!errors.isEmpty()) {
            return Mono.error(new InvalidLoanRequestDataException(errors));
        }
        return Mono.just(loanRequest);
    }

    /**
     * Valida las reglas de negocio que requieren consultas externas de forma reactiva y en paralelo.
     */
    private Mono<LoanRequest> validateBusinessRules(LoanRequest loanRequest) {
        if (loanRequest.getIdLoanType() == null || loanRequest.getIdentityDocument() == null) {
            return Mono.error(new IllegalStateException("Los datos llegaron incompletos a la validación de negocio."));
        }

        return Mono.zip(
                        loanTypeRepository.findById(loanRequest.getIdLoanType())
                                .switchIfEmpty(Mono.error(new LoanTypeNotFoundException("El tipo de préstamo seleccionado no existe."))),
                        userGateway.existsByIdentityDocument(loanRequest.getIdentityDocument())
                )
                .filter(validationResult -> validationResult.getT2())
                .switchIfEmpty(Mono.error(new UserNotFoundException("El usuario con el documento proporcionado no está registrado.")))
                .thenReturn(loanRequest);
    }

    // --- Métodos de Ayuda para Validación de Formato ---

    private Optional<String> validateRequiredField(Long value, String fieldName) {
        return Objects.isNull(value)
                ? Optional.of(String.format(ERROR_FIELD_REQUIRED, fieldName))
                : Optional.empty();
    }

    private Optional<String> validateAmount(BigDecimal amount) {
        if (Objects.isNull(amount)) {
            return Optional.of(String.format(ERROR_FIELD_REQUIRED, FIELD_AMOUNT));
        }
        return amount.compareTo(BigDecimal.ZERO) <= 0
                ? Optional.of(ERROR_AMOUNT_POSITIVE)
                : Optional.empty();
    }

    private Optional<String> validateTerm(Long term) {
        if (Objects.isNull(term)) {
            return Optional.of(String.format(ERROR_FIELD_REQUIRED, FIELD_TERM));
        }
        return term < 1
                ? Optional.of(ERROR_TERM_MINIMUM)
                : Optional.empty();
    }
}

