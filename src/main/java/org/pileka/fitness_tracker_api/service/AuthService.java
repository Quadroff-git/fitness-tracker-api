package org.pileka.fitness_tracker_api.service;

import org.pileka.fitness_tracker_api.dto.auth.LoginDto;
import org.pileka.fitness_tracker_api.dto.auth.TokenDto;
import org.pileka.fitness_tracker_api.dto.auth.RegistrationDto;
import org.pileka.fitness_tracker_api.exception.EntityRestrictionViolationException;

/**
 * Authentication service class
 */
public interface AuthService {
    /**
     * Register a new user
     *
     * @param request values to create the new user with
     * @return true if registration is creation is successful
     * @throws EntityRestrictionViolationException if any entity restrictions are violated
     * (typically related to value uniqueness)
     */
    void register(RegistrationDto request) throws EntityRestrictionViolationException;

    /**
     * Login an existing user
     *
     * @param request existing user's credentials
     * @return a TokenDto object with two valid JWT tokens
     */
    TokenDto login(LoginDto request);

    /**
     * Return new bearer and refresh token if the provided refresh token is valid
     *
     * @param refreshToken refresh token
     * @return a TokenDto object with two fresh tokens
     */
    TokenDto refresh(String refreshToken);
}
