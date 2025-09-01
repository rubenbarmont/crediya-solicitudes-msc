package co.com.crediya.usecase.command.usergateway;

import reactor.core.publisher.Mono;

public interface UserGateway {
    Mono<Boolean> existsByEmail(String email);
}
