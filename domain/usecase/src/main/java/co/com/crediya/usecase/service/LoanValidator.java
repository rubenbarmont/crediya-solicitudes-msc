package co.com.crediya.usecase.service;

import co.com.crediya.model.loan.Loan;
import reactor.core.publisher.Mono;

public interface LoanValidator {
    Mono<Loan> validate(Loan loan);
}
