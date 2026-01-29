package org.pileka.fitness_tracker_api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenDto {
    private String bearerToken;

    private Long accessTokenExpirationSeconds;

    private String refreshToken;

    private Long refreshTokenExpirationSeconds;
}
