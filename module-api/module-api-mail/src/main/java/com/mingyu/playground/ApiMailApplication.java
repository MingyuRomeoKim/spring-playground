package com.mingyu.playground;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
public class ApiMailApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiMailApplication.class, args);
    }
}