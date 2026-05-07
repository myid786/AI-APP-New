package com.aimedia.auth.service;

import com.aimedia.auth.dto.AuthResponse;
import com.aimedia.auth.dto.LoginRequest;
import com.aimedia.auth.dto.RegisterRequest;
import com.aimedia.auth.entity.Role;
import com.aimedia.auth.entity.UserAuth;
import com.aimedia.auth.repository.UserAuthRepository;
import com.aimedia.common.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {
    private final UserAuthRepository repository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserAuthRepository repository, PasswordEncoder encoder,
                       @Value("${app.jwt.secret}") String secret,
                       @Value("${app.jwt.expiration-ms}") long expirationMs) {
        this.repository = repository;
        this.encoder = encoder;
        this.jwtUtil = new JwtUtil(secret, expirationMs);
    }

    public AuthResponse register(RegisterRequest request) {
        repository.findByEmail(request.email()).ifPresent(u -> { throw new RuntimeException("Email already exists"); });
        repository.findByUsername(request.username()).ifPresent(u -> { throw new RuntimeException("Username already exists"); });

        UserAuth user = new UserAuth();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(encoder.encode(request.password()));
        user.setRole(request.role() == null ? Role.CONSUMER : request.role());

        UserAuth saved = repository.save(user);
        String token = jwtUtil.generateToken(saved.getEmail(), Map.of("userId", saved.getId(), "role", saved.getRole().name()));
        return new AuthResponse(token, saved.getId(), saved.getUsername(), saved.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        UserAuth user = repository.findByEmail(request.email()).orElseThrow(() -> new RuntimeException("Invalid credentials"));
        if (!encoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        String token = jwtUtil.generateToken(user.getEmail(), Map.of("userId", user.getId(), "role", user.getRole().name()));
        return new AuthResponse(token, user.getId(), user.getUsername(), user.getRole().name());
    }
}
