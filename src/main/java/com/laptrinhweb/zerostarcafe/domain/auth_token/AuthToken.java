package com.laptrinhweb.zerostarcafe.domain.auth_token;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

/**
 * <h2>Description:</h2>
 * <p>
 * Represents a long-lived authentication token
 * that can be used to restore a login session.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 16/11/2025
 * @since 1.0.0
 */
public final class AuthToken {

    private Long id;
    private Long userId;
    private String authHash;
    private String deviceId;
    private TokenStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
    private LocalDateTime lastRotatedAt;

    private String ipLast;
    private String userAgent;

    private LocalDateTime revokedAt;
    private String revokedReason;

    // ==== Constructors ====
    public AuthToken() {
    }

    public AuthToken(HttpServletRequest request,
                     Long userId,
                     String authHash,
                     String deviceId,
                     TokenStatus status,
                     LocalDateTime expiredAt) {
        this.userId = userId;
        this.authHash = authHash;
        this.deviceId = deviceId;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.expiredAt = expiredAt;
        this.lastRotatedAt = LocalDateTime.now();
        this.ipLast = request.getRemoteAddr();
        this.userAgent = request.getHeader("User-Agent");
    }

    // ==== Getters and Setters ====
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

    public String getAuthHash() {
        return authHash;
    }

    public void setAuthHash(String authHash) {
        this.authHash = authHash;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public TokenStatus getStatus() {
        return status;
    }

    public void setStatus(TokenStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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

    public String getIpLast() {
        return ipLast;
    }

    public void setIpLast(String ipLast) {
        this.ipLast = ipLast;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public LocalDateTime getRevokedAt() {
        return revokedAt;
    }

    public void setRevokedAt(LocalDateTime revokedAt) {
        this.revokedAt = revokedAt;
    }

    public String getRevokedReason() {
        return revokedReason;
    }

    public void setRevokedReason(String revokedReason) {
        this.revokedReason = revokedReason;
    }
}
