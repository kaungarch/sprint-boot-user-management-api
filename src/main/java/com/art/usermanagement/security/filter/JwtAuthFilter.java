package com.art.usermanagement.security.filter;

import com.art.usermanagement.controller.security.jwt.JwtService;
import com.art.usermanagement.dto.response.ApiResponse;
import com.art.usermanagement.exception.AccessDeniedException;
import com.art.usermanagement.model.AccountStatus;
import com.art.usermanagement.security.AccountDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    private UserDetailsService userDetailsService;
    @Value("${apiPrefix}")
    private String apiPrefix;
    @Autowired
    @Qualifier("handlerExceptionResolver") // because spring found 2 beans
    private HandlerExceptionResolver exceptionResolver;

    public JwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService)
    {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        try {
            String accessToken = this.extractBearerToken(request);
            boolean isTokenValid = this.jwtService.isValid(accessToken);

            if (!isTokenValid) {
                this.exceptionResolver.resolveException(request, response, null, new AccessDeniedException("Token is " +
                        "expired or invalid."));
                return;
            }

            String phoneNumber = (String) this.jwtService.getClaim(accessToken, (claims) -> claims.get("phoneNumber"));

            AccountDetails accountDetails = (AccountDetails) this.userDetailsService.loadUserByUsername(phoneNumber);

            if (accountDetails.getAccountStatus() != AccountStatus.APPROVED) {
                AccessDeniedException accessDeniedException = new AccessDeniedException("You don't have permission to access this route.");
                this.exceptionResolver.resolveException(request, response, null, accessDeniedException);
                return;
            }

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(accountDetails, null, accountDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            exceptionResolver.resolveException(request, response, null, e);
        }
    }

    private String extractBearerToken(HttpServletRequest request) throws AccessDeniedException
    {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null)
            throw new AccessDeniedException("You don't have permission to access this route.");

        return authHeader.substring(7);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
    {
        List<String> publicEndpoints = List.of(apiPrefix + "/auth/*",
                apiPrefix + "/accounts/register",
                "/swagger-ui/*",
                "/v3/api-docs",
                "/v3/api-docs/*"
        );
        return publicEndpoints.stream()
                .map(PathPatternRequestMatcher::pathPattern)
                .anyMatch(matcher -> matcher.matches(request));
    }

}
