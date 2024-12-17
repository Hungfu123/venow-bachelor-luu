package org.venow.backend.service.exception;

public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String s) {
        super(s);
    }
}
