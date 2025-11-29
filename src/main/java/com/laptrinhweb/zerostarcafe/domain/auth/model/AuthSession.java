package com.laptrinhweb.zerostarcafe.domain.auth.model;

import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * <h2>Description:</h2>
 * <p>
 * Stores session state for an authenticated user.
 * It tracks the expiry time and the last rotation time.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 23/11/2025
 * @since 1.0.0
 */
public class AuthSession {

    private LocalDateTime expiredAt;
    private LocalDateTime lastRotatedAt;

    public AuthSession() {
    }

    /**
     * Checks if this session is expired.
     *
     * @return true if the session has passed its expiry time
     */
    public boolean isExpired() {
        return expiredAt != null
                && LocalDateTime.now().isAfter(expiredAt);
    }

    /**
     * Checks if the session should rotate its token.
     * Rotation happens only when the session is not expired
     * and enough time has passed since the last rotation.
     *
     * @return true if rotation is required
     */
    public boolean shouldRotated() {
        if (isExpired() || lastRotatedAt == null)
            return false;

        LocalDateTime now = LocalDateTime.now();
        long minutes = Duration.between(lastRotatedAt, now).toMinutes();
        return minutes >= SecurityKeys.REFRESH_MINUTES;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    public LocalDateTime getLastRotatedAt() {
        return lastRotatedAt;
    }

    public void setLastRotatedAt(LocalDateTime lastRotatedAt) {
        this.lastRotatedAt = lastRotatedAt;
    }
}
