package org.example.authjwt.service;

import lombok.RequiredArgsConstructor;
import org.example.authjwt.JwtProperties;
import org.example.authjwt.model.RefreshToken;
import org.example.authjwt.repository.RefreshTokenRepository;
import org.example.authjwt.utils.JwtUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;
    private final InMemoryUserDetailsManager userDetailsManager;
    private final JwtProperties properties;

    public String createRefreshToken(UserDetails userDetails) {

        refreshTokenRepository.findByUsername(userDetails.getUsername())
                .ifPresent(refreshTokenRepository::delete);

        String token = jwtUtils.createRefreshToken(userDetails);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUsername(userDetails.getUsername());
        refreshToken.setExpiryDate(Instant.now().plusMillis(properties.getRefresh().getExpiration()));

        refreshTokenRepository.save(refreshToken);

        return token;
    }

    public boolean validateRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .map(t -> !t.getExpiryDate().isBefore(Instant.now()))
                .orElse(false);
    }

    public String getUsernameFromRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .map(RefreshToken::getUsername)
                .orElseThrow(() -> new SecurityException("Invalid refresh token"));
    }

    public String updateRefreshToken(String oldToken) {
        return refreshTokenRepository.findByToken(oldToken)
                .map(token -> {
                    token.setExpiryDate(Instant.now().plusMillis(properties.getRefresh().getExpiration()));
                    refreshTokenRepository.save(token);
                    return token.getToken();
                })
                .orElseThrow(() -> new SecurityException("Refresh token not found"));
    }
}