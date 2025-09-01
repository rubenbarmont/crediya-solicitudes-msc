/*
package co.com.crediya.api;

import co.com.crediya.model.loanrequest.LoanRequest;
import co.com.crediya.usecase.command.createloanrequest.CreateLoanRequestUseCase;
import co.com.crediya.usecase.createsolicitud.CreateSolicitudUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class Handler {

    private final CreateLoanRequestUseCase createLoanRequestUseCase;

    public Mono<ServerResponse> listenGETUseCase(ServerRequest serverRequest) {
        // useCase.logic();
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenGETOtherUseCase(ServerRequest serverRequest) {
        // useCase2.logic();
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenPOSTCreateSolicitudUseCase(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoanRequest.class)
                .flatMap(createLoanRequestUseCase::create) // Llama al caso de uso para la lógica de negocio
                .flatMap(loanRequest -> ServerResponse.created(URI.create("/api/v1/solicitud/" + loanRequest.getIdSolicitud()))
                        .bodyValue(loanRequest))
                .onErrorResume(IllegalArgumentException.class, e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }
}
*/
