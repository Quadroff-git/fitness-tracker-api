package org.pileka.fitness_tracker_api.controller.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.pileka.fitness_tracker_api.controller.AuthController;
import org.pileka.fitness_tracker_api.dto.auth.LoginRefreshResponseDto;
import org.pileka.fitness_tracker_api.dto.auth.LoginRequestDto;
import org.pileka.fitness_tracker_api.dto.auth.TokenDto;
import org.pileka.fitness_tracker_api.dto.auth.RegistrationDto;
import org.pileka.fitness_tracker_api.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Override
    public ResponseEntity register(@Valid @RequestBody RegistrationDto request) {
        authService.register(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/login", produces = "application/json")
    @Override
    public ResponseEntity<LoginRefreshResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        return getResponseFromTokenDto(authService.login(request));
    }

    @PostMapping(path = "/refresh", produces = "application/json")
    @Override
    public ResponseEntity<LoginRefreshResponseDto> refresh(@CookieValue("refresh_token") String refreshToken) {
        return getResponseFromTokenDto(authService.refresh(refreshToken));
    }

    private ResponseEntity<LoginRefreshResponseDto> getResponseFromTokenDto(TokenDto tokenDto) {
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", tokenDto.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/api/auth/refresh")
                .maxAge(tokenDto.getRefreshTokenExpiration() / 1000)
                .sameSite("strict")
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(new LoginRefreshResponseDto(tokenDto.getBearerToken(), tokenDto.getAccessTokenExpiration() / 100));
    }
}
