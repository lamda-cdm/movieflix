package com.sapu.movieflix.controller;

import com.sapu.movieflix.auth.entities.RefreshToken;
import com.sapu.movieflix.auth.entities.User;
import com.sapu.movieflix.auth.services.AuthService;
import com.sapu.movieflix.auth.services.JwtService;
import com.sapu.movieflix.auth.services.RefreshTokenService;
import com.sapu.movieflix.auth.utils.AuthResponse;
import com.sapu.movieflix.auth.utils.LoginRequest;
import com.sapu.movieflix.auth.utils.RefreshTokenRequest;
import com.sapu.movieflix.auth.utils.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(authService.register(registerRequest));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authService.login(loginRequest));
    }
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return ResponseEntity.ok(authService.refresh(refreshTokenRequest));
    }
}
