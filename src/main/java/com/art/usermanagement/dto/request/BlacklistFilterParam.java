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
public class BlacklistFilterParam {
    @Builder.Default
    @Min(value = 1, message = "Page should be at least 1.")
    int page = 1;

    @Builder.Default
    @Min(value = 1, message = "Size should be at least 1.")
    int size = 10;

    @Builder.Default
    @Pattern(regexp = "asc|desc", message = "Order must be asc or desc.")
    String order = "asc"; // asc, desc

    @Builder.Default
    @Pattern(regexp = "phoneNumber|nrcNumber", message = "SortBy should be phoneNumber or nrcNumber.")
    String sortBy = "phoneNumber"; // title, createdAt, updatedAt
}
