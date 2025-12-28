package com.art.usermanagement.repository.project;

import com.art.usermanagement.dto.request.ProjectSearchQueryParam;
import com.art.usermanagement.model.Project;

import java.util.List;
import java.util.UUID;

public interface ProjectCriteriaRepo {
    List<Project> search(UUID accountId, boolean isSuperUser, ProjectSearchQueryParam param);

    long getCount(ProjectSearchQueryParam param, boolean isSuperUser, UUID accountId);
}
