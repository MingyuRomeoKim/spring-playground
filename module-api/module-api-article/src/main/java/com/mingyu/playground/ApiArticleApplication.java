package com.mingyu.playground;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
public class ApiArticleApplication {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}