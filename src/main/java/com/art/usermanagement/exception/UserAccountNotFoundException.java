package com.art.usermanagement.exception;

public class UserAccountNotFoundException extends RuntimeException {
    public UserAccountNotFoundException(String message)
    {
        super(message);
    }
}
