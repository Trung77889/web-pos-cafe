package com.laptrinhweb.zerostarcafe.domain.auth.service;

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
 * ValidationResult validation = AuthValidator.registerCheck(email, username, password);
 * if (!validation.valid()) { ... }
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 28/10/2025
 * @since 1.0.0
 */
public final class AuthValidator {

    // prevent instantiation
    private AuthValidator() {
    }

    /**
     * Validate new user registration fields.
     */
    public static ValidationResult registerCheck(String email, String username, String password) {
        Map<String, String> errors = new LinkedHashMap<>();

        String emailErr = Validator.field("email", email)
                .notEmpty()
                .email()
                .maxLength(100)
                .getError();
        if (emailErr != null) errors.put("regEmail", emailErr);

        String userErr = Validator.field("username", username)
                .notEmpty()
                .username()
                .minLength(3)
                .maxLength(32)
                .getError();
        if (userErr != null) errors.put("regUsername", userErr);

        String passErr = Validator.field("password", password)
                .notEmpty()
                .passwordBasic()
                .minLength(6)
                .maxLength(32)
                .getError();
        if (passErr != null) errors.put("regPassword", passErr);

        return ValidationResult.merge(errors);
    }

    /**
     * Validate login fields (usually only username & password)
     */
    public static ValidationResult loginCheck(String username, String password) {
        Map<String, String> errors = new LinkedHashMap<>();

        String userErr = Validator.field("username", username)
                .notEmpty()
                .username()
                .getError();
        if (userErr != null) errors.put("loginUsername", userErr);

        String passErr = Validator.field("password", password)
                .notEmpty()
                .minLength(6)
                .getError();
        if (passErr != null) errors.put("loginPassword", passErr);

        return ValidationResult.merge(errors);
    }
}
