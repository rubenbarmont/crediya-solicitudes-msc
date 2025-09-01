package co.com.crediya.r2dbc.reactiverepository.status;

import co.com.crediya.model.status.Status;
import co.com.crediya.model.status.gateways.StatusRepository;
import co.com.crediya.r2dbc.entity.StatusEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class StatusReactiveRepositoryAdapter
        extends ReactiveAdapterOperations<Status, StatusEntity, Long, StatusReactiveRepository>
        implements StatusRepository {

    public StatusReactiveRepositoryAdapter(StatusReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.mapBuilder(d, Status.StatusBuilder.class).build());
    }

    @Override
    public Mono<Status> findByName(String name) {
        return repository.findByName(name)
                .map(this::toEntity);
    }
}
