package org.venow.backend.applicationservice.exception;

public class AlreadyRejectedException extends RuntimeException {
    public AlreadyRejectedException(String message) {
        super(message);
    }
}
