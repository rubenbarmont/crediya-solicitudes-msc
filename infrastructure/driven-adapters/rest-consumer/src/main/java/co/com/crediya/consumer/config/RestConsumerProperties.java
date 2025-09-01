package co.com.crediya.consumer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

// Le decimos a Spring que esta clase contiene las propiedades bajo el prefijo "adapter.restconsumer"
@ConfigurationProperties(prefix = "adapter.restconsumer")
public record RestConsumerProperties(String url, int timeout) {
}