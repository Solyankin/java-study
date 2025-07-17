package org.example.authjwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JwtApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(JwtApplication.class);
        app.run(args);
    }
}