package co.com.crediya.api;

import co.com.crediya.api.dto.ErrorResponseDTO;
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
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import co.com.crediya.model.loan.Loan;
import co.com.crediya.model.loan.exceptions.LoanCreationForbiddenException;
import org.springframework.security.oauth2.jwt.Jwt;


import java.time.LocalDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LoanHandler {
    private static final Logger log = LoggerFactory.getLogger(LoanHandler.class);
    private final CreateLoanUseCase createLoanUseCase;
    private final LoanApiMapper loanApiMapper;
    private final TransactionalOperator transactionalOperator;

    public Mono<ServerResponse> createLoanRequest(ServerRequest serverRequest) {
        Mono<Loan> loanMono = serverRequest.bodyToMono(LoanRequestDTO.class)
                .map(loanApiMapper::toDomain);

        Mono<Long> authenticatedUserIdMono = ReactiveSecurityContextHolder.getContext()
                .map(ctx -> ctx.getAuthentication().getPrincipal())
                .flatMap(principal -> {
                    if (principal instanceof Jwt jwt) {
                        Map<String, Object> claims = jwt.getClaims();
                        Object userIdClaim = claims.get("userId");
                        if (userIdClaim instanceof Number) {
                            return Mono.just(((Number) userIdClaim).longValue());
                        }
                    }
                    return Mono.error(new IllegalStateException("No se pudo obtener un userId válido del token."));
                })
                .switchIfEmpty(Mono.error(new IllegalStateException("No se pudo obtener el userId del token")));

        return Mono.zip(loanMono, authenticatedUserIdMono)
                .doOnNext(tuple -> log.info("Iniciando solicitud de préstamo para doc: {} por user_id: {}",
                        tuple.getT1().getIdentityDocument(), tuple.getT2()))
                .flatMap(tuple -> createLoanUseCase.execute(tuple.getT1(), tuple.getT2()))
                .as(transactionalOperator::transactional)
                .doOnSuccess(saved -> log.info("Solicitud #{} creada exitosamente.", saved.getIdLoan()))
                .flatMap(loanRequest -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(loanRequest))
                .doOnError(err -> log.error("Error al crear solicitud para {}: {}", serverRequest.path(), err.getMessage()))
                .onErrorResume(InvalidLoanRequestDataException.class, e ->
                        buildErrorResponse(e, HttpStatus.BAD_REQUEST, "INVALID_LOAN_DATA", serverRequest, e.getErrors()))
                .onErrorResume(UserNotFoundException.class, e ->
                        buildErrorResponse(e, HttpStatus.BAD_REQUEST, "USER_NOT_FOUND", serverRequest))
                .onErrorResume(LoanTypeNotFoundException.class, e ->
                        buildErrorResponse(e, HttpStatus.BAD_REQUEST, "LOAN_TYPE_NOT_FOUND", serverRequest))
                .onErrorResume(LoanCreationForbiddenException.class, e ->
                        buildErrorResponse(e, HttpStatus.FORBIDDEN, "FORBIDDEN_LOAN_CREATION", serverRequest))
                .onErrorResume(IllegalStateException.class, e ->
                        buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", serverRequest));
    }

    private Mono<ServerResponse> buildErrorResponse(Throwable err, HttpStatus status, String errorCode, ServerRequest request) {
        return buildErrorResponse(err, status, errorCode, request, err.getMessage());
    }

    private Mono<ServerResponse> buildErrorResponse(Throwable err, HttpStatus status, String errorCode, ServerRequest request, Object body) {
        ErrorResponseDTO errorDto = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .error(errorCode)
                .message(err.getMessage())
                .path(request.path())
                .build();
        // Para InvalidLoanRequestDataException, el body puede ser una lista de errores
        // Para los demás, será el mensaje del error.
        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body instanceof String ? errorDto : body);
    }
}