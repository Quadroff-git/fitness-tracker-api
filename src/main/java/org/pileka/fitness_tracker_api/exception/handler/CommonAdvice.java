package org.pileka.fitness_tracker_api.exception.handler;

import org.pileka.fitness_tracker_api.exception.EntityDoesntBelongToUserException;
import org.pileka.fitness_tracker_api.exception.EntityRestrictionViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CommonAdvice {
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityDoesntBelongToUserException.class)
    public ResponseEntity<String> handleEntityDoesntBelongToUser() {
        // Potentially log this since it might be done on purporse
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
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
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ResponseEntity<String> handleGenericBadRequest(EntityRestrictionViolationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleDataAccessException(DataAccessException e) {
        return ResponseEntity.internalServerError().body("Something went wrong when accessing the database:\n" + e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(code= HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleGenericInternalError(RuntimeException e) {
        return ResponseEntity.internalServerError().body("Unexpected error happened:\n" + e.getMessage());
    }
}
