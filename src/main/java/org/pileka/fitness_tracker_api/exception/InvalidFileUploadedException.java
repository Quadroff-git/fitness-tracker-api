package org.pileka.fitness_tracker_api.exception;

public class InvalidFileUploadedException extends RuntimeException {
    public InvalidFileUploadedException(String message) {
        super(message);
    }
}
