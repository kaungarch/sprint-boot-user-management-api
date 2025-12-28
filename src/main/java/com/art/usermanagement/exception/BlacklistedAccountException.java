package com.art.usermanagement.exception;

public class BlacklistedAccountException extends RuntimeException {
    public BlacklistedAccountException(String message)
    {
        super(message);
    }
}
