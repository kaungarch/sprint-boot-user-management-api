package com.art.usermanagement.exception;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message)
    {
        super(message);
    }
}
