package com.cloud.aerohub.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
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

}