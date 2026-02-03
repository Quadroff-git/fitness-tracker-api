package org.pileka.fitness_tracker_api.dto.auth;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoginRefreshResponseDto {
    private String accessToken;
    private Long expiresIn; // in seconds
}
