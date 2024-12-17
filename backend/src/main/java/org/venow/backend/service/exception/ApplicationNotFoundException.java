package org.venow.backend.service.exception;

public class ApplicationNotFoundException extends RuntimeException{
    public ApplicationNotFoundException(String message){
        super(message);
    }
}
