package com.example.manager.security;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.manager.dto.AuthUser;

import jakarta.servlet.http.HttpServletRequest;

public class SecurityContext {

    public static AuthUser getCurrentUser() {

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (requestAttributes == null) {
            return null;
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        return (AuthUser) request.getAttribute(ServletAttributes.AUTH_USER);
    }

    public static Long getCurrentUserId() {

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (requestAttributes == null) {
            return null;
        }

        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        return (Long) request.getAttribute(ServletAttributes.AUTH_USER_ID);
    }

}
