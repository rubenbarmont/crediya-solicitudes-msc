package co.com.crediya.r2dbc.reactiverepository.loanrequest;

import co.com.crediya.model.loanrequest.LoanRequest;
import co.com.crediya.model.loanrequest.gateways.LoanRequestRepository;
import co.com.crediya.r2dbc.entity.LoanRequestEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class LoanRequestReactiveRepositoryAdapter
        extends ReactiveAdapterOperations<LoanRequest, LoanRequestEntity, Long, LoanRequestReactiveRepository>
        implements LoanRequestRepository {

    public LoanRequestReactiveRepositoryAdapter(LoanRequestReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.mapBuilder(d, LoanRequest.LoanRequestBuilder.class).build());
    }
    // El método save() es heredado de la clase base ReactiveAdapterOperations
}
