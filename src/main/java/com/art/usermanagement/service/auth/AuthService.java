package com.art.usermanagement.service.auth;

import com.art.usermanagement.dto.request.AccountSignInRequest;
import com.art.usermanagement.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse signIn(AccountSignInRequest request);
}
