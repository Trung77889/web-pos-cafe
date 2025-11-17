package com.laptrinhweb.zerostarcafe.domain.auth_token;

import com.laptrinhweb.zerostarcafe.core.database.DBConnection;
import com.laptrinhweb.zerostarcafe.core.security.TokenUtil;
import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import jakarta.servlet.http.HttpServletRequest;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * Manages long-lived auth tokens stored in the {@code auth_tokens} table.
 * Responsible for issuing, hashing, saving, finding, and revoking tokens.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * // Issue a new token for a user after login
 * String rawToken = authTokenService.issueToken(req, userId, deviceId, expiredAt);
 *
 * // Find a valid token from the raw token (from a cookie)
 * Optional<AuthToken> tokenOpt = authTokenService.findValidByRawToken(rawToken);
 * }</pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 16/11/2025
 * @since 1.0.0
 */
public class AuthTokenService {

    /**
     * Issues a new auth token, hashes it, and saves it to the DB.
     *
     * @param request   the current request
     * @param userId    the user ID who owns the token
     * @param deviceId  the device/browser identifier
     * @param expiredAt the token's expiration time
     * @return the raw token (for the cookie) or {@code null} if the DB save fails
     */
    public String issueToken(HttpServletRequest request,
                             Long userId,
                             String deviceId,
                             LocalDateTime expiredAt) {

        String authToken = TokenUtil.generateToken();
        String authHash = TokenUtil.hashToken(authToken);

        AuthToken token = new AuthToken(
                request,
                userId,
                authHash,
                deviceId,
                TokenStatus.ACTIVE,
                expiredAt
        );

        try (Connection conn = DBConnection.getConnection()) {
            AuthTokenDAO dao = new AuthTokenDAOImpl(conn);
            dao.save(token);
            return authToken;

        } catch (SQLException e) {
            LoggerUtil.error(AuthTokenService.class,
                    "Failed to issue auth token for userId=" + userId, e);
            return null;
        }
    }

    /**
     * Revokes all tokens for a specific user.
     *
     * @param userId the user's ID; does nothing if {@code null}
     */
    public void revokeAllByUserId(Long userId) {
        if (userId == null)
            return;

        try (Connection conn = DBConnection.getConnection()) {
            AuthTokenDAO dao = new AuthTokenDAOImpl(conn);
            dao.revokeAllByUserId(userId);
        } catch (SQLException e) {
            LoggerUtil.error(AuthTokenService.class,
                    "Failed to issue auth token for userId=" + userId, e);
        }
    }

    /**
     * Revokes a single token based on the raw token.
     *
     * @param rawToken the raw token string; does nothing if {@code null} or blank
     */
    public void revokeByRawToken(String rawToken) {
        if (rawToken == null || rawToken.isBlank())
            return;

        String authHash = TokenUtil.hashToken(rawToken);

        try (Connection conn = DBConnection.getConnection()) {
            AuthTokenDAO dao = new AuthTokenDAOImpl(conn);
            dao.revokeByAuthHash(authHash);
        } catch (SQLException e) {
            LoggerUtil.warn(AuthTokenService.class,
                    "Failed to revoke token by hash: " + e.getMessage());
        }
    }

    /**
     * Finds an active auth token by its raw token string.
     *
     * @param rawToken the raw token string
     * @return an {@link Optional} containing the {@link AuthToken} if valid; otherwise empty
     */
    public Optional<AuthToken> findValidByRawToken(String rawToken) {
        if (rawToken == null || rawToken.isBlank())
            return Optional.empty();

        String authHash = TokenUtil.hashToken(rawToken);

        try (Connection conn = DBConnection.getConnection()) {
            AuthTokenDAO dao = new AuthTokenDAOImpl(conn);
            return dao.findValidByAuthHash(authHash);
        } catch (SQLException e) {
            LoggerUtil.warn(AuthTokenService.class,
                    "Failed to find token by hash: " + e.getMessage());
            return Optional.empty();
        }
    }

}
