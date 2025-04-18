package com.cloud.aerohub.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.stereotype.Component;

@EnableCaching
@Component
public class AppConfiguration {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @PostConstruct
    public void clearRedisOnStartup() {
        redisConnectionFactory.getConnection().flushAll();
    }

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

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        GenericJackson2JsonRedisSerializer jsonSerializer =
                new GenericJackson2JsonRedisSerializer(mapper);

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer)
                );

        return RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .build();
    }
}