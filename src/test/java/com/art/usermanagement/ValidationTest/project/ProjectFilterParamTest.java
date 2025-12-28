package com.art.usermanagement.ValidationTest.project;

import com.art.usermanagement.dto.request.ProjectSearchQueryParam;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ProjectFilterParamTest {
    private static Validator validator;

    @BeforeAll
    public static void setUp()
    {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    private void printErrorMessages(Set<ConstraintViolation<ProjectSearchQueryParam>> violations)
    {
        Iterator<ConstraintViolation<ProjectSearchQueryParam>> iterator = violations.iterator();
        while (iterator.hasNext()) {
            ConstraintViolation<ProjectSearchQueryParam> next = iterator.next();
            System.out.println(next.getMessage());
        }
    }

    @Test
    public void whenPageIsLessThanOne_thenOneConstraintViolation()
    {
        ProjectSearchQueryParam param = new ProjectSearchQueryParam();
        param.setPage(0);
        Set<ConstraintViolation<ProjectSearchQueryParam>> violations = validator.validate(param);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void whenSizeIsLessThanOne_thenOneConstraintViolation()
    {
        ProjectSearchQueryParam param = new ProjectSearchQueryParam();
        param.setSize(0);
        Set<ConstraintViolation<ProjectSearchQueryParam>> violations = validator.validate(param);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void whenSortOrderIsInvalid_thenOneConstraintViolation()
    {
        ProjectSearchQueryParam param = new ProjectSearchQueryParam();
        param.setOrder("sort");
        Set<ConstraintViolation<ProjectSearchQueryParam>> violations = validator.validate(param);
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    public void whenSortByIsInvalid_thenOneConstraintViolation()
    {
        ProjectSearchQueryParam param = new ProjectSearchQueryParam();
        param.setSortBy("invalid input");
        Set<ConstraintViolation<ProjectSearchQueryParam>> violations = validator.validate(param);
        assertThat(violations.size()).isEqualTo(1);
    }
}
