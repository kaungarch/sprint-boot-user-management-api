package com.art.usermanagement.ValidationTest.project;

import com.art.usermanagement.dto.request.ProjectCreationRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProjectDtoValidationTest {

    private static Validator validator;

    @BeforeAll
    public static void setupValidatorInstance()
    {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void whenNullDescription_thenOneConstraintViolations()
    {
        ProjectCreationRequest request = new ProjectCreationRequest("project_one", null);
        Set<ConstraintViolation<ProjectCreationRequest>> violations = validator.validate(request);

        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void whenBothFieldsNull_thenTwoConstraintViolations()
    {
        ProjectCreationRequest request = new ProjectCreationRequest(null, null);
        Set<ConstraintViolation<ProjectCreationRequest>> violations = validator.validate(request);

        assertThat(violations.size()).isEqualTo(2);
    }

    @Test
    public void whenTitleLessThan3Chars_thenOneConstraintViolations()
    {
        ProjectCreationRequest request = new ProjectCreationRequest("pr", "This is my project.");
        Set<ConstraintViolation<ProjectCreationRequest>> violations = validator.validate(request);

        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void whenDescriptionIsLessThan5Chars_thenOneConstraintViolations()
    {
        ProjectCreationRequest request = new ProjectCreationRequest("project_one", "prj1");
        Set<ConstraintViolation<ProjectCreationRequest>> violations = validator.validate(request);
        assertThat(violations.size()).isEqualTo(1);
        Iterator<ConstraintViolation<ProjectCreationRequest>> iterator = violations.iterator();
        while (iterator.hasNext()) {
            ConstraintViolation<ProjectCreationRequest> next = iterator.next();
            System.out.println(next.getMessage());
        }
    }

    @Test
    public void whenTitleIsBlank_thenTwoConstraintViolations()
    {
        ProjectCreationRequest request = new ProjectCreationRequest("", "This is my project.");
        Set<ConstraintViolation<ProjectCreationRequest>> violations = validator.validate(request);

        assertThat(violations.size()).isEqualTo(2);
        Iterator<ConstraintViolation<ProjectCreationRequest>> iterator = violations.iterator();
        while (iterator.hasNext()) {
            ConstraintViolation<ProjectCreationRequest> next = iterator.next();
            System.out.println(next.getMessage());
        }
    }

    @Test
    void whenDescriptionIsBlank_thenTwoConstraintViolations()
    {
        ProjectCreationRequest request = new ProjectCreationRequest("project_one", "");
        Set<ConstraintViolation<ProjectCreationRequest>> violations = validator.validate(request);
        assertThat(violations.size()).isEqualTo(2);

        Iterator<ConstraintViolation<ProjectCreationRequest>> iterator = violations.iterator();
        while (iterator.hasNext()) {
            ConstraintViolation<ProjectCreationRequest> next = iterator.next();
            System.out.println(next.getMessage());
        }
    }
}
