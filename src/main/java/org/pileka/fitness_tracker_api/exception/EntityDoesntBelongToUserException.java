package org.pileka.fitness_tracker_api.exception;

public class EntityDoesntBelongToUserException extends RuntimeException {
    public EntityDoesntBelongToUserException(String message) {
        super(message);
    }
}
