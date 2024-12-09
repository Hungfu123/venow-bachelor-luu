package org.venow.backend.applicationservice.exception;

public class ApplicationNotFoundException extends RuntimeException{
    public ApplicationNotFoundException(String message){
        super(message);
    }
}
