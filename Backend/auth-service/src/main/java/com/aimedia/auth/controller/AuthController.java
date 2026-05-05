package com.aimedia.auth.controller;

import com.aimedia.auth.dto.AuthResponse;
import com.aimedia.auth.dto.LoginRequest;
import com.aimedia.auth.dto.RegisterRequest;
import com.aimedia.auth.service.AuthService;
import com.aimedia.common.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ApiResponse<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.ok("registered", service.register(request));
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok("logged in", service.login(request));
    }
}
