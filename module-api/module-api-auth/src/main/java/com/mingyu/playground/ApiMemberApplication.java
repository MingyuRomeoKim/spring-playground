package com.mingyu.playground;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableCaching
@EnableJpaAuditing
public class ApiMemberApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiMemberApplication.class, args);
    }
}