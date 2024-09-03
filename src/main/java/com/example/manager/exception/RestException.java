package com.example.manager.exception;

public class RestException extends RuntimeException {

    private int status;

    public RestException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

}
