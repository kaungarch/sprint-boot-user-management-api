package com.art.usermanagement.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountSignInRequest {

    @Pattern(regexp = "^959\\d{9}$", message = "Phone number must start with 959 and contain exactly 12 digits.")
    private String phoneNumber;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,20}$",
            message = "Password should contain at least one small letter, one capital letter, one digit, one special " +
                    "symbol, should be 8 characters long and shouldn't be exceed 20 characters."
    )
    private String password;

}
