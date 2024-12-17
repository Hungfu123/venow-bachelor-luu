package org.venow.backend.service.exception;

public class AlreadyRejectedException extends RuntimeException {
    public AlreadyRejectedException(String message) {
        super(message);
    }
}
