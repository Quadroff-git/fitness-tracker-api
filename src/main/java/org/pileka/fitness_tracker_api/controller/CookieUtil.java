package org.pileka.fitness_tracker_api.controller;

import org.springframework.http.ResponseCookie;

public class CookieUtil {
    /**
     * A factory method creating refresh token cookies
     *
     * @param refreshToken refresh token string representation
     * @param refreshTokenExpiration refresh token expiration (in seconds)
     * @return the cookie to return in the response
     */
    public static ResponseCookie getRefreshTokenCookie(String refreshToken, long refreshTokenExpiration) {
        return ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/api/auth/refresh")
                .maxAge(refreshTokenExpiration)
                .sameSite("strict")
                .build();
    }
}
