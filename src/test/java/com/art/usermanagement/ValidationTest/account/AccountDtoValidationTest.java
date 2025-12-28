package com.art.usermanagement.ValidationTest.account;

import com.art.usermanagement.dto.request.RegistrationRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class AccountDtoValidationTest {
    private static Validator validator;

    @BeforeAll
    public static void setUpValidatorInstance()
    {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void whenValidRequest_thenNoViolations()
    {
        RegistrationRequest request = new RegistrationRequest("Alice", "aLice@2025", "959123456789", "5/KALAWA(C)" +
                "123456");

        Set<ConstraintViolation<RegistrationRequest>> validate = validator.validate(request);
        Iterator<ConstraintViolation<RegistrationRequest>> iterator = validate.iterator();
        printViolations(iterator);
        assertThat(validate.size()).isEqualTo(0);
    }

    @Test
    public void whenNameIsBlank_thenTwoViolations()
    {
        RegistrationRequest request = new RegistrationRequest("", "aLice@2025", "959123456789", "5/KALAWA(C)" +
                "123456");
        Set<ConstraintViolation<RegistrationRequest>> validate = validator.validate(request);
        Iterator<ConstraintViolation<RegistrationRequest>> iterator = validate.iterator();
        printViolations(iterator);
        assertThat(validate.size()).isEqualTo(2);
    }

    @Test
    public void whenPasswordIsInvalid_thenOneViolations_1()
    {
        RegistrationRequest request = new RegistrationRequest("Alice", "alice@2025", "959123456789", "5/KALAWA(C)" +
                "123456");
        Set<ConstraintViolation<RegistrationRequest>> validate = validator.validate(request);
        Iterator<ConstraintViolation<RegistrationRequest>> iterator = validate.iterator();
        printViolations(iterator);
        assertThat(validate.size()).isEqualTo(1);
    }

    @Test
    public void whenPasswordIsInvalid_thenOneViolations_2()
    {
        RegistrationRequest request = new RegistrationRequest("Alice", "ALICE@2025", "959123456789", "5/KALAWA(C)" +
                "123456");
        Set<ConstraintViolation<RegistrationRequest>> validate = validator.validate(request);
        Iterator<ConstraintViolation<RegistrationRequest>> iterator = validate.iterator();
        printViolations(iterator);
        assertThat(validate.size()).isEqualTo(1);
    }

    @Test
    public void whenPasswordIsInvalid_thenOneViolations_3()
    {
        RegistrationRequest request = new RegistrationRequest("Alice", "aLice2025", "959123456789", "5/KALAWA(C)" +
                "123456");
        Set<ConstraintViolation<RegistrationRequest>> validate = validator.validate(request);
        Iterator<ConstraintViolation<RegistrationRequest>> iterator = validate.iterator();
        printViolations(iterator);
        assertThat(validate.size()).isEqualTo(1);
    }

    @Test
    public void whenPasswordIsInvalid_thenOneViolations_4()
    {
        RegistrationRequest request = getRegistrationRequest();
        Set<ConstraintViolation<RegistrationRequest>> validate = validator.validate(request);
        Iterator<ConstraintViolation<RegistrationRequest>> iterator = validate.iterator();
        printViolations(iterator);
        assertThat(validate.size()).isEqualTo(1);
    }

    private static void printViolations(Iterator<ConstraintViolation<RegistrationRequest>> iterator)
    {
        while (iterator.hasNext()) {
            ConstraintViolation<RegistrationRequest> next = iterator.next();
            System.out.println(next.getMessage());
        }
    }

    private static @NonNull RegistrationRequest getRegistrationRequest()
    {
        return new RegistrationRequest("Alice", "aLice@12345", "959123456789", "5/KALAWA" +
                "(C)" +
                "123456");
    }

    @Test
    public void whenPhoneNumberIsNotInFormat_thenOneViolations()
    {
        RegistrationRequest registrationRequest = new RegistrationRequest("Alice", "aLice@12345", "959", "5/KALAWA" +
                "(C)123456");

        Set<ConstraintViolation<RegistrationRequest>> validated = validator.validate(registrationRequest);
        assertThat(validated.size()).isEqualTo(1);
        printViolations(validated.iterator());
    }

    @Test
    public void whenPhoneNumberIsEmpty_thenTwoViolations()
    {
        RegistrationRequest registrationRequest = new RegistrationRequest("Alice", "aLice@12345", "", "5/KALAWA" +
                "(C)123456");

        Set<ConstraintViolation<RegistrationRequest>> validated = validator.validate(registrationRequest);
        assertThat(validated.size()).isEqualTo(2);
        printViolations(validated.iterator());
    }

    @Test
    public void whenNrcNumberIsEmpty_thenTwoViolations()
    {
        RegistrationRequest registrationRequest = new RegistrationRequest("Alice", "aLice@12345", "959999999999", "");

        Set<ConstraintViolation<RegistrationRequest>> validated = validator.validate(registrationRequest);
        assertThat(validated.size()).isEqualTo(2);
        printViolations(validated.iterator());
    }

    @Test
    public void whenNrcNumberIsNotInFormat_thenOneViolations()
    {
        RegistrationRequest registrationRequest = new RegistrationRequest("Alice", "aLice@12345", "959888888888", "5" +
                "/KALAWA" +
                "(p)123456");

        Set<ConstraintViolation<RegistrationRequest>> validated = validator.validate(registrationRequest);
        assertThat(validated.size()).isEqualTo(1);
        printViolations(validated.iterator());
    }

    @Test
    public void whenNrcNumberIsNotInFormat_thenOneViolations2()
    {
        RegistrationRequest registrationRequest = new RegistrationRequest("Alice", "aLice@12345", "959888888888", "23" +
                "/KALAWA" +
                "(C)123456");

        Set<ConstraintViolation<RegistrationRequest>> validated = validator.validate(registrationRequest);
        assertThat(validated.size()).isEqualTo(1);
        printViolations(validated.iterator());
    }

    @Test
    public void whenNrcNumberIsNotInFormat_thenOneViolations3()
    {
        RegistrationRequest registrationRequest = new RegistrationRequest("Alice", "aLice@12345", "959888888888", "5" +
                "/KALAWA" +
                "(C)123");

        Set<ConstraintViolation<RegistrationRequest>> validated = validator.validate(registrationRequest);
        assertThat(validated.size()).isEqualTo(1);
        printViolations(validated.iterator());
    }

    @Test
    public void testMultipleFieldsNotInFormat()
    {
        RegistrationRequest registrationRequest = new RegistrationRequest("Alice", "aLice@12345", "959", "");

        Set<ConstraintViolation<RegistrationRequest>> validated = validator.validate(registrationRequest);
        assertThat(validated.size()).isEqualTo(2);
        printViolations(validated.iterator());
    }
}
