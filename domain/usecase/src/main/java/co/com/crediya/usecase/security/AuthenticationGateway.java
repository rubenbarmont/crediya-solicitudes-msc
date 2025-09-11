package co.com.crediya.usecase.security;

import reactor.core.publisher.Mono;

public interface AuthenticationGateway {
    Mono<Long> getAuthenticatedUserId();
}
