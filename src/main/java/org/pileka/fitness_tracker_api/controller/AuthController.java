package org.pileka.fitness_tracker_api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.pileka.fitness_tracker_api.dto.auth.LoginRefreshResponseDto;
import org.pileka.fitness_tracker_api.dto.auth.LoginRequestDto;
import org.pileka.fitness_tracker_api.dto.auth.RegistrationDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthController {
    @Operation(summary = "Register a new user",
            tags = {"auth"},
            description = "Adds a new user"
    )
    @PostMapping("/register")
    ResponseEntity register(@Valid @RequestBody RegistrationDto request);

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
    @PostMapping(path = "/login", produces = "application/json")
    ResponseEntity<LoginRefreshResponseDto> login(@Valid @RequestBody LoginRequestDto request);

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
    @PostMapping(path = "/refresh", produces = "application/json")
    ResponseEntity<LoginRefreshResponseDto> refresh(@CookieValue("refresh_token") String refreshToken);
}
