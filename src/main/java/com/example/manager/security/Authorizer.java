package com.example.manager.security;

import com.example.manager.dao.UserRepository;
import com.example.manager.dto.AuthUser;
import com.example.manager.exception.UnauthorizedException;
import com.example.manager.model.Role;

import jakarta.servlet.http.HttpServletRequest;

public class Authorizer {

    protected Role allowedRole;

    public Authorizer(UserRepository userRepository, Role allowed) {
        this.allowedRole = allowed;
    }

    protected void authorize(HttpServletRequest request, AuthUser user) {
        if (allowedRole != user.role()) {
            throw new UnauthorizedException("permission denied for role [" + user.role() + "]");
        }

        request.setAttribute(ServletAttributes.AUTH_USER, user);
        request.setAttribute(ServletAttributes.AUTH_USER_ID, user.userId());
    }

}
