package co.com.crediya.usecase.command.createloan;

import co.com.crediya.model.loan.Loan;
import co.com.crediya.model.loan.gateways.LoanRepository;
import co.com.crediya.model.status.gateways.StatusRepository;
import co.com.crediya.usecase.service.LoanValidator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateLoanUseCase {

    private final LoanValidator validator;
    private final LoanRepository loanRepository;
    private final StatusRepository statusRepository;

    private static final String PENDING_STATUS_NAME = "Pendiente de revisión";

    public Mono<Loan> execute(Loan loan) {
        return validator.validate(loan)
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
