package com.github.andrelugomes.v5.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecretConfig {

    @Bean
    public String secret() {
        return "SECRET_AUTH";
    }
}
