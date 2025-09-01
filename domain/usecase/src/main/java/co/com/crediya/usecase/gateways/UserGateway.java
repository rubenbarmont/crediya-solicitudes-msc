package co.com.crediya.usecase.gateways;

import reactor.core.publisher.Mono;

public interface UserGateway {
    Mono<Boolean> existsByEmail(String email);
}
