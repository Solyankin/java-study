package org.example.authjwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@ConfigurationProperties(prefix = "jwt")
@Component
@Setter
@Getter
public class JwtProperties {

    private String secret;
    private Token access;
    private Token refresh;

    @Setter
    @Getter
    public static class Token {
        private Long expiration;
    }
}
