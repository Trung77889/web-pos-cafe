package com.laptrinhweb.zerostarcafe.domain.auth.service;

import com.laptrinhweb.zerostarcafe.core.database.DBConnection;
import com.laptrinhweb.zerostarcafe.core.security.CookieUtil;
import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;
import com.laptrinhweb.zerostarcafe.core.security.TokenUtil;
import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthUser;
import com.laptrinhweb.zerostarcafe.domain.auth_token.AuthToken;
import com.laptrinhweb.zerostarcafe.domain.auth_token.AuthTokenService;
import com.laptrinhweb.zerostarcafe.domain.user.UserMapper;
import com.laptrinhweb.zerostarcafe.domain.user.dao.UserDAO;
import com.laptrinhweb.zerostarcafe.domain.user.dao.UserDAOImpl;
import com.laptrinhweb.zerostarcafe.domain.user.model.User;
import com.laptrinhweb.zerostarcafe.domain.user.model.UserStatus;
import com.laptrinhweb.zerostarcafe.domain.user_role.UserStoreRole;
import com.laptrinhweb.zerostarcafe.domain.user_role.UserStoreRoleDAO;
import com.laptrinhweb.zerostarcafe.domain.user_role.UserStoreRoleDAOImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * Simple service that coordinates login sessions and long-lived auth tokens.
 * <ul>
 *     <li>{@link AuthSessionManager} to manage in-memory {@link HttpSession} objects.</li>
 *     <li>{@link AuthTokenService} to store and validate auth tokens in the database.</li>
 * </ul>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 16/11/2025
 * @since 1.0.0
 */
public class AuthSessionService {

    private final AuthSessionManager sessionManager;
    private final AuthTokenService tokenService;

    /**
     * Creates a new AuthSessionService.
     *
     * @param sessionManager manager for HttpSession in memory
     * @param tokenService   manager for auth tokens in the database
     */
    public AuthSessionService(AuthSessionManager sessionManager, AuthTokenService tokenService) {
        this.sessionManager = sessionManager;
        this.tokenService = tokenService;
    }

    /**
     * Starts a fresh login session for a signed-in user.
     *
     * @param req  current HTTP request
     * @param user authenticated user information
     * @return new HttpSession, or {@code null} if token persistence failed
     */
    public HttpSession newLoginSession(HttpServletRequest req, AuthUser user) {
        Long userId = user.userId();

        // 1. Revoke old session & tokens
        sessionManager.revokeSession(userId);
        tokenService.revokeAllByUserId(userId);

        // 2. Resolve expiration
        LocalDateTime expiredAt = sessionManager.resolveExpiredAt(user);

        // 3. Resolve / generate deviceId
        String deviceId = CookieUtil.get(req, SecurityKeys.COOKIE_DEVICE_ID);
        if (deviceId == null || deviceId.isBlank()) {
            deviceId = TokenUtil.generateToken();
        }

        // 4. Issue new token (persist to DB)
        String rawAuthToken = tokenService.issueToken(req, userId, deviceId, expiredAt);
        if (rawAuthToken == null) {
            LoggerUtil.warn(AuthSessionService.class,
                    "Failed to create session: could not persist auth token for userId=" + userId);
            return null;
        }

        // 5. Create HttpSession and attach everything
        return sessionManager.createSession(req, user, expiredAt, deviceId, rawAuthToken);
    }

    /**
     * Restores a login session from a long-lived auth token
     *
     * @param req      current HTTP request
     * @param rawToken raw token string from cookies
     * @return new HttpSession if restored successfully, otherwise {@code null}
     */
    public HttpSession restoreByToken(HttpServletRequest req, String rawToken) {
        Optional<AuthToken> tokenOpt = tokenService.findValidByRawToken(rawToken);
        if (tokenOpt.isEmpty()) {
            return null;
        }

        AuthToken token = tokenOpt.get();
        Long userId = token.getUserId();

        try (Connection conn = DBConnection.getConnection()) {
            // Load user
            UserDAO userDAO = new UserDAOImpl(conn);
            Optional<User> userOpt = userDAO.findById(userId);
            if (userOpt.isEmpty()) return null;

            User user = userOpt.get();
            if (user.getStatus() != UserStatus.ACTIVE) {
                return null;
            }

            // Load roles
            UserStoreRoleDAO roleDAO = new UserStoreRoleDAOImpl(conn);
            List<UserStoreRole> roles = roleDAO.findByUserId(user.getId());

            // Map to AuthUser
            AuthUser authUser = UserMapper.toAuthenticatedUser(user, roles);

            // Create new session (this will also revoke old session & tokens)
            return newLoginSession(req, authUser);

        } catch (SQLException e) {
            LoggerUtil.warn(AuthSessionService.class,
                    "Failed to restore session by token: " + e.getMessage());
            return null;
        }
    }

    /**
     * Helper method for logout by token.
     * It revokes the token in the database. The HTTP session should
     * still be invalidated separately (e.g. in LogoutServlet).
     *
     * @param rawToken raw auth token string from cookies
     */
    public void logoutByToken(String rawToken) {
        tokenService.revokeByRawToken(rawToken);
    }
}