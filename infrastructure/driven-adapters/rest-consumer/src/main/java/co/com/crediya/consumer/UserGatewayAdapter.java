package co.com.crediya.consumer;

import co.com.crediya.usecase.gateways.UserGateway;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserGatewayAdapter implements UserGateway {

    private static final Logger log = LoggerFactory.getLogger(UserGatewayAdapter.class);
    private final WebClient client; // Inyecta el WebClient creado por RestConsumerConfig
    private static final String AUTENTICACION_CIRCUIT_BREAKER = "autenticacionService";

    @Override
    @CircuitBreaker(name = AUTENTICACION_CIRCUIT_BREAKER, fallbackMethod = "fallbackExistsByIdentityDocument")
    public Mono<Boolean> existsByIdentityDocument(Long identityDocument) {
        log.info("Consultando existencia de documento: {} en servicio de autenticación", identityDocument);
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/usuarios")
                        .queryParam("identityDocument", identityDocument)
                        .build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(e -> {
                    log.error("Error al consultar servicio de autenticación: {}", e.getMessage());
                    return Mono.error(e);
                });
    }

    public Mono<Boolean> fallbackExistsByIdentityDocument(Long identityDocument, Throwable throwable) {
        log.warn("Fallback activado para existsByIdentityDocument. Documento: {}. Causa: {}", identityDocument, throwable.getMessage());
        return Mono.just(false);
    }
}
