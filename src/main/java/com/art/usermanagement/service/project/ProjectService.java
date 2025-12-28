package com.art.usermanagement.service.project;

import com.art.usermanagement.dto.request.ProjectCreationRequest;
import com.art.usermanagement.dto.request.ProjectSearchQueryParam;
import com.art.usermanagement.dto.request.ProjectUpdateRequest;
import com.art.usermanagement.dto.response.PaginationDto;
import com.art.usermanagement.dto.response.ProjectSummaryDto;
import com.art.usermanagement.security.AccountDetails;

import java.util.UUID;

public interface ProjectService {

    ProjectSummaryDto createProject(ProjectCreationRequest request, UUID accountId);

    ProjectSummaryDto updateProject(ProjectUpdateRequest request, UUID projectId, UUID accountId);

    void eraseProject(UUID projectId, UUID accountId); // soft delete

    void deleteProject(UUID projectId, UUID accountId); // hard delete

    ProjectSummaryDto findProjectById(UUID projectId, AccountDetails currentUser);

    // filter methods
    PaginationDto filterProjects(ProjectSearchQueryParam param); // user
}
