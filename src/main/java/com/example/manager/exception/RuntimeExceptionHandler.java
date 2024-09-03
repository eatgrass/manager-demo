package com.example.manager.exception;

import java.io.IOException;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.manager.security.ServletAttributes;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class RuntimeExceptionHandler {

    @ExceptionHandler(RestException.class)
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            RestException e) throws ServletException, IOException {

        request.setAttribute(ServletAttributes.LOG_MESSAGE, e.getMessage());
        response.sendError(e.getStatus());

    }

}
