package com.app.Q_Entertainment.Exception;

public class InvalidUsernamePasswordException extends RuntimeException {
    public InvalidUsernamePasswordException(String message) {
        super(message);
    }
}
