package com.art.usermanagement.exception;

public class BlacklistNotFoundException extends RuntimeException {
    public BlacklistNotFoundException(String message)
    {
        super(message);
    }
}
