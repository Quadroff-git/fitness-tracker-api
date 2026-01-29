package org.pileka.fitness_tracker_api.service;

import org.pileka.fitness_tracker_api.dto.auth.LoginDto;
import org.pileka.fitness_tracker_api.dto.auth.TokenDto;
import org.pileka.fitness_tracker_api.dto.auth.RegistrationDto;

/**
 * Authentication service class
 */
public interface AuthService {
    /**
     * Register a new user
     *
     * @param request values to create the new user with
     * @return true if registration is creation is successful
     */
    boolean register(RegistrationDto request);

    /**
     * Login an existing user
     *
     * @param request existing user's credentials
     * @return a LoginResponse object with two valid JWT tokens
     */
    TokenDto login(LoginDto request);
}
