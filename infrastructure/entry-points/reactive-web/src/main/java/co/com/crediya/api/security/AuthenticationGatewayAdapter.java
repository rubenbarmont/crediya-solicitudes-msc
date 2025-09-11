package co.com.crediya.api.security;

import co.com.crediya.usecase.security.AuthenticationGateway;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class AuthenticationGatewayAdapter implements AuthenticationGateway {

    @Override
    public Mono<Long> getAuthenticatedUserId() {
        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> ctx.getAuthentication().getPrincipal())
                .flatMap(principal -> {
                    if (principal instanceof Jwt jwt) {
                        Map<String, Object> claims = jwt.getClaims();
                        Object userIdClaim = claims.get("userId");
                        if (userIdClaim instanceof Number) {
                            return Mono.just(((Number) userIdClaim).longValue());
                        }
                    }
                    return Mono.error(new IllegalStateException("No se pudo obtener un userId válido del token."));
                })
                .switchIfEmpty(Mono.error(new IllegalStateException("No se pudo obtener el userId del contexto de seguridad.")));
    }
}
