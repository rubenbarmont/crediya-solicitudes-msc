package co.com.crediya.r2dbc.reactiverepository.loan;

import co.com.crediya.model.loan.Loan;
import co.com.crediya.model.loan.gateways.LoanRepository;
import co.com.crediya.r2dbc.entity.LoanEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class LoanReactiveRepositoryAdapter
        extends ReactiveAdapterOperations<Loan, LoanEntity, Long, LoanReactiveRepository>
        implements LoanRepository {

    public LoanReactiveRepositoryAdapter(LoanReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.mapBuilder(d, Loan.LoanBuilder.class).build());
    }
    // El método save() es heredado de la clase base ReactiveAdapterOperations
}
