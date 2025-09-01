package co.com.crediya.consumer;

import co.com.crediya.usecase.gateways.UserGateway;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class UserGatewayAdapter implements UserGateway {

    private final WebClient client;
    private static final String AUTENTICACION_CIRCUIT_BREAKER = "autenticacionService";

    @Override
    @CircuitBreaker(name = AUTENTICACION_CIRCUIT_BREAKER, fallbackMethod = "fallbackExistsByEmail")
    public Mono<Boolean> existsByEmail(String email) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/usuarios/existe-por-email")
                        .queryParam("email", email)
                        .build())
                .retrieve()
                .bodyToMono(Boolean.class);
    }

    // Método Fallback: Si el servicio de autenticación falla o está caído,
    // este método se ejecuta. Por seguridad, asumimos que el usuario no existe.
    public Mono<Boolean> fallbackExistsByEmail(String email, Throwable throwable) {
        // Aquí podríamos loggear el error: log.error("Fallback para existsByEmail, email: {}, error: {}", email, throwable.getMessage());
        return Mono.just(false);
    }
}
