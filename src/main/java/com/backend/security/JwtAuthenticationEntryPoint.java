package com.backend.security;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.backend.exceptions.JwtExpiredException;
import com.backend.exceptions.UserNotFoundException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // @Override
    // public void commence(HttpServletRequest request, HttpServletResponse
    // response,
    // AuthenticationException authException) throws IOException {
    // // This is where you can use your JwtExpiredException or customize the
    // response
    // // based on authException
    // response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: JWT
    // token is expired or invalid");
    // }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String message;
        if (authException.getCause() instanceof JwtExpiredException) {
            message = "{\"error\": \"JWT token has expired\", \"status\": \"401\"}";
        } else if (authException.getCause() instanceof UserNotFoundException) {
            message = "{\"error\": \"User not found\", \"status\": 404}";
        } else {
            message = "{\"error\": \"" + authException.getMessage() + "\", \"status\": \"401\"}";
        }

        response.getWriter().write(message);
    }
}