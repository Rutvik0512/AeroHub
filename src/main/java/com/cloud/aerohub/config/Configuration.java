package com.cloud.aerohub.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Configuration {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public OpenAPI swaggerCustomConfig() {
        return new OpenAPI().info(
                new Info().title("Aerohub Endpoints Documentation")
                        .description("Author: Rutvik, Shekapuram")
                );
    }

    @Bean("airportKeyGen")
    public KeyGenerator airportKeyGen() {
        return (target, method, params) -> {
            int pageSize = (Integer) params[0];
            String sortField = (String) params[2];
            String sortDir = (String) params[3];
            String search = (String) params[4];

            return new SimpleKey(pageSize,
                    sortField == null ? "" : sortField,
                    sortDir == null ? "" : sortDir,
                    search == null ? "" : search.trim());
        };
    }

}