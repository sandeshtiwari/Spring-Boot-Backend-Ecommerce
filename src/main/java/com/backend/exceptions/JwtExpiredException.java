package com.backend.exceptions;

public class JwtExpiredException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public JwtExpiredException(String message) {
        super(message);
    }
}
