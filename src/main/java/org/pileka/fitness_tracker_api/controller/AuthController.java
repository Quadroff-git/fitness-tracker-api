package org.pileka.fitness_tracker_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user",
            tags = {"auth"},
            description = "Adds a new user"
    )
    public ResponseEntity register(@Valid @RequestBody RegistrationDto request) {
        authService.register(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/login", produces = "application/json")
    @Operation(summary = "Authenticate an existing user",
            tags = {"auth"},
            description = "Authenticates sent user credentials and returns a pair of tokens if successful",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Authentication success",
                    headers = {
                            @Header(
                                    name = "Set-Cookie",
                                    schema = @Schema(type = "string"),
                                    example = "refresh_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...; HttpOnly; Secure; Path=/api/auth/refresh; Max-Age=604800; SameSite=Strict"
                            )
                    }
                )
            }
    )
    public ResponseEntity<LoginRefreshResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        return getResponseFromTokenDto(authService.login(request));
    }

    @PostMapping(path = "/refresh", produces = "application/json")
    @Operation(summary = "Refresh client's tokens",
            tags = {"auth"},
            description = "Checks if client's refresh token is valid and returns a fresh pair of tokens if it is",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "Refresh success",
                    headers = {
                            @Header(
                                    name = "Set-Cookie",
                                    schema = @Schema(type = "string"),
                                    example = "refresh_token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...; HttpOnly; Secure; Path=/api/auth/refresh; Max-Age=604800; SameSite=Strict"
                            )
                    }
            )
            })
    public ResponseEntity<LoginRefreshResponseDto> refresh(@CookieValue("refresh_token") String refreshToken) {
        TokenDto tokenDto = authService.refresh(refreshToken);
        if (tokenDto != null) {
            return getResponseFromTokenDto(tokenDto);
        }
        else {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();
        }
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
