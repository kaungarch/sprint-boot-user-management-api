package com.art.usermanagement.exception;

public class AccountAlreadyExistedException extends RuntimeException {
    public AccountAlreadyExistedException(String message)
    {
        super(message);
    }
}
