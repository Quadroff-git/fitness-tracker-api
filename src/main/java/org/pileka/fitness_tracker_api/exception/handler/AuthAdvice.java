package org.pileka.fitness_tracker_api.exception.handler;

import org.pileka.fitness_tracker_api.controller.AuthController;
import org.pileka.fitness_tracker_api.exception.RefreshTokenInvalidException;
import org.pileka.fitness_tracker_api.exception.UserLoginFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(assignableTypes = {AuthController.class})
public class AuthAdvice {
    @ExceptionHandler({UserLoginFailedException.class,
    RefreshTokenInvalidException.class})
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    ResponseEntity<String> handleGenericUnauthorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
