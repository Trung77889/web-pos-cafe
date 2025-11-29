package com.laptrinhweb.zerostarcafe.domain.auth.model;

import java.time.LocalDateTime;

/**
 * <h2>Description:</h2>
 * <p>
 * Represents a single authentication token.
 * Each token has a name, value, and expiry time.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 23/11/2025
 * @since 1.0.0
 */
public final class AuthToken {
    private String name;
    private String value;
    private LocalDateTime expiredAt;

    public AuthToken() {
    }

    public AuthToken(String name, String value, LocalDateTime expiredAt) {
        this.name = name;
        this.value = value;
        this.expiredAt = expiredAt;
    }

    /**
     * Checks if this token is expired.
     *
     * @return true if the expiry time is in the past
     */
    public boolean isExpired() {
        return expiredAt != null && LocalDateTime.now().isAfter(expiredAt);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }
}
