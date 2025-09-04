package co.com.crediya.api;

import co.com.crediya.api.dto.LoanRequestDTO;
import co.com.crediya.api.mapper.LoanApiMapper;
import co.com.crediya.model.loan.exceptions.InvalidLoanRequestDataException;
import co.com.crediya.model.loan.exceptions.UserNotFoundException;
import co.com.crediya.model.loantype.exceptions.LoanTypeNotFoundException;
import co.com.crediya.usecase.command.createloan.CreateLoanUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
public class LoanHandler {
    private static final Logger log = LoggerFactory.getLogger(LoanHandler.class);
    private final CreateLoanUseCase createLoanUseCase;
    private final LoanApiMapper loanApiMapper;
    private final TransactionalOperator transactionalOperator;

    public Mono<ServerResponse> createLoanRequest(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoanRequestDTO.class)
                .map(loanApiMapper::toDomain)
                .doOnNext(req -> log.info("Iniciando solicitud de préstamo para el documento: {}", req.getIdentityDocument()))
                .flatMap(createLoanUseCase::execute)
                .as(transactionalOperator::transactional)
                .doOnSuccess(saved -> log.info("Solicitud #{} creada exitosamente.", saved.getIdLoan()))
                .flatMap(loanRequest -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(loanRequest))
                .doOnError(err -> log.error("Error al crear solicitud: {}", err.getMessage()))
                .onErrorResume(ServerWebInputException.class, e ->
                        ServerResponse.badRequest().bodyValue(e.getReason()))
                .onErrorResume(InvalidLoanRequestDataException.class, e ->
                        ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON).bodyValue(e.getErrors()))
                .onErrorResume(UserNotFoundException.class, e ->
                        ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(e.getMessage()))
                .onErrorResume(LoanTypeNotFoundException.class, e ->
                        ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(e.getMessage()))
                .onErrorResume(IllegalStateException.class, e ->
                        ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("Error de configuración interna."));
    }
}