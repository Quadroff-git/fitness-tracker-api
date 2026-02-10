package org.pileka.fitness_tracker_api.exception.handler;

import org.pileka.fitness_tracker_api.exception.EntityDoesntBelongToUserException;
import org.pileka.fitness_tracker_api.exception.EntityRestrictionViolationException;
import org.pileka.fitness_tracker_api.exception.InvalidFileUploadedException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
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
        // Potentially log this since it might be done on purpose
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

    @ExceptionHandler(EntityRestrictionViolationException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ErrorResponse handleConflict(EntityRestrictionViolationException e) {
        return ErrorResponse.create(e, HttpStatus.CONFLICT, "Data integrity conflict: " + e.getMessage());
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, InvalidFileUploadedException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleGenericBadRequest(RuntimeException e) {
        return ErrorResponse.create(e, HttpStatus.BAD_REQUEST, "Bad request: " + e.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleDataAccessException(DataAccessException e) {
        return ErrorResponse.create(e, HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong while accessing the database:" + e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(code= HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericInternalError(RuntimeException e) {
        return ErrorResponse.create(e, HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error happened: " + e.getMessage());
    }
}
