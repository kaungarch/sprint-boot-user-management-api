package com.art.usermanagement.controller.security.jwt;

import com.art.usermanagement.security.AccountDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.refresh.token.expiration}")
    private long refreshTokenExp;

    @Value("${jwt.access.token.expiration}")
    private long accessTokenExp;

    @Override
    public String generateAccessToken(AccountDetails accountDetails)
    {
        Map<String, Object> claims = new HashMap<>();
        claims.put("phoneNumber", accountDetails.getUsername());
        claims.put("nrcNumber", accountDetails.getNrcNumber());

        long expirationInMillis = Long.sum(accessTokenExp, System.currentTimeMillis());

        return Jwts.builder()
                .id(accountDetails.getAccountId().toString())
                .issuedAt(new Date())
                .signWith(this.getKey(), SignatureAlgorithm.HS256)
//                .signWith(this.getKey())
                .claims(claims)
                .expiration(
                        new Date(expirationInMillis)
                ).compact();
    }

    @Override
    public String generateRefreshToken(AccountDetails accountDetails)
    {
        long expirationInMillis = Long.sum(this.refreshTokenExp, System.currentTimeMillis());

        Map<String, String> claims = new HashMap<>();
        claims.put("phoneNumber", accountDetails.getUsername());

        return Jwts.builder()
                .id(accountDetails.getAccountId().toString())
                .issuedAt(new Date())
                .signWith(this.getKey(), SignatureAlgorithm.HS256)
                .claims(claims)
                .expiration(
                        new Date(expirationInMillis)
                ).compact();
    }

    @Override
    public boolean isValid(String token)
    {
        try {
            Date expirationDate = this.getClaim(token, Claims::getExpiration);
            return expirationDate.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver)
    {
        return claimsResolver.apply(this.extractAllClaims(token));
    }

    private Claims extractAllClaims(String token)
    {
        try {
            return Jwts.parser()
                    .verifyWith(this.getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            throw new RuntimeException("Failed to extract claims from jwt");
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Failed to extract claims fro jwt");
        }
    }

    private SecretKey getKey()
    {
        byte[] bytes = Base64.getDecoder().decode(this.jwtSecret.getBytes());
        return Keys.hmacShaKeyFor(bytes);
    }
}
