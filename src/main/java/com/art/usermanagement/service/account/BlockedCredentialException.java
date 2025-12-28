package com.art.usermanagement.service.account;

public class BlockedCredentialException extends RuntimeException {
    public BlockedCredentialException(String message)
    {
        super(message);
    }
}
