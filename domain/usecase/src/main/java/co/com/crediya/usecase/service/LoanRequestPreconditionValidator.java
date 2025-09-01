package co.com.crediya.usecase.service;

import co.com.crediya.model.loanrequest.LoanRequest;
import reactor.core.publisher.Mono;

public interface LoanRequestPreconditionValidator {
    Mono<LoanRequest> validate(LoanRequest loanRequest);
}
