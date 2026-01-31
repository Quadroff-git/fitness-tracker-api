package org.pileka.fitness_tracker_api.service;

import org.pileka.fitness_tracker_api.dto.auth.LoginDto;
import org.pileka.fitness_tracker_api.dto.auth.TokenDto;
import org.pileka.fitness_tracker_api.dto.auth.RegistrationDto;
import org.pileka.fitness_tracker_api.exception.EntityRestrictionViolationException;
import org.pileka.fitness_tracker_api.exception.RefreshTokenInvalidException;
import org.pileka.fitness_tracker_api.exception.UserLoginFailedException;

/**
 * Authentication service class
 */
public interface AuthService {
    /**
     * Register a new user
     *
     * @param request values to create the new user with
     * @return true if registration is creation is successful
     * @throws EntityRestrictionViolationException Thrown when any restrictions associated with user information are violated
     * (typically related to value uniqueness)
     */
    void register(RegistrationDto request) throws EntityRestrictionViolationException;

    /**
     * Login an existing user
     *
     * @param request existing user's credentials
     * @return a TokenDto object with two valid JWT tokens
     * @throws UserLoginFailedException Thrown when the authentication process fails
     */
    TokenDto login(LoginDto request) throws UserLoginFailedException;

    /**
     * Return new bearer and refresh token if the provided refresh token is valid
     *
     * @param refreshToken refresh token
     * @return a TokenDto object with two fresh tokens
     * @throws RefreshTokenInvalidException Thrown when an invalid refresh token is supplied
     */
    TokenDto refresh(String refreshToken) throws RefreshTokenInvalidException;
}
