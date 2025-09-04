package co.com.crediya.usecase.gateways;

import co.com.crediya.model.user.User;
import reactor.core.publisher.Mono;

public interface UserGateway {
    // Mono<Boolean> existsByIdentityDocument(Long identityDocument);

    Mono<User> findByIdentityDocument(Long identityDocument); // <-- NUEVO MÉTODO
}
