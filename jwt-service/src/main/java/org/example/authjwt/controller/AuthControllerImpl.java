package org.example.authjwt.controller;

import lombok.RequiredArgsConstructor;
import org.example.authjwt.controller.model.AuthRequest;
import org.example.authjwt.controller.model.AuthResponse;
import org.example.authjwt.controller.model.RefreshTokenRequest;
import org.example.authjwt.service.RefreshTokenService;
import org.example.authjwt.utils.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final InMemoryUserDetailsManager userDetailsManager;


    @PostMapping("/login")
    @Override
    public ResponseEntity<AuthResponse> login() {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        UserDetails userDetails = userDetailsManager.loadUserByUsername(username);
        String accessToken = jwtUtils.createAccessToken(userDetails);


        String refreshToken = refreshTokenService.createRefreshToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    @Override
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!refreshTokenService.validateRefreshToken(refreshToken)) {
            throw new SecurityException("Invalid refresh token");
        }

        String username = refreshTokenService.getUsernameFromRefreshToken(refreshToken);

        UserDetails userDetails = userDetailsManager.loadUserByUsername(username);

        String newAccessToken = jwtUtils.createAccessToken(userDetails);
        String newRefreshToken = refreshTokenService.updateRefreshToken(refreshToken);

        return ResponseEntity.ok(new AuthResponse(newAccessToken, newRefreshToken));
    }
}