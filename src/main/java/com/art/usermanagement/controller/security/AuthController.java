package com.art.usermanagement.controller.security;

import com.art.usermanagement.dto.request.AccountSignInRequest;
import com.art.usermanagement.dto.response.ApiResponse;
import com.art.usermanagement.dto.response.AuthResponse;
import com.art.usermanagement.service.auth.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "${apiPrefix}/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService)
    {
        this.authService = authService;
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<Object>> signIn(@RequestBody @Valid AccountSignInRequest request,
                                               HttpServletResponse response,
                                              @Value("${jwt.refresh.token.expiration}") long refreshTokenExp) throws IOException
    {
        long sessionCookieAgeInSeconds = refreshTokenExp / 1000L;
        AuthResponse authResponse = this.authService.signIn(request);
//        set response
        Cookie cookie = new Cookie("refresh-token", authResponse.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(Long.valueOf(sessionCookieAgeInSeconds).intValue());
        cookie.setSecure(true);
        response.addCookie(cookie);

//        body
        Map<String, String> resBody = new HashMap<>();
        resBody.put("accessToken", authResponse.getAccessToken());

        ApiResponse<Object> apiResponse = ApiResponse.builder()
                .data(resBody)
                .status(HttpStatus.OK.value())
                .message("Signin succeed.")
                .errors(null)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + authResponse.getAccessToken())
                .body(apiResponse);
    }
}
