package co.com.crediya.r2dbc.reactiverepository.status;

import co.com.crediya.r2dbc.entity.StatusEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface StatusReactiveRepository extends ReactiveCrudRepository<StatusEntity, Long>, ReactiveQueryByExampleExecutor<StatusEntity> {
    // Spring Data infiere la consulta a partir del nombre del método
    Mono<StatusEntity> findByName(String name);
}