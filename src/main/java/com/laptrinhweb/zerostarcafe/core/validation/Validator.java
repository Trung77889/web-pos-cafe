package com.laptrinhweb.zerostarcafe.core.validation;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * <h2>Description:</h2>
 * <p>
 * Provides common rules (email, phone, password, etc.) and input constraints.
 * Built on top of {@link RegexLibrary}.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * boolean isValidEmail = new AuthValidator(email)
 *                          .notEmpty()
 *                          .email()
 *                          .maxLength(100)
 *                          .isValid();
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 28/10/2025
 * @since 1.0.0
 */
public final class Validator {
    private final String field;
    private final String value;
    private boolean valid = true;
    private String errorKey;

    // Constructors
    public Validator(String field, String value) {
        this.field = field;
        this.value = value;
    }

    // Factory method for concise syntax
    public static Validator field(String field, String value) {
        return new Validator(field, value);
    }

    // ============================================================
    // 🧩 Private helper
    // ============================================================

    private String key(String detail) {
        return "form." + field + ".error." + detail;
    }

    private Validator match(Pattern pattern, String detail) {
        if (!valid) return this;
        if (value == null || !pattern.matcher(value).matches()) {
            valid = false;
            errorKey = key(detail);
        }
        return this;
    }

    // ============================================================
    // 🧠 Chainable instance methods (for form or entity validation)
    // ============================================================

    /**
     * Must not be null or empty
     */
    public Validator notEmpty() {
        if (!valid) return this;
        if (value == null || value.trim().isEmpty()) {
            valid = false;
            errorKey = key("notEmpty");
        }
        return this;
    }

    /**
     * Must be checked
     */
    public Validator checked() {
        if (!valid) return this;
        if (value == null || value == "off") {
            valid = false;
            errorKey = key("notChecked");
        }
        return this;
    }

    /**
     * Minimum length required
     */
    public Validator minLength(int length) {
        if (!valid) return this;
        if (value == null || value.length() < length) {
            valid = false;
            errorKey = key("minLength." + length);
        }
        return this;
    }

    /**
     * Maximum length allowed
     */
    public Validator maxLength(int length) {
        if (!valid) return this;
        if (value != null && value.length() > length) {
            valid = false;
            errorKey = key("maxLength." + length);
        }
        return this;
    }

    /**
     * Must equal another string (case-sensitive)
     */
    public Validator equalTo(String other) {
        if (!valid) return this;
        if (!(Objects.equals(value, other))) {
            valid = false;
            errorKey = key("notMatch");
        }
        return this;
    }

    /**
     * Must not equal another string
     */
    public Validator notEqualTo(String other) {
        if (!valid) return this;
        if (value != null && value.equals(other)) {
            valid = false;
            errorKey = key("shouldNotEqual");
        }
        return this;
    }

    // === Regex-based chain methods ===

    public Validator email() {
        return match(RegexLibrary.EMAIL, "invalidFormat");
    }

    public Validator username() {
        return match(RegexLibrary.USERNAME, "invalidFormat");
    }

    public Validator passwordBasic() {
        return match(RegexLibrary.PASSWORD_BASIC, "invalidFormat");
    }

    public Validator passwordStrong() {
        return match(RegexLibrary.PASSWORD_STRONG, "invalidFormat");
    }

    public Validator phone() {
        return match(RegexLibrary.VN_PHONE, "invalidFormat");
    }

    public Validator url() {
        return match(RegexLibrary.URL, "invalidFormat");
    }

    public Validator slug() {
        return match(RegexLibrary.SLUG, "invalidFormat");
    }

    public Validator isoDate() {
        return match(RegexLibrary.ISO_DATE, "invalidFormat");
    }

    public Validator uuid() {
        return match(RegexLibrary.UUID, "invalidFormat");
    }

    public Validator code() {
        return match(RegexLibrary.CODE, "invalidFormat");
    }

    // ============================================================
    // ✅ Final result
    // ============================================================

    /**
     * Convenience: true if valid
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Convenience: null if no error
     */
    public String getError() {
        return errorKey;
    }

    /**
     * Optional wrapper for functional usage
     */
    public Optional<String> error() {
        return Optional.ofNullable(errorKey);
    }
}
