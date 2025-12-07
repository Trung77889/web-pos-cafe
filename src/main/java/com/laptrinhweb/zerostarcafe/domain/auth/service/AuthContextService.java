package com.laptrinhweb.zerostarcafe.domain.auth.service;

import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;
import com.laptrinhweb.zerostarcafe.core.security.TokenUtil;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthContext;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthSession;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthToken;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthUser;
import com.laptrinhweb.zerostarcafe.domain.auth.request.AuthReqInfo;
import com.laptrinhweb.zerostarcafe.domain.user.model.UserRole;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * Builds and updates authentication contexts.
 * It creates new contexts on login, refreshes tokens when needed,
 * and calculates expiry times based on user roles.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * AuthContext ctx = contextService.create(user, reqInfo);
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.1
 * @lastModified 07/12/2025
 * @since 1.0.0
 */
public class AuthContextService {

    /**
     * Create a new authentication context for the given user and request.
     *
     * @param user    the authenticated user
     * @param reqInfo the request information
     * @return a new authentication context
     */
    public AuthContext create(
            AuthUser user,
            AuthReqInfo reqInfo
    ) {
        if (user == null || reqInfo == null)
            return null;

        // Create auth session info
        LocalDateTime expiredAt = resolveExpiredAt(user);
        LocalDateTime now = LocalDateTime.now();

        AuthSession sessionInfo = new AuthSession();
        sessionInfo.setExpiredAt(expiredAt);
        sessionInfo.setLastRotatedAt(now);

        // Create token
        List<AuthToken> tokens = new ArrayList<>();
        AuthToken authToken = new AuthToken(
                SecurityKeys.TOKEN_AUTH,
                TokenUtil.generateToken(),
                expiredAt
        );
        tokens.add(authToken);

        String deviceId = reqInfo.getCookieValue(SecurityKeys.TOKEN_DEVICE_ID);
        AuthToken deviceToken = new AuthToken(
                SecurityKeys.TOKEN_DEVICE_ID,
                deviceId != null ? deviceId : TokenUtil.generateToken(),
                expiredAt
        );
        tokens.add(deviceToken);

        // Create Context
        AuthContext context = new AuthContext();
        context.setAuthUser(user);
        context.setSessionInfo(sessionInfo);
        context.setTokens(tokens);

        return context;
    }

    /**
     * Refresh an existing authentication context with new tokens.
     */
    public void refresh(AuthContext context) {
        if (context == null || !context.isValid()) {
            return;
        }

        AuthSession session = context.getSessionInfo();
        session.setLastRotatedAt(LocalDateTime.now());

        context.updateToken(
                SecurityKeys.TOKEN_AUTH,
                TokenUtil.generateToken(),
                session.getExpiredAt()
        );
    }

    /**
     * Resolve the expiry time for the given user based on user role.
     *
     * @param user the authenticated user
     * @return the calculated expiry time
     */
    public LocalDateTime resolveExpiredAt(AuthUser user) {
        LocalDateTime now = LocalDateTime.now();

        if (user == null)
            return now.plusMinutes(SecurityKeys.DEFAULT_SESSION_TTL);

        if (user.hasRole(UserRole.SUPER_ADMIN))
            return now.plusMinutes(SecurityKeys.SUPER_ADMIN_SESSION_TTL);

        if (user.hasRole(UserRole.STORE_MANAGER))
            return now.plusMinutes(SecurityKeys.MANAGER_SESSION_TTL);

        if (user.hasRole(UserRole.STAFF))
            return LocalDate.now().atTime(23, 59);

        return now.plusMinutes(SecurityKeys.DEFAULT_SESSION_TTL);
    }
}
