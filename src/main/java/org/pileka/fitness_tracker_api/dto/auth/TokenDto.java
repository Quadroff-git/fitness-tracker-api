package org.pileka.fitness_tracker_api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenDto {
    private String bearerToken;

    /**
     * The period the access token is valid for in milliseconds
     */
    private Long accessTokenExpiration;

    private String refreshToken;

    /**
     * The period the refresh token is valid for in milliseconds
     */
    private Long refreshTokenExpiration;
}
