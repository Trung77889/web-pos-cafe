package com.laptrinhweb.zerostarcafe.domain.user_role;

import java.time.LocalDateTime;

/**
 * <h2>Description:</h2>
 * <p>
 * Represents a user's role assignment in a specific store.
 * Each record links a user to a store with a given {@code role_code}.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 11/11/2025
 * @since 1.0.0
 */
public final class UserStoreRole {

    private Long id;
    private Long userId;
    private Long storeId;
    private String roleCode;
    private LocalDateTime createdAt;

    // ===== Constructors ====

    public UserStoreRole() {
    }

    public UserStoreRole(Long id, Long userId, Long storeId, String roleCode, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.storeId = storeId;
        this.roleCode = roleCode;
        this.createdAt = createdAt;
    }

    // ===== Getters and setters ====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
