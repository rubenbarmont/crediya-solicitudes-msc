package co.com.crediya.usecase.command.createloanrequest;

import co.com.crediya.model.loanrequest.LoanRequest;
import co.com.crediya.model.loanrequest.gateways.LoanRequestRepository;
import co.com.crediya.model.status.gateways.StatusRepository;
import co.com.crediya.usecase.service.LoanRequestValidator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateLoanRequestUseCase {

    private final LoanRequestValidator validator;
    private final LoanRequestRepository loanRequestRepository;
    private final StatusRepository statusRepository;

    private static final String PENDING_STATUS_NAME = "Pendiente de revisión";

    public Mono<LoanRequest> execute(LoanRequest loanRequest) {
        return validator.validate(loanRequest)
                .flatMap(this::setInitialStatus)
                .flatMap(loanRequestRepository::save);
    }

    private Mono<LoanRequest> setInitialStatus(LoanRequest loanRequest) {
        return statusRepository.findByName(PENDING_STATUS_NAME)
                .switchIfEmpty(Mono.error(new IllegalStateException("Estado inicial '" + PENDING_STATUS_NAME + "' no encontrado.")))
                .map(initialStatus -> {
                    loanRequest.setIdStatus(initialStatus.getIdStatus());
                    return loanRequest;
                });
    }
}
