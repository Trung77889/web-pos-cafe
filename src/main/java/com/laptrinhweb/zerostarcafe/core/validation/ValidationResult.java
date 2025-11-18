package com.laptrinhweb.zerostarcafe.core.validation;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <h2>Description:</h2>
 * <p>
 * Represents the result of a validation process.
 * Stores whether the data is valid and a list of field-specific errors.
 * Each error message is stored as an i18n message key.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * ValidationResult result = AuthValidator.registerCheck(email, username, password);
 * if (!result.valid()) {
 *     // Error handling...
 * }
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 29/10/2025
 * @since 1.0.0
 */
public record ValidationResult(
        boolean valid,
        Map<String, String> fieldErrors,
        LocalDateTime timestamp
) {

    // ==== Constructors ====
    public ValidationResult(boolean valid, Map<String, String> fieldErrors) {
        this(valid, fieldErrors, LocalDateTime.now());
    }

    // ==== Factory Methods ====

    /**
     * All fields are valid
     */
    public static ValidationResult ok() {
        return new ValidationResult(true, Map.of());
    }

    /**
     * Quick fail: one field, one message key
     */
    public static ValidationResult fail(String field, String messageKey) {
        return new ValidationResult(false, Map.of(field, messageKey));
    }

    /**
     * Merge multiple field errors
     */
    public static ValidationResult merge(Map<String, String> errors) {
        return new ValidationResult(errors.isEmpty(), errors);
    }

    // ==== Helpers ====

    /**
     * Whether there are any errors
     */
    public boolean hasErrors() {
        return !valid && !fieldErrors.isEmpty();
    }
}
