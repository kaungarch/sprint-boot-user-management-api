package com.art.usermanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProjectDetailsDto extends ProjectDto {

    private UUID id;
    private String title;
    private String description;
    private boolean isDeleted;

}
