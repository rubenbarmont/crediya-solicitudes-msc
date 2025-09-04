package co.com.crediya.consumer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "adapter.restconsumer")
public record RestConsumerProperties(String url, int timeout) {
}