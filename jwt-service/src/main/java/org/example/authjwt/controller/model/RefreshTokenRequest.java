package org.example.authjwt.controller.model;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String refreshToken;
}
