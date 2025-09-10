package co.com.crediya.consumer;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RestConsumer {
    private final WebClient client;



    @CircuitBreaker(name = "testGet")
    public Mono<ObjectResponse> testGet() {
        return client
                .get()
                .retrieve()
                .bodyToMono(ObjectResponse.class);
    }

    @CircuitBreaker(name = "testPost")
    public Mono<ObjectResponse> testPost() {
        ObjectRequest request = ObjectRequest.builder()
            .val1("exampleval1")
            .val2("exampleval2")
            .build();
        return client
                .post()
                .body(Mono.just(request), ObjectRequest.class)
                .retrieve()
                .bodyToMono(ObjectResponse.class);
    }
}
