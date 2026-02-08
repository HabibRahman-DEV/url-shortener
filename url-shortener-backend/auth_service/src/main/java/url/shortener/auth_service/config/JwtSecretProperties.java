package url.shortener.auth_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt.secrets")
public record JwtSecretProperties(String secretKey, int accessTokenExpiration, int refreshTokenExpiration) {
}
