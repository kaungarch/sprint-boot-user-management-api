package com.art.usermanagement.repository.project;

import com.art.usermanagement.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectRepo extends JpaRepository<Project, UUID> {
    int countByAccountId(UUID accountId);
}
