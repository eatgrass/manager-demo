package com.example.manager.exception;

public class UnauthorizedException extends RestException {

    public UnauthorizedException(String message) {
        super(message, 403);
    }

}
