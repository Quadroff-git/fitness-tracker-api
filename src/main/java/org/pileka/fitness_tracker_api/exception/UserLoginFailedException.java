package org.pileka.fitness_tracker_api.exception;

public class UserLoginFailedException extends RuntimeException {
    public UserLoginFailedException(String message) {
        super(message);
    }
    public UserLoginFailedException(Throwable throwable) {
        super(throwable);
    }
}
