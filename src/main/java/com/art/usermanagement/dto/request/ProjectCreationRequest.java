package com.art.usermanagement.dto.request;

import com.art.usermanagement.model.Project;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProjectCreationRequest(
        @NotBlank(message = "Title is required.")
        @Size(min = 3, message = "Title should be at least 3 characters long.")
        String title,
        @NotBlank(message = "Description is required.")
        @Size(min = 5, message = "Description should be at least 5 characters long.")
        String description
) {
    public static Project buildProject(ProjectCreationRequest request)
    {
        return Project.builder()
                .id(null)
                .title(request.title)
                .description(request.description)
                .account(null)
                .build();
    }
}
