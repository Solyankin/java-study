package org.example.authjwt.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.authjwt.controller.model.AuthResponse;
import org.example.authjwt.controller.model.RefreshTokenRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/auth")
@Tag(name = "Auth API", description = "Auth")
public interface AuthController {

    @PostMapping("/login")
    ResponseEntity<AuthResponse> login();

    @PostMapping("/refresh")
    ResponseEntity<AuthResponse> refresh(@RequestBody RefreshTokenRequest request);
}
