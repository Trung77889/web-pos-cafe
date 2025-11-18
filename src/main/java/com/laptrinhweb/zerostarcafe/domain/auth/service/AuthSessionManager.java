package com.laptrinhweb.zerostarcafe.domain.auth.service;

import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;
import com.laptrinhweb.zerostarcafe.core.utils.TimeUtil;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthUser;
import com.laptrinhweb.zerostarcafe.domain.user.model.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <h2>Description:</h2>
 * <p>
 * Manages the in-memory {@link HttpSession} lifecycle.
 * Ensures only one active session per user and resolves session expiration
 * times based on user roles.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * LocalDateTime expiredAt = sessionManager.resolveExpiredAt(authUser);
 * HttpSession session = sessionManager.createSession(req, authUser, expiredAt, deviceId, rawToken);
 * }</pre>
 *
 * @author Dang Van Trung
 * @version 1.1.0
 * @lastModified 18/11/2025
 * @since 1.0.0
 */
public final class AuthSessionManager {

    // Map of: userId -> active HttpSession.
    private final ConcurrentHashMap<Long, HttpSession> userSessions;

    // ===== Constructor ====
    public AuthSessionManager(ConcurrentHashMap<Long, HttpSession> userSessions) {
        this.userSessions = userSessions;
    }

    // ==== Getter =====
    public ConcurrentHashMap<Long, HttpSession> getUserSessions() {
        return userSessions;
    }

    // ===== Public Methods ====

    /**
     * Invalidates the user's current in-memory session, if it exists.
     *
     * @param userId the user's ID; does nothing if {@code null}
     */
    public void revokeSession(Long userId) {
        if (userId == null)
            return;

        HttpSession currentSession = userSessions.remove(userId);
        if (currentSession != null) {
            try {
                currentSession.invalidate();
            } catch (IllegalStateException ignore) {
                // Already invalidated
            }
        }
    }

    /**
     * Creates a new HttpSession for the user, saves it to the in-memory map,
     * and attaches all required metadata (auth user, expiredAt, deviceId, authToken).
     *
     * @param request   the current request
     * @param user      the authenticated user
     * @param expiredAt the session's expiration time
     * @param deviceId  the device/browser identifier
     * @param authToken the raw auth token (for the cookie)
     * @return the newly created {@link HttpSession}
     */
    public HttpSession createSession(HttpServletRequest request,
                                     AuthUser user,
                                     LocalDateTime expiredAt,
                                     String deviceId,
                                     String authToken
    ) {

        // Enforce single active session per user
        revokeSession(user.userId());

        HttpSession session = request.getSession();
        request.changeSessionId();

        userSessions.put(user.userId(), session);

        LocalDateTime now = LocalDateTime.now();
        int ttlSeconds = TimeUtil.ttlFromNow(expiredAt);

        session.setAttribute(SecurityKeys.SESSION_AUTH_USER, user);
        session.setAttribute(SecurityKeys.SESSION_EXPIRED_AT, expiredAt);
        session.setAttribute(SecurityKeys.SESSION_LAST_ROTATED, now);
        session.setAttribute(SecurityKeys.SESSION_DEVICE_ID, deviceId);
        session.setAttribute(SecurityKeys.SESSION_AUTH_TOKEN, authToken);
        session.setMaxInactiveInterval(ttlSeconds);

        return session;
    }

    /**
     * Calculates the session expiration time based on user roles.
     *
     * @param user the authenticated user; can be {@code null}
     * @return a {@link LocalDateTime} representing the expiration time
     */
    public LocalDateTime resolveExpiredAt(AuthUser user) {
        LocalDateTime now = LocalDateTime.now();

        if (user != null && user.hasRole(UserRole.SUPER_ADMIN)) {
            return now.plusMinutes(30);
        }
        if (user != null && user.hasRole(UserRole.STORE_MANAGER)) {
            return now.plusHours(4);
        }
        if (user != null && user.hasRole(UserRole.STAFF)) {
            return LocalDate.now().atTime(23, 59);
        }

        return now.plusDays(7);
    }
}
