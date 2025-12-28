package com.art.usermanagement.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Setter;

public record RegistrationRequest(
//        @NotEmpty(message = "Name is required.")
        @Size(min = 3, message = "Name should be at least 3 characters long.")
        String name,

//        @NotEmpty(message = "Password is required.")
//        @Min(value = 8, message = "Password should be at least 8 characters long.")
//        @Max(value = 20, message = "Password shouldn't be exceed 20 characters.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,20}$",
                message = "Password should contain at least one small letter, one capital letter, one digit, one special " +
                        "symbol, should be 8 characters long and shouldn't be exceed 20 characters."
        )
        String password,

        @NotEmpty(message = "phone number must not be empty")
        @Pattern(regexp = "^959\\d{9}$", message = "Phone number must start with 959 and contain exactly 12 digits.")
        String phoneNumber,

        @NotEmpty(message = "nrc number must not be empty")
        @Pattern(regexp = "^(1[0-4]|[1-9])/([A-Za-z]+)\\(C\\)\\d{6}$", message = "Nrc number must be in correct " +
                "format.")
        String nrcNumber
) {
}
