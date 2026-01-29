package org.pileka.fitness_tracker_api.security;
/**
 * A utility class for JWT tokens
 * */
public interface JwtTokenProvider {
    /**
     * Generate a bearer token
     *
     * @param username the username of the user to whom the bearer token will be issued
     * @return a valid JWT token with the supplied username as the subject
     * and an expiration date as specified in application.properties
     * */
    String generateBearerToken(String username);

    /**
     * Generate a refresh token
     *
     * @param username the username of the user to whom the refresh token will be issued
     * @return a valid JWT token with the supplied username as the subject
     * and an expiration date as specified in application.properties
     */
    String generateRefreshToken(String username);

    /**
     * Check if a token is a valid token issued by this implementation
     *
     * @param token the JWT token
     * @return true if token is valid, false otherwise
     */
    boolean tokenIsValid(String token);

    /**
     * Extract the username of the user to whom the token was issued
     *
     * @param token the JWT token
     * @return username of the user to whom the token was issued
     */
    String getUsernameFromToken(String token);
}
