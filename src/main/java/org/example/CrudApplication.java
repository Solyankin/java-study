package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class CrudApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CrudApplication.class);
        app.run(args);
    }
}