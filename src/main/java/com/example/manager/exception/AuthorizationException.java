package com.example.manager.exception;

public class AuthorizationException extends RestException {

    public AuthorizationException(String message) {
        super(message, 401);
    }

}
