package org.pileka.fitness_tracker_api.controller;

import lombok.RequiredArgsConstructor;
import org.pileka.fitness_tracker_api.dto.auth.LoginRequest;
import org.pileka.fitness_tracker_api.dto.auth.TokenDto;
import org.pileka.fitness_tracker_api.dto.auth.RegistrationRequest;
import org.pileka.fitness_tracker_api.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegistrationRequest request) {
        authService.register(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> authenticate(@RequestBody LoginRequest request) {
        // Authenticate and return JWT
        return ResponseEntity.ok(authService.login(request));
    }
}
