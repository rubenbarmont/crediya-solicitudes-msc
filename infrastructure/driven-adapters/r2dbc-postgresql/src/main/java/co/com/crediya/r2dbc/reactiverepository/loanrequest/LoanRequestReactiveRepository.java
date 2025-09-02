package co.com.crediya.r2dbc.reactiverepository.loanrequest;

import co.com.crediya.r2dbc.entity.LoanRequestEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface LoanRequestReactiveRepository extends ReactiveCrudRepository<LoanRequestEntity, Long>, ReactiveQueryByExampleExecutor<LoanRequestEntity> {

}
