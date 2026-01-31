package org.pileka.fitness_tracker_api.exception;

/**
 * Thrown when the formats of the values in the entity are valid, but some more complicated
 * domain- or data integrity-related restrictions are violated, e.g. email or username not unique, etc.
 */
public class EntityRestrictionViolationException extends RuntimeException {
    public EntityRestrictionViolationException(String message) {
        super(message);
    }
    public EntityRestrictionViolationException(Throwable throwable) {
        super(throwable);
    }
}
