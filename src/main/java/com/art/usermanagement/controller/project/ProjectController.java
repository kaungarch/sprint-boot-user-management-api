package com.art.usermanagement.controller.project;

import com.art.usermanagement.dto.request.ProjectCreationRequest;
import com.art.usermanagement.dto.request.ProjectSearchQueryParam;
import com.art.usermanagement.dto.request.ProjectUpdateRequest;
import com.art.usermanagement.dto.response.ApiResponse;
import com.art.usermanagement.dto.response.PaginationDto;
import com.art.usermanagement.dto.response.ProjectSummaryDto;
import com.art.usermanagement.security.AccountDetails;
import com.art.usermanagement.service.project.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("${apiPrefix}/projects")
public class ProjectController {

    private ProjectService projectService;

    public ProjectController(ProjectService projectService)
    {
        this.projectService = projectService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('SUPER_USER', 'USER')")
    public ResponseEntity<ApiResponse<ProjectSummaryDto>> getProject(@PathVariable UUID id, @AuthenticationPrincipal AccountDetails currentUser)
    {
//        TODO: This route can be accessed by both user and super user. But user can access his own project.
        ProjectSummaryDto project = this.projectService.findProjectById(id, currentUser);
        ApiResponse<ProjectSummaryDto> apiResponse = ApiResponse.<ProjectSummaryDto>builder()
                .status(HttpStatus.OK.value())
                .message("Project retrieved successfully")
                .data(project)
                .timestamp(LocalDateTime.now())
                .errors(null)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('SUPER_USER', 'USER')")
    public ResponseEntity<ApiResponse<PaginationDto>> filterProjects(@Validated @ModelAttribute ProjectSearchQueryParam param)
    {
        PaginationDto paginationDto = this.projectService.filterProjects(param);
        ApiResponse<PaginationDto> apiResponse = ApiResponse.<PaginationDto>builder()
                .status(HttpStatus.OK.value())
                .message("Projects retrieved successfully")
                .data(paginationDto)
                .timestamp(LocalDateTime.now())
                .errors(null)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ApiResponse<ProjectSummaryDto>> createProject(@RequestBody @Validated ProjectCreationRequest projectCreationRequest)
    {
        AccountDetails principal = (AccountDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ProjectSummaryDto project = this.projectService.createProject(projectCreationRequest, principal.getAccountId());
        ApiResponse<ProjectSummaryDto> apiResponse = ApiResponse.<ProjectSummaryDto>builder()
                .status(HttpStatus.CREATED.value())
                .message("Project created successfully")
                .data(project)
                .timestamp(LocalDateTime.now())
                .errors(null)
                .build();
        URI uri = ServletUriComponentsBuilder.fromPath("/projects/{id}").build(project.getId());
        return ResponseEntity.created(uri).body(apiResponse);
    }

    @PatchMapping("/{id}/update")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ApiResponse<ProjectSummaryDto>> updateProject(@PathVariable UUID id,
                                                                        @RequestBody @Validated ProjectUpdateRequest projectUpdateRequest)
    {
        AccountDetails principal = (AccountDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ProjectSummaryDto updatedProject = this.projectService.updateProject(projectUpdateRequest, id, principal.getAccountId());
        ApiResponse<ProjectSummaryDto> apiResponse = ApiResponse.<ProjectSummaryDto>builder()
                .status(HttpStatus.OK.value())
                .message("Project updated successfully")
                .data(updatedProject)
                .timestamp(LocalDateTime.now())
                .errors(null)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping("/{id}/erase")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ApiResponse<Void>> eraseProject(@PathVariable UUID id)
    {
        AccountDetails principal = (AccountDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        this.projectService.eraseProject(id, principal.getAccountId());
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Project erased successfully")
                .data(null)
                .timestamp(LocalDateTime.now())
                .errors(null)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ApiResponse<Void>> deleteProject(@PathVariable UUID id)
    {
        AccountDetails principal = (AccountDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        this.projectService.deleteProject(id, principal.getAccountId());
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Project deleted successfully")
                .timestamp(LocalDateTime.now())
                .data(null)
                .errors(null)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

}
