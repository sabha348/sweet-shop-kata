package com.incubyte.sweetshop;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;
import jakarta.validation.ConstraintViolation;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void should_fail_validation_if_email_is_invalid() {
        // Attempt to create a user with a bad email
        User user = new User("bad-email", "password123", "CUSTOMER");
        
        // Validate it
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        
        // Expect a violation
        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).contains("must be a well-formed email address");
    }

    @Test
    public void should_pass_validation_with_valid_details() {
        User user = new User("test@incubyte.co", "SecurePass123!","CUSTOMER");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations).isEmpty();
    }
}