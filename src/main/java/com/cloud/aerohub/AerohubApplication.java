package com.cloud.aerohub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class AerohubApplication {

    public static void main(String[] args) {
        SpringApplication.run(AerohubApplication.class, args);
    }

}
