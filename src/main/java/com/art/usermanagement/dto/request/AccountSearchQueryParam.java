package com.art.usermanagement.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountSearchQueryParam {
    @Min(1)
    int page = 1;

    @Min(1)
    int size = 10;

    @Pattern(regexp = "asc|desc", message = "Order must be asc or desc.")
    String order = "asc";

    @Pattern(regexp = "name|phoneNumber|nrcNumber", message = "SortBy must be name or phoneNumber or nrcNumber.")
    String sortBy = "name";

    @Pattern(regexp = "REGISTERED|APPROVED|REJECTED|BLOCKED", message = "Invalid status")
    String status;

    @Pattern(regexp = "USER|SUPER_USER", message = "Invalid role")
    String role;
}
