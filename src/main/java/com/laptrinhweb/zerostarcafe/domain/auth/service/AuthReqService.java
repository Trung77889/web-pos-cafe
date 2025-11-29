package com.laptrinhweb.zerostarcafe.domain.auth.service;

import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;
import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthContext;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthResult;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthStatus;
import com.laptrinhweb.zerostarcafe.domain.auth.request.AuthReqInfo;

import java.util.Map;

/**
 * <h2>Description:</h2>
 * <p>
 * Handles the authentication flow for each request.
 * It validates tokens, refreshes sessions, and restores user context.
 * This service decides the final auth status returned to the filter.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * AuthResult result = authReqService.handleRequest(reqInfo, currentContext);
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 29/11/2025
 * @since 1.0.0
 */
public class AuthReqService {

    private final AuthService authService;

    public AuthReqService() {
        this(new AuthService());
    }

    public AuthReqService(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Handle the full authentication flow for a request.
     *
     * @param reqInfo        request info from client
     * @param currentContext current auth session context, may be null
     * @return auth result containing status and optional new context
     */
    public AuthResult<AuthStatus, AuthContext> handleRequest(
            AuthReqInfo reqInfo,
            AuthContext currentContext
    ) {
        try {
            if (reqInfo == null)
                return AuthResult.fail(AuthStatus.SESSION_NOT_FOUND);

            Map<String, String> reqTokens =
                    reqInfo.extractTokensByPrefix(SecurityKeys.AUTH_COOKIE_PREFIX);

            if (reqTokens == null || reqTokens.isEmpty())
                return AuthResult.fail(AuthStatus.SESSION_NOT_FOUND);

            if (currentContext != null && currentContext.isValid())
                return reAuthenticateFlow(currentContext, reqInfo, reqTokens);

            return restoreFlow(reqInfo, reqTokens);
        } catch (Exception e) {
            LoggerUtil.error(AuthReqService.class,
                    "AUTH REQUEST FLOW ERROR: " + e.getMessage(), e);
            return AuthResult.fail(AuthStatus.SESSION_INVALID);
        }
    }

    /**
     * Process re-authentication using existing session and tokens.
     *
     * @param currentContext the current authentication context
     * @param reqInfo        the request information
     * @param reqTokens      the token map extracted from cookies
     * @return auth result containing status and optional updated context
     */
    private AuthResult<AuthStatus, AuthContext> reAuthenticateFlow(
            AuthContext currentContext,
            AuthReqInfo reqInfo,
            Map<String, String> reqTokens
    ) {
        AuthContext newContext = authService.reAuthenticate(currentContext, reqInfo, reqTokens);

        if (newContext == null || !newContext.isValid()) {
            String oldToken = currentContext.getTokenValue(SecurityKeys.TOKEN_AUTH);
            authService.clearAuthState(oldToken);

            return AuthResult.fail(AuthStatus.SESSION_INVALID);
        }

        String oldToken = currentContext.getTokenValue(SecurityKeys.TOKEN_AUTH);
        String newToken = newContext.getTokenValue(SecurityKeys.TOKEN_AUTH);

        boolean rotated = oldToken != null
                && newToken != null
                && !oldToken.equals(newToken);

        if (rotated)
            return AuthResult.ok(AuthStatus.SESSION_ROTATED, newContext);

        return AuthResult.ok(AuthStatus.SESSION_REUSED, newContext);
    }

    /**
     * Try to restore auth context when the request has valid cookies but no session.
     *
     * @param reqInfo   request information
     * @param reqTokens token map from cookies
     * @return auth result with status and restored context if valid
     */
    private AuthResult<AuthStatus, AuthContext> restoreFlow(
            AuthReqInfo reqInfo,
            Map<String, String> reqTokens
    ) {
        AuthContext restored = authService.restore(reqInfo, reqTokens);

        if (restored != null && restored.isValid())
            return AuthResult.ok(AuthStatus.SESSION_RESTORED, restored);

        String rawToken = reqTokens.get(SecurityKeys.TOKEN_AUTH);
        authService.clearAuthState(rawToken);

        return AuthResult.fail(AuthStatus.SESSION_INVALID);
    }
}
