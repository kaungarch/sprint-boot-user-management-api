package com.art.usermanagement.IntegrationTest.project;

import com.art.usermanagement.dto.request.ProjectCreationRequest;
import com.art.usermanagement.dto.request.ProjectUpdateRequest;
import com.art.usermanagement.dto.response.ProjectSummaryDto;
import com.art.usermanagement.exception.AccessDeniedException;
import com.art.usermanagement.exception.ProjectNotFoundException;
import com.art.usermanagement.exception.UserAccountNotFoundException;
import com.art.usermanagement.model.Account;
import com.art.usermanagement.model.Project;
import com.art.usermanagement.repository.account.AccountRepo;
import com.art.usermanagement.repository.project.ProjectCriteriaRepoImpl;
import com.art.usermanagement.repository.project.ProjectRepo;
import com.art.usermanagement.security.AccountDetails;
import com.art.usermanagement.service.project.ProjectService;
import com.art.usermanagement.service.project.ProjectServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({ModelMapper.class, AccountDetails.class, ProjectServiceImpl.class, Account.class, ProjectCriteriaRepoImpl.class})
@Transactional
@ActiveProfiles("test")
public class ProjectServiceTest {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectRepo projectRepo;
    @Autowired
    private AccountRepo accountRepo;

    public static UUID FAKE_UUID = UUID.fromString("3f9b6c54-1e2a-4d8f-9b27-4e7b8cdfe123");
    private static UUID EXISTED_ACCOUNT_ID = UUID.fromString("e0f1a2b3-aaaa-4f00-9abc-123456789010");

    private ProjectCreationRequest getProjectCreationRequest()
    {
        return new ProjectCreationRequest("project_one", "User Management System");
    }

    private ProjectUpdateRequest getProjectUpdateRequest()
    {
        return new ProjectUpdateRequest("updated project title", null);
    }

    private AccountDetails getAccountDetails(UUID accountId)
    {
        Account account = this.accountRepo.findById(accountId).orElseThrow(() -> new UserAccountNotFoundException("Account " +
                "with id " + accountId + " not found."));

        return AccountDetails.of(account);
    }

    @Test
    public void testFailedProjectCreationDueToAccountNotFound()
    {
//        call method using non-existed account id
        assertThrows(UserAccountNotFoundException.class,
                () -> this.projectService.createProject(this.getProjectCreationRequest(), FAKE_UUID));
    }

    @Test
    public void testSuccessfulProjectCreation()
    {
//        call method using valid account id
        ProjectSummaryDto project = this.projectService.createProject(this.getProjectCreationRequest(), EXISTED_ACCOUNT_ID);
        assertNotNull(project);
    }

    @Test
    public void testFailedProjectUpdatingDueToAccessDenial()
    {
//        create a project with existing account
        ProjectSummaryDto createdProject = this.projectService.createProject(this.getProjectCreationRequest(), EXISTED_ACCOUNT_ID);
//        try updating with other account
        Executable executable = () -> this.projectService.updateProject(this.getProjectUpdateRequest(),
                createdProject.getId(),
                FAKE_UUID);

//        assert that AccessDeniedException is thrown
        assertThrows(AccessDeniedException.class, executable);
    }

    @Test
    public void testFailedProjectUpdatingDueToProjectNotFound()
    {
//        assert
        assertThrows(ProjectNotFoundException.class,
                () -> this.projectService.updateProject(this.getProjectUpdateRequest(), FAKE_UUID, EXISTED_ACCOUNT_ID));
    }

    @Test
    public void testSuccessfulProjectUpdating()
    {
//        create a new project with existing account
        ProjectSummaryDto project = this.projectService.createProject(this.getProjectCreationRequest(), EXISTED_ACCOUNT_ID);
//        assert that project is saved in db
        assertNotNull(project);
        assertEquals("User Management System", project.getDescription());

//        try updating project with non-existed account
        ProjectSummaryDto updatedProject = this.projectService.updateProject(this.getProjectUpdateRequest(), project.getId(), EXISTED_ACCOUNT_ID);
//        assert that project is successfully updated
        assertNotNull(updatedProject);
        assertEquals("updated project title", updatedProject.getTitle());
        assertEquals("User Management System", updatedProject.getDescription());
    }

    @Test
    public void testFailedProjectEraseDueToProjectNotFound()
    {
        assertThrows(ProjectNotFoundException.class, () -> this.projectService.eraseProject(FAKE_UUID, EXISTED_ACCOUNT_ID));
    }

    @Test
    public void testFailedProjectEraseDueToAccessDenial()
    {
//        create a project
        ProjectSummaryDto createdProject = this.projectService.createProject(this.getProjectCreationRequest(), EXISTED_ACCOUNT_ID);
//        try erasing with other account
        Executable executable = () -> this.projectService.eraseProject(createdProject.getId(), FAKE_UUID);
//        assert
        assertThrows(AccessDeniedException.class, executable);
    }

    @Test
    public void testSuccessfulProjectErase()
    {
//        create a project
        ProjectSummaryDto createdProject = this.projectService.createProject(this.getProjectCreationRequest(), EXISTED_ACCOUNT_ID);
//        erase it
        this.projectService.eraseProject(createdProject.getId(), EXISTED_ACCOUNT_ID);
//        find project
        Optional<Project> optionalProject = this.projectRepo.findById(createdProject.getId());
//        assert
        assertEquals(optionalProject.get().isDeleted(), true);
    }

    @Test
    public void testFailedProjectDeletionDueToProjectNotFound()
    {
        assertThrows(ProjectNotFoundException.class, () -> this.projectService.deleteProject(FAKE_UUID, EXISTED_ACCOUNT_ID));
    }

    @Test
    public void testFailedProjectDeletionDueToAccessDenial()
    {
//        create a project
        ProjectSummaryDto createdProject = this.projectService.createProject(this.getProjectCreationRequest(), EXISTED_ACCOUNT_ID);
//        try deleting with other account
        Executable executable = () -> this.projectService.deleteProject(createdProject.getId(), FAKE_UUID);
//        assert
        assertThrows(AccessDeniedException.class, executable);
    }

    @Test
    public void testSuccessfulProjectDeletion()
    {
//        create a project
        ProjectSummaryDto createdProject = this.projectService.createProject(this.getProjectCreationRequest(),
                EXISTED_ACCOUNT_ID);
//        delete it
        this.projectService.deleteProject(createdProject.getId(), EXISTED_ACCOUNT_ID);

//        fetch deleted project
        Optional<Project> optionalProject = this.projectRepo.findById(createdProject.getId());

//        assert
        assertThat(optionalProject.isEmpty()).isTrue();
    }

    @Test
    public void testFailedProjectSearchDueToNotFound()
    {
        assertThrows(ProjectNotFoundException.class, () -> this.projectService.findProjectById(FAKE_UUID,
                this.getAccountDetails(EXISTED_ACCOUNT_ID)));
    }

    @Test
    public void testSuccessfulProjectSearch()
    {
//        create a project
        ProjectSummaryDto createdProject = this.projectService.createProject(this.getProjectCreationRequest(), EXISTED_ACCOUNT_ID);
//        find project
        ProjectSummaryDto foundProject = this.projectService.findProjectById(createdProject.getId(),
                this.getAccountDetails(EXISTED_ACCOUNT_ID));
//        assert
        assertNotNull(foundProject);
        assertEquals(createdProject.getId(), foundProject.getId());
        assertEquals(createdProject.getTitle(), foundProject.getTitle());
        assertEquals(createdProject.getDescription(), foundProject.getDescription());
    }
}