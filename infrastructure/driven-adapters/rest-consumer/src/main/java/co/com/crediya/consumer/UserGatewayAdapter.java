package co.com.crediya.consumer;

import co.com.crediya.consumer.dto.UserResponseDTO;
import co.com.crediya.model.user.User;
import co.com.crediya.usecase.gateways.UserGateway;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class UserGatewayAdapter implements UserGateway {

    private static final Logger log = LoggerFactory.getLogger(UserGatewayAdapter.class);
    private final WebClient client;
    private static final String AUTENTICACION_CIRCUIT_BREAKER = "autenticacionService";

    @Override
    @CircuitBreaker(name = AUTENTICACION_CIRCUIT_BREAKER, fallbackMethod = "fallbackFindUserById")
    public Mono<User> findById(Long userId) {
        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> ctx.getAuthentication().getPrincipal())
                .flatMap(principal -> {
                    if (principal instanceof Jwt jwt) {
                        return Mono.just(jwt.getTokenValue());
                    }
                    return Mono.error(new IllegalStateException("El principal no es un token JWT válido"));
                })
                .switchIfEmpty(Mono.error(new IllegalStateException("No se pudo obtener el token del contexto")))
                .flatMap(token -> {
                    log.info("Consultando datos del usuario con ID: {} en servicio de autenticación", userId);
                    return client.get()
                            .uri("/api/v1/usuarios/{id}", userId)
                            .headers(h -> h.setBearerAuth(token))
                            .retrieve()
                            .bodyToMono(UserResponseDTO.class)
                            .map(dto -> User.builder()
                                    .identityDocument(dto.getIdentityDocument())
                                    .email(dto.getEmail())
                                    .baseSalary(dto.getBaseSalary())
                                    .build());
                })
                .onErrorResume(e -> {
                    log.error("Error al consultar servicio de autenticación para ID {}: {}", userId, e.getMessage());
                    return Mono.error(e);
                });
    }

    public Mono<User> fallbackFindUserById(Long userId, Throwable throwable) {
        log.warn("Fallback activado para findById. ID: {}. Causa: {}", userId, throwable.getMessage());
        return Mono.empty();
    }
}