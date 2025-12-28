package com.art.usermanagement.IntegrationTest.project;

import com.art.usermanagement.dto.request.ProjectSearchQueryParam;
import com.art.usermanagement.model.Project;
import com.art.usermanagement.repository.project.ProjectCriteriaRepo;
import com.art.usermanagement.repository.project.ProjectCriteriaRepoImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import(ProjectCriteriaRepoImpl.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class ProjectCriteriaRepoTest {
    @Autowired
    private ProjectCriteriaRepo projectCriteriaRepo;

    private static UUID ACCOUNT_ID = UUID.fromString("e0f1a2b3-aaaa-4f00-9abc-123456789010");

    @Test
    public void testWithAdminSearchUsingDefaultParams()
    {
        ProjectSearchQueryParam param = new ProjectSearchQueryParam();
        List<Project> projectList = projectCriteriaRepo.search(ACCOUNT_ID, true, param);
        assertThat(projectList.size()).isEqualTo(10);
    }

    @Test
    public void testFilterBasedOnCreationDateAndSortOrder()
    {
        ProjectSearchQueryParam param = new ProjectSearchQueryParam();
        param.setSortBy("createdAt");
        param.setOrder("desc");
        List<Project> projectList = this.projectCriteriaRepo.search(ACCOUNT_ID, false, param);
        assertThat(projectList.size()).isEqualTo(8);
        for (Project project: projectList)
            System.out.println(project.getTitle() + " - "+ project.getCreatedAt());
    }
}
