package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class CrudApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CrudApplication.class);
        app.run(args);
    }
}