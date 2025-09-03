package co.com.crediya.usecase.service;

import co.com.crediya.model.loan.Loan;
import co.com.crediya.model.loan.exceptions.InvalidLoanRequestDataException;
import co.com.crediya.model.loan.exceptions.UserNotFoundException;
import co.com.crediya.model.loantype.exceptions.LoanTypeNotFoundException;
import co.com.crediya.model.loantype.gateways.LoanTypeRepository;
import co.com.crediya.usecase.gateways.UserGateway;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static co.com.crediya.usecase.service.LoanConstants.*;

public class StandardLoanValidator implements LoanValidator {

    private final LoanTypeRepository loanTypeRepository;
    private final UserGateway userGateway;

    public StandardLoanValidator(LoanTypeRepository loanTypeRepository, UserGateway userGateway) {
        this.loanTypeRepository = loanTypeRepository;
        this.userGateway = userGateway;
    }

    @Override
    public Mono<Loan> validate(Loan loan) {
        return validateDataFormat(loan)
                .flatMap(this::validateBusinessRules);
    }

    /**
     * Valida el formato y la estructura de los datos de entrada de forma funcional.
     * Acumula todos los errores y falla si encuentra al menos uno.
     */
    private Mono<Loan> validateDataFormat(Loan loan) {
        List<String> errors = Stream.of(
                        validateRequiredField(loan.getIdentityDocument(), FIELD_IDENTITY_DOCUMENT),
                        validateAmount(loan.getAmount()),
                        validateTerm(loan.getTerm()),
                        validateRequiredField(loan.getIdLoanType(), FIELD_LOAN_TYPE_ID)
                )
                .flatMap(Optional::stream)
                .toList();

        if (!errors.isEmpty()) {
            return Mono.error(new InvalidLoanRequestDataException(errors));
        }
        return Mono.just(loan);
    }

    /**
     * Valida las reglas de negocio que requieren consultas externas de forma reactiva y en paralelo.
     */
    private Mono<Loan> validateBusinessRules(Loan loan) {
        if (loan.getIdLoanType() == null || loan.getIdentityDocument() == null) {
            return Mono.error(new IllegalStateException("Los datos llegaron incompletos a la validación de negocio."));
        }

        return Mono.zip(
                        loanTypeRepository.findById(loan.getIdLoanType())
                                .switchIfEmpty(Mono.error(new LoanTypeNotFoundException("El tipo de préstamo seleccionado no existe."))),
                        userGateway.existsByIdentityDocument(loan.getIdentityDocument())
                )
                .filter(validationResult -> validationResult.getT2())
                .switchIfEmpty(Mono.error(new UserNotFoundException("El usuario con el documento proporcionado no está registrado.")))
                .thenReturn(loan);
    }

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

