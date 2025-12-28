package com.art.usermanagement.exception;

import com.art.usermanagement.dto.response.ApiResponse;
import jakarta.validation.constraints.Null;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException ex)
    {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach((fieldError) -> {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .data(null)
                .message("Validation failed")
                .timestamp(LocalDateTime.now())
                .errors(errors)
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUsernameNotFoundException(UsernameNotFoundException ex)
    {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message("Account not found")
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException ex)
    {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .data(null)
                .message("Access denied.")
                .status(HttpStatus.FORBIDDEN.value())
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiResponse);
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationFailedException(AuthenticationFailedException ex)
    {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message("Authentication failed")
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex)
    {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "The request body is invalid or cannot be read.");
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .data(null)
                .message("Malformed JSON request")
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthorizationDeniedException(AuthorizationDeniedException ex)
    {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "You do not have permission to access this resource.");
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .status(HttpStatus.FORBIDDEN.value())
                .message("Access denied")
                .errors(errors)
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiResponse);
    }

    @ExceptionHandler(AlreadyExistedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAlreadyExistedException(AlreadyExistedException ex)
    {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .status(HttpStatus.CONFLICT.value())
                .message("Data integrity violation")
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex)
    {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "The value provided in the URL path is not the correct data type.");

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .status(HttpStatus.CONFLICT.value())
                .message("Invalid parameter type")
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
    }

    @ExceptionHandler(UserAccountNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserAccountNotFoundException(UserAccountNotFoundException ex)
    {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message("Account not found")
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleProjectNotFoundException(ProjectNotFoundException ex)
    {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message("Project not found")
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @ExceptionHandler(BlacklistNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleBlacklistNotFoundException(BlacklistNotFoundException ex)
    {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message("Blacklist record not found")
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoResourceFoundException(NoResourceFoundException ex)
    {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "The requested API endpoint does not exist or the path is incorrect.");
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message("Resource not found")
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }

}
