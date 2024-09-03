package com.example.manager.security;

import java.io.IOException;
import java.util.Base64;

import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.manager.dto.AuthUser;
import com.example.manager.exception.AuthorizationException;
import com.example.manager.exception.UnauthorizedException;
import com.example.manager.utils.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 检查Authorization请求头
 * 解码并反序列后交由鉴权器 {@see Authorizer} 做权限检查
 */
public class AuthorizationFilter extends OncePerRequestFilter {

    private ObjectMapper objectMapper = new ObjectMapper();

    private Authorizer authorizer;

    public AuthorizationFilter(Authorizer authorizer) {
        super();
        this.authorizer = authorizer;
    }

    private static final String AUTH_HEADER = "Authorization";

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws ServletException, IOException {
        try {
            auth(request, response, chain);
        } catch (AuthorizationException e) {
            forwardError(401, request, response, e);
        } catch (UnauthorizedException e) {
            forwardError(403, request, response, e);
        }

    }

    private void forwardError(int code, HttpServletRequest request, HttpServletResponse response, Exception e)
            throws ServletException, IOException {
        request.setAttribute(RequestDispatcher.ERROR_EXCEPTION, e);
        request.setAttribute(RequestDispatcher.ERROR_MESSAGE, e.getMessage());
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, code);
        request.setAttribute(ServletAttributes.LOG_MESSAGE, e.getMessage());
        response.setStatus(code);
        request.getRequestDispatcher("/error").forward(request, response);
    }

    private void auth(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String token = request.getHeader(AUTH_HEADER);

        if (StringUtils.isEmpty(token)) {
            throw new AuthorizationException("token required");
        }

        AuthUser user;

        try {
            byte[] tokenBytes = Base64.getDecoder().decode(token);
            String jsonStr = new String(tokenBytes, "UTF-8");
            user = objectMapper.readValue(jsonStr, AuthUser.class);

        } catch (Exception e) {
            throw new AuthorizationException("invalid token");
        }

        authorizer.authorize(request, user);
        chain.doFilter(request, response);
    }

}
