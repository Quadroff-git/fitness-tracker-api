package org.pileka.fitness_tracker_api.exception.handler;

import org.pileka.fitness_tracker_api.controller.AuthController;
import org.pileka.fitness_tracker_api.exception.RefreshTokenInvalidException;
import org.pileka.fitness_tracker_api.exception.UserLoginFailedException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = {AuthController.class})
public class AuthAdvice {
    @ExceptionHandler({UserLoginFailedException.class,
    RefreshTokenInvalidException.class})
    ResponseEntity<String> handleGenericUnauthorized() {
        return ResponseEntity.status(401).build();
    }
}
