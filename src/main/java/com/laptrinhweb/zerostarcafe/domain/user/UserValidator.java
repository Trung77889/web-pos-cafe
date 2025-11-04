package com.laptrinhweb.zerostarcafe.domain.user;

import com.laptrinhweb.zerostarcafe.core.validation.ValidationResult;
import com.laptrinhweb.zerostarcafe.core.validation.Validator;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <h2>Description:</h2>
 * <p>
 * Domain-level validator for user registration and login.
 * Uses {@link Validator} for reusable core validation rules and i18n ready keys.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * ValidationResult validation = UserValidator.validateRegistration(email, username, password);
 * if (!validation.valid()) { ... }
 * }
 * </pre>
 *
 * @author
 * @version 1.0.0
 * @lastModified 28/10/2025
 * @since 1.0.0
 */
public final class UserValidator {

    // prevent instantiation
    private UserValidator() {
    }

    /**
     * Validate new user registration fields.
     */
    public static ValidationResult validateRegistration(String email, String username, String password, String agreeTerms) {
        Map<String, String> errors = new LinkedHashMap<>();

        String emailErr = Validator.field("email", email)
                .notEmpty()
                .email()
                .maxLength(100)
                .getError();
        if (emailErr != null) errors.put("email", emailErr);

        String userErr = Validator.field("username", username)
                .notEmpty()
                .username()
                .minLength(3)
                .maxLength(32)
                .getError();
        if (userErr != null) errors.put("username", userErr);

        String passErr = Validator.field("password", password)
                .notEmpty()
                .passwordBasic()
                .minLength(6)
                .maxLength(32)
                .getError();
        if (passErr != null) errors.put("password", passErr);

        String agreeTermsErr = Validator.field("agreeTerms", agreeTerms)
                .checked()
                .getError();
        if (agreeTermsErr != null) errors.put("agreeTerms", agreeTermsErr);

        return ValidationResult.merge(errors);
    }

    /**
     * Validate login fields (usually only username & password)
     */
    public static ValidationResult validateLogin(String username, String password) {
        Map<String, String> errors = new LinkedHashMap<>();

        String userErr = Validator.field("username", username)
                .notEmpty()
                .username()
                .getError();
        if (userErr != null) errors.put("username", userErr);

        String passErr = Validator.field("password", password)
                .notEmpty()
                .minLength(6)
                .getError();
        if (passErr != null) errors.put("password", passErr);

        return ValidationResult.merge(errors);
    }
}
