package co.com.crediya.usecase.command.createloan;

import co.com.crediya.model.loan.Loan;
import co.com.crediya.model.loan.exceptions.UserNotFoundException;
import co.com.crediya.model.loan.gateways.LoanRepository;
import co.com.crediya.model.status.gateways.StatusRepository;
import co.com.crediya.usecase.gateways.UserGateway;
import co.com.crediya.usecase.service.LoanValidator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import co.com.crediya.model.loan.exceptions.LoanCreationForbiddenException;


@RequiredArgsConstructor
public class CreateLoanUseCase {

    private final LoanValidator validator;
    private final LoanRepository loanRepository;
    private final StatusRepository statusRepository;
    private final UserGateway userGateway; // <-- INYECTAR EL GATEWAY

    private static final String PENDING_STATUS_NAME = "Pendiente de revisión";

    public Mono<Loan> execute(Loan loan, Long authenticatedUserId) {
        return userGateway.findById(authenticatedUserId)
                .switchIfEmpty(Mono.error(new UserNotFoundException("El usuario autenticado no fue encontrado en el sistema.")))
                .flatMap(authenticatedUser -> {
                    // VALIDACIÓN CLAVE: Compara el documento del token con el de la petición
                    if (!authenticatedUser.getIdentityDocument().equals(loan.getIdentityDocument())) {
                        return Mono.error(new LoanCreationForbiddenException());
                    }

                    // Si la validación pasa, enriquecemos el préstamo con el email verificado
                    loan.setEmail(authenticatedUser.getEmail());
                    return Mono.just(loan);
                })
                .flatMap(validator::validate)
                .flatMap(this::setInitialStatus)
                .flatMap(loanRepository::save);
    }

    private Mono<Loan> setInitialStatus(Loan loan) {
        return statusRepository.findByName(PENDING_STATUS_NAME)
                .switchIfEmpty(Mono.error(new IllegalStateException("Estado inicial '" + PENDING_STATUS_NAME + "' no encontrado.")))
                .map(initialStatus -> {
                    loan.setIdStatus(initialStatus.getIdStatus());
                    return loan;
                });
    }
}
