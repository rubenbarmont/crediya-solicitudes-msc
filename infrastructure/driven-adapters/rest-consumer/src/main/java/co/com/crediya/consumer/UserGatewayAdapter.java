package co.com.crediya.consumer;

import co.com.crediya.consumer.dto.UserResponseDTO;
import co.com.crediya.model.user.User;
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
    @CircuitBreaker(name = AUTENTICACION_CIRCUIT_BREAKER, fallbackMethod = "fallbackFindUser")
    public Mono<User> findByIdentityDocument(Long identityDocument) {
        log.info("Consultando datos del usuario con documento: {} en servicio de autenticación", identityDocument);
        return client.get()
                .uri("/api/v1/usuarios/by-identity-document/{identityDocument}", identityDocument) // <-- NUEVA RUTA
                .retrieve()
                .bodyToMono(UserResponseDTO.class) // <-- Mapear a nuestro DTO de respuesta
                .map(dto -> User.builder() // <-- Mapear DTO a nuestro modelo de dominio
                        .identityDocument(dto.getIdentityDocument())
                        .email(dto.getEmail())
                        .baseSalary(dto.getBaseSalary())
                        .build())
                .onErrorResume(e -> {
                    log.error("Error al consultar servicio de autenticación para documento {}: {}", identityDocument, e.getMessage());
                    return Mono.error(e);
                });
    }

    // El fallback ahora devuelve un Mono vacío para indicar que no se pudo obtener la información.
    public Mono<User> fallbackFindUser(Long identityDocument, Throwable throwable) {
        log.warn("Fallback activado para findByIdentityDocument. Documento: {}. Causa: {}", identityDocument, throwable.getMessage());
        return Mono.empty(); // <-- Devolver un Mono vacío es mejor que un error aquí.
    }
}
