package org.pileka.fitness_tracker_api.exception.handler;

import org.pileka.fitness_tracker_api.exception.EntityDoesntBelongToUserException;
import org.pileka.fitness_tracker_api.exception.EntityRestrictionViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CommonAdvice {
    @ExceptionHandler(EntityDoesntBelongToUserException.class)
    public ResponseEntity<String> handleEntityDoesntBelongToUser() {
        // Potentially log this since it might be done on purporse
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errorInfo = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();

            errorInfo.put(fieldName, message);
        });

        return ResponseEntity.badRequest().body(errorInfo.toString());
    }

    @ExceptionHandler({EntityRestrictionViolationException.class,
            HttpMessageNotReadableException.class,})
    public ResponseEntity<String> handleGenericBadRequest(EntityRestrictionViolationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDataAccessException(DataAccessException e) {
        return ResponseEntity.internalServerError().body("Something went wrong when accessing the database:\n" + e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleGenericInternalError(RuntimeException e) {
        return ResponseEntity.internalServerError().body("Unexpected error happened:\n" + e.getMessage());
    }
}
