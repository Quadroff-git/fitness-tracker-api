package org.pileka.fitness_tracker_api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.pileka.fitness_tracker_api.dto.auth.LoginDto;
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
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody RegistrationDto request) {
        authService.register(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDto request) {
        return getResponseFromTokenDto(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@CookieValue("refresh_token") String refreshToken) {
        TokenDto tokenDto = authService.refresh(refreshToken);
        if (tokenDto != null) {
            return getResponseFromTokenDto(tokenDto);
        }
        else {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();
        }
    }

    private ResponseEntity<String> getResponseFromTokenDto(TokenDto tokenDto) {
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refresh_token", tokenDto.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/api/auth/refresh")
                .maxAge(tokenDto.getRefreshTokenExpiration() / 1000)
                .sameSite("strict")
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(String.format("""
                            {
                                "access_token" : "%s",
                                "expires_in" : %d
                            }
                            """, tokenDto.getBearerToken(), tokenDto.getAccessTokenExpiration() / 1000));
    }
}
