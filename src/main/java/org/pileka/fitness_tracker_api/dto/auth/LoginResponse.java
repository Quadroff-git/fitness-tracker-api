package org.pileka.fitness_tracker_api.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String bearerToken;
    private String refreshToken;
}
