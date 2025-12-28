package com.art.usermanagement.exception;

public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException(String message)
    {
        super(message);
    }
}
