package com.aimedia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SingleBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(SingleBackendApplication.class, args);
    }
}
