package co.com.crediya.usecase.command.createloanrequest;

import co.com.crediya.model.loanrequest.LoanRequest;
import co.com.crediya.model.loanrequest.gateways.LoanRequestRepository;
import co.com.crediya.model.status.Status;
import co.com.crediya.model.status.gateways.StatusRepository;
import co.com.crediya.usecase.service.LoanRequestPreconditionValidator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;


@RequiredArgsConstructor
public class CreateLoanRequestUseCase {

    private final LoanRequestPreconditionValidator preconditionValidator;
    private final LoanRequestRepository loanRequestRepository;
    private final StatusRepository statusRepository; // <-- El repositorio vuelve a ser una dependencia

    private static final String PENDING_STATUS_NAME = "Pendiente de revisión";

    public Mono<LoanRequest> execute(LoanRequest loanRequest) {
        return preconditionValidator.validate(loanRequest)
                .flatMap(this::findInitialStatusAndCombine) // Usamos el repositorio para buscar el estado
                .flatMap(resultTuple -> {
                    LoanRequest validRequest = resultTuple.getT1();
                    Status initialStatus = resultTuple.getT2();
                    validRequest.setIdStatus(initialStatus.getIdStatus()); // Asignamos el ID
                    return loanRequestRepository.save(validRequest);
                });
    }

    private Mono<Tuple2<LoanRequest, Status>> findInitialStatusAndCombine(LoanRequest loanRequest) {
        return Mono.zip(
                Mono.just(loanRequest),
                statusRepository.findByName(PENDING_STATUS_NAME)
                        .switchIfEmpty(Mono.error(new IllegalStateException("Estado inicial '" + PENDING_STATUS_NAME + "' no encontrado.")))
        );
    }
}
