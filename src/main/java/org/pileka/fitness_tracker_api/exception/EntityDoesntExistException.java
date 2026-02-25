package org.pileka.fitness_tracker_api.exception;

public class EntityDoesntExistException extends RuntimeException {
    public EntityDoesntExistException(String message) {
        super(message);
    }
}
