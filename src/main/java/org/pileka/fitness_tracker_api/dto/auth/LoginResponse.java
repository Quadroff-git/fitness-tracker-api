package org.pileka.fitness_tracker_api.dto.auth;

import lombok.Data;

@Data
public class LoginResponse {
    private String bearerToken;
    private String refreshToken;
}
