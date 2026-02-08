package url.shortener.auth_service.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import url.shortener.auth_service.config.JwtSecretProperties;

import java.security.Key;
import java.util.Date;

@Component
@AllArgsConstructor
public class JwtUtils {

    private final JwtSecretProperties jwtSecretProperties;

    public String generateAccessToken(String username, String role) {
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + (jwtSecretProperties.accessTokenExpiration())))
                .signWith(key())
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretProperties.secretKey()));
    }
}
