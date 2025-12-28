package com.art.usermanagement.exception;

public class AlreadyExistedException extends RuntimeException {
    public AlreadyExistedException(String message)
    {
        super(message);
    }
}
