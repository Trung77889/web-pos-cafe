package com.laptrinhweb.zerostarcafe.domain.auth.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * Holds all authentication information for a logged-in user.
 * It includes user data, session info, and all active auth tokens.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 22/11/2025
 * @since 1.0.0
 */
public class AuthContext {
    private AuthUser authUser;
    private AuthSession sessionInfo;
    private List<AuthToken> tokens;

    public AuthContext() {
    }

    /**
     * Validates this authentication context.
     *
     * @return true if user, session, and tokens are valid
     */
    public boolean isValid() {
        return authUser != null
                && sessionInfo != null
                && !sessionInfo.isExpired()
                && tokens != null
                && !tokens.isEmpty();
    }

    /**
     * Gets the value of a token by its name.
     *
     * @param name the token name
     * @return the token value, or null if not found
     */
    public String getTokenValue(String name) {
        if (tokens == null || tokens.isEmpty() || name == null)
            return null;

        for (AuthToken token : tokens) {
            if (token.getName().equals(name)) {
                return token.getValue();
            }
        }

        return null;
    }

    /**
     * Updates a token with a new value and expiry time.
     *
     * @param name         the token name
     * @param newValue     the new token value
     * @param newExpiredAt the new expiry time
     */
    public void updateToken(String name, String newValue, LocalDateTime newExpiredAt) {
        if (tokens == null || tokens.isEmpty() || name == null)
            return;

        for (AuthToken t : tokens) {
            if (t.getName().equals(name)) {
                t.setValue(newValue);
                t.setExpiredAt(newExpiredAt);
                return;
            }
        }
    }

    public AuthUser getAuthUser() {
        return authUser;
    }

    public void setAuthUser(AuthUser authUser) {
        this.authUser = authUser;
    }

    public AuthSession getSessionInfo() {
        return sessionInfo;
    }

    public void setSessionInfo(AuthSession sessionInfo) {
        this.sessionInfo = sessionInfo;
    }

    public List<AuthToken> getTokens() {
        return tokens;
    }

    public void setTokens(List<AuthToken> tokens) {
        this.tokens = tokens;
    }
}
