package co.com.crediya.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProperties jwtProperties;

    // --- CAMBIO: Se añaden las rutas públicas para Swagger ---
    private static final String[] SWAGGER_ROUTES = {
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/webjars/**"
    };

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        // Corrección: Asumiendo que JwtProperties es un record, el método es .secret()
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.secret());
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);
        return NimbusReactiveJwtDecoder.withSecretKey(key).build();
    }

    @Bean
    public Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("rol");
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        ReactiveJwtAuthenticationConverter jwtAuthenticationConverter = new ReactiveJwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt ->
                Flux.fromIterable(grantedAuthoritiesConverter.convert(jwt))
        );

        return jwtAuthenticationConverter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         Converter<Jwt, Mono<AbstractAuthenticationToken>> converter) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        // --- CAMBIO: Se permite el acceso público a las rutas de Swagger ---
                        .pathMatchers(SWAGGER_ROUTES).permitAll()

                        // Tus reglas de negocio se mantienen
                        .pathMatchers(HttpMethod.POST, "/api/v1/solicitud").hasRole("CLIENTE")

                        // Cualquier otra petición requiere autenticación
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtSpec -> jwtSpec
                                .jwtDecoder(reactiveJwtDecoder())
                                .jwtAuthenticationConverter(converter)
                        )
                )
                .build();
    }
}