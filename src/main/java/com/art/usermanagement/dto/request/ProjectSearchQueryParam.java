package com.art.usermanagement.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectSearchQueryParam {
    @Min(value = 1, message = "Page should be at least 1.")
    int page = 1;

    @Min(value = 1, message = "Size should be at least 1.")
    int size = 10;

    @Pattern(regexp = "asc|desc", message = "Order must be asc or desc.")
    String order = "asc"; // asc, desc

    @Pattern(regexp = "title|createdAt|updatedAt", message = "SortBy should be title or createdAt or updatedAt.")
    String sortBy = "title"; // title, createdAt, updatedAt

}
