package com.art.usermanagement.service.auth;

import com.art.usermanagement.dto.request.AccountSignInRequest;
import com.art.usermanagement.dto.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    AuthResponse signIn(AccountSignInRequest request);
    AuthResponse refresh(HttpServletRequest servletRequest);
}
