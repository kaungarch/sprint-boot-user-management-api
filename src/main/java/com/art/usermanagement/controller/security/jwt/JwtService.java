package com.art.usermanagement.controller.security.jwt;

import com.art.usermanagement.security.AccountDetails;
import io.jsonwebtoken.Claims;

import java.util.function.Function;

public interface JwtService {
    String generateAccessToken(AccountDetails accountDetails);

    String generateRefreshToken(AccountDetails accountDetails);

    boolean isValid(String token);

    <T> T getClaim(String token, Function<Claims, T> claimsResolver);
}
