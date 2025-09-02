package co.com.crediya.usecase.service;

import co.com.crediya.model.loanrequest.LoanRequest;
import reactor.core.publisher.Mono;

public interface LoanRequestValidator {
    Mono<LoanRequest> validate(LoanRequest loanRequest);
}
