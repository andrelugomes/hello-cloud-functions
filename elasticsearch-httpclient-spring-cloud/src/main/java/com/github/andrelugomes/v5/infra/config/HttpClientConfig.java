package com.github.andrelugomes.v5.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.net.http.HttpClient.Version.HTTP_1_1;
import static java.time.Duration.ofSeconds;

@Configuration
public class HttpClientConfig {

    @Bean
    public java.net.http.HttpClient httpClient() {
        return java.net.http.HttpClient.newBuilder().version(HTTP_1_1).connectTimeout(ofSeconds(10)).build();
    }
}
