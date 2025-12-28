package com.art.usermanagement.service.project;

import com.art.usermanagement.dto.request.ProjectCreationRequest;
import com.art.usermanagement.dto.request.ProjectSearchQueryParam;
import com.art.usermanagement.dto.request.ProjectUpdateRequest;
import com.art.usermanagement.dto.response.PaginationDto;
import com.art.usermanagement.dto.response.ProjectDetailsDto;
import com.art.usermanagement.dto.response.ProjectDto;
import com.art.usermanagement.dto.response.ProjectSummaryDto;
import com.art.usermanagement.exception.AccessDeniedException;
import com.art.usermanagement.exception.ProjectNotFoundException;
import com.art.usermanagement.exception.UserAccountNotFoundException;
import com.art.usermanagement.model.Account;
import com.art.usermanagement.model.Project;
import com.art.usermanagement.repository.project.ProjectCriteriaRepo;
import com.art.usermanagement.repository.project.ProjectRepo;
import com.art.usermanagement.repository.account.AccountRepo;
import com.art.usermanagement.security.AccountDetails;
import com.art.usermanagement.util.PaginationHelper;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectServiceImpl implements ProjectService {

    private ProjectRepo projectRepo;
    private AccountRepo accountRepo;
    private ModelMapper modelMapper;
    private ProjectCriteriaRepo projectCriteriaRepo;

    public ProjectServiceImpl(ProjectRepo projectRepo, AccountRepo accountRepo, ModelMapper modelMapper, ProjectCriteriaRepo projectCriteriaRepo)
    {
        this.projectRepo = projectRepo;
        this.accountRepo = accountRepo;
        this.modelMapper = modelMapper;
        this.projectCriteriaRepo = projectCriteriaRepo;
    }

    @Override
    public ProjectSummaryDto createProject(ProjectCreationRequest request, UUID accountId)
    {
//        first, fetch account entity using account repo.
//        if found, then continue. if not found, throws UserAccountNotFoundException.
        Account account = this.accountRepo.findById(accountId).orElseThrow(() -> new UserAccountNotFoundException("Account with id " + accountId + " not found."));

        Project projectToBeCreated = ProjectCreationRequest.buildProject(request);
        projectToBeCreated.setAccount(account);

        Project savedProject = this.projectRepo.save(projectToBeCreated);

        return this.modelMapper.map(savedProject, ProjectSummaryDto.class);
    }

    @Override
    public ProjectSummaryDto updateProject(ProjectUpdateRequest request, UUID projectId, UUID accountId)
    {
//        fetch project by id,
//        if not found, then throws ProjectNotFoundException
        Project existedProject = this.projectRepo.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(
                "Project with id " + projectId + " not found."));

//        Compare both IDs
//        if IDs are not equal, throws AccessDeniedException
        if (!existedProject.getAccount().getId().equals(accountId))
            throw new AccessDeniedException("You can only update your own project.");

        Optional.ofNullable(request.title()).ifPresent(existedProject::setTitle);
        Optional.ofNullable(request.description()).ifPresent(existedProject::setDescription);
        Project updatedProject = this.projectRepo.save(existedProject);
        return this.modelMapper.map(updatedProject, ProjectSummaryDto.class);
    }

    @Override
    public void eraseProject(UUID projectId, UUID accountId)
    {
//        fetch project by id, if not found, then throw ProjectNotFoundException
        Project existedProject = this.projectRepo.findById(projectId).orElseThrow(() -> new ProjectNotFoundException("Project with id " + projectId + " not found."));

//        compare account id and project's account id
//        if they are not equal, throw AccessDeniedException
        if (!existedProject.getAccount().getId().equals(accountId))
            throw new AccessDeniedException("You can only erase you own project.");

//        if they are equal, erase project
        existedProject.setDeleted(true);
        this.projectRepo.save(existedProject);
    }

    @Override
    public void deleteProject(UUID projectId, UUID accountId)
    {
//        find a project by id. If not found, throw ProjectNotFoundException
        Project existedProject = this.projectRepo.findById(projectId).orElseThrow(() -> new ProjectNotFoundException("Project with id " + projectId + " not found."));

//        compare account id and project account id
        if (!existedProject.getAccount().getId().equals(accountId))
            throw new AccessDeniedException("You can only delete your own project.");

//        existedProject.setAccount(null); // this creates error Data integrity violation
//        this.projectRepo.delete(existedProject);

        Account account = this.accountRepo.findById(accountId).orElseThrow(() -> new UserAccountNotFoundException("Account with id " + accountId + " not found."));

        account.getProjectList().remove(existedProject);
        this.projectRepo.delete(existedProject); // I added this because one of my project service test method failed.
        this.accountRepo.save(account);
    }

    @Override
    public ProjectSummaryDto findProjectById(UUID projectId, AccountDetails currentUser)
    {
//        TODO: Make sure that super user can view any project but user can view his own one.
        boolean isSuperUser = currentUser.getAuthorities().contains(new SimpleGrantedAuthority("SUPER_USER"));

        Project project = this.projectRepo.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project with id " + projectId + " not found."));

        if (isSuperUser)
            return this.modelMapper.map(project, ProjectSummaryDto.class);

        UUID projectOwnerId = project.getAccount().getId();
        if (!currentUser.getAccountId().equals(projectOwnerId))
            throw new AccessDeniedException("You can only view your project.");

        return this.modelMapper.map(project, ProjectSummaryDto.class);
    }

    @Override
    public PaginationDto filterProjects(ProjectSearchQueryParam param)
    {
        AccountDetails accountDetails = (AccountDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isSuperUser = accountDetails.getAuthorities().contains(new SimpleGrantedAuthority("SUPER_USER"));

        List<ProjectDto> projectDtoList = this.projectCriteriaRepo.search(accountDetails.getAccountId(), isSuperUser, param)
                .stream().map(project -> {
                    if (isSuperUser) return this.modelMapper.map(project, ProjectDetailsDto.class);
                    return this.modelMapper.map(project, ProjectSummaryDto.class);
                })
                .toList();

        long totalItems = 0;

        if (isSuperUser)
            totalItems =
                    Long.valueOf(this.projectCriteriaRepo.getCount(param, isSuperUser, accountDetails.getAccountId())).intValue();
        else
            totalItems = this.projectCriteriaRepo.getCount(param, isSuperUser, accountDetails.getAccountId());

        return PaginationHelper.buildPagination(param.getPage(), param.getSize(), totalItems, projectDtoList);
    }
}
