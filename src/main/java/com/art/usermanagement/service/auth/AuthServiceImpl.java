package com.art.usermanagement.service.auth;

import com.art.usermanagement.controller.security.jwt.JwtService;
import com.art.usermanagement.dto.request.AccountSignInRequest;
import com.art.usermanagement.dto.response.AuthResponse;
import com.art.usermanagement.exception.AuthenticationFailedException;
import com.art.usermanagement.model.Account;
import com.art.usermanagement.model.AccountStatus;
import com.art.usermanagement.security.AccountDetails;
import com.art.usermanagement.service.account.AccountService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AccountService accountService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public AuthServiceImpl(BCryptPasswordEncoder passwordEncoder, JwtService jwtService, AccountService accountService)
    {
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.accountService = accountService;
    }

    @Override
    public AuthResponse signIn(AccountSignInRequest request)
    {
        Account account = this.accountService.loadAccountByPhoneNumber(request.getPhoneNumber());
        boolean arePassEqual = this.passwordEncoder.matches(request.getPassword(), account.getPassword());

        if (!arePassEqual || (account.getStatus() != AccountStatus.APPROVED))
            throw new AuthenticationFailedException("Invalid phone number or password.");

//        create tokens
        String refreshToken = this.jwtService.generateRefreshToken(AccountDetails.of(account));
        String accessToken = this.jwtService.generateAccessToken(AccountDetails.of(account));

        return new AuthResponse(refreshToken, accessToken);
    }

    @Override
    public AuthResponse refresh(HttpServletRequest servletRequest)
    {
        String refreshToken = extractRefreshToken(servletRequest);
        boolean isTokenValid = this.jwtService.isValid(refreshToken);

        if (!isTokenValid)
            throw new BadCredentialsException("The provided refresh token is invalid, expired, or has been revoked.");

        String phoneNumber = (String) this.jwtService.getClaim(refreshToken, (claims -> claims.get("phoneNumber")));

        Account account = this.accountService.loadAccountByPhoneNumber(phoneNumber);
        String accessToken = this.jwtService.generateAccessToken(AccountDetails.of(account));
        return new AuthResponse(null, accessToken);
    }

    private static String extractRefreshToken(HttpServletRequest servletRequest)
    {
        Cookie[] cookies = servletRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh-token"))
                    return cookie.getValue();
            }
        }
        throw new BadCredentialsException("The provided refresh token is invalid, expired, or has been revoked.");
    }
}
