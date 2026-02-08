package url.shortener.auth_service.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.hibernate.internal.util.StringHelper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import url.shortener.auth_service.config.JwtSecretProperties;
import url.shortener.auth_service.dto.AuthResponse;
import url.shortener.auth_service.dto.LoginRequest;
import url.shortener.auth_service.dto.RefreshTokenRequest;
import url.shortener.auth_service.dto.UserRegisterRequest;
import url.shortener.auth_service.entity.RefreshToken;
import url.shortener.auth_service.entity.User;
import url.shortener.auth_service.repository.RefreshTokenRepository;
import url.shortener.auth_service.repository.UserRepository;
import url.shortener.auth_service.security.JwtUtils;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final JwtSecretProperties jwtSecretProperties;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthResponse registerUser(UserRegisterRequest request) throws Exception {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("User already exist");
        }

        User newUser = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("ROLE_USER")
                .updatedAt(LocalDateTime.now())
                .build();
        userRepository.save(newUser);

        // create tokens
        return createAuthTokens(newUser, null);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        refreshTokenRepository.deleteByUsername(request.getUsername());
        return createAuthTokens(user, null);
    }

    public AuthResponse createAuthTokens(User user, String refreshToken) {
        String accessToken = jwtUtils.generateAccessToken(user.getUsername(), user.getRole());
        refreshToken = StringHelper.isEmpty(refreshToken) ? generateRefreshToken(user.getUsername()) : refreshToken;
        return AuthResponse.builder()
                .username(user.getUsername())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private String generateRefreshToken(String username) {
        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .username(username)
                .expiryDate(LocalDateTime.now().plusSeconds(jwtSecretProperties.refreshTokenExpiration() / 1000))
                .build();

        refreshTokenRepository.save(refreshToken);
        return token;
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.getByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
        
        User user = userRepository.findByUsername(refreshToken.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid user"));
        
        return createAuthTokens(user, request.getRefreshToken());
    }
}
