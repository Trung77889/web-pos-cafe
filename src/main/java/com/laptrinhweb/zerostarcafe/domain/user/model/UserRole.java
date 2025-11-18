package com.laptrinhweb.zerostarcafe.domain.user.model;

/**
 * <h2>Description:</h2>
 * <p>
 * Defines the application-wide roles (e.g., ADMIN, MANAGER, STAFF).
 * This enum is the single source of truth for all system roles.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 16/11/2025
 * @since 1.0.0
 */
public enum UserRole {
    SUPER_ADMIN("SUPER_ADMIN", "System Administrator"),
    STORE_MANAGER("STORE_MANAGER", "Store Manager"),
    STAFF("STAFF", "Store staff");

    private final String code;
    private final String description;

    // ==== Constructor ====
    UserRole(String code, String description) {
        this.code = code;
        this.description = description;
    }

    // ==== Getters and Setters ====
    public String getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }
}
