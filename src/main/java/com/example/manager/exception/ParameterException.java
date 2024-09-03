package com.example.manager.exception;

public class ParameterException extends RestException{

    public ParameterException(String message) {
        super(message, 400);
    }
    
}
