package com.laptrinhweb.zerostarcafe.domain.auth.record;

import com.laptrinhweb.zerostarcafe.core.exception.AppException;
import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;
import com.laptrinhweb.zerostarcafe.core.security.TokenUtil;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthContext;
import com.laptrinhweb.zerostarcafe.domain.auth.model.TokenStatus;
import com.laptrinhweb.zerostarcafe.domain.auth.request.AuthReqInfo;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * Manages authentication record storage in the database.
 * Supports creating, updating, validating, and revoking auth records.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * AuthRecordService service = new AuthRecordService(conn);
 * service.create(context, reqInfo);
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 29/11/2025
 * @since 1.0.0
 */
public class AuthRecordService {

    private final AuthRecordDAO recordDAO;

    public AuthRecordService(Connection conn) {
        this.recordDAO = new AuthRecordDAOImpl(conn);
    }

    /**
     * Create a new auth record based on the given context and request info.
     *
     * @param ctx     the authentication context
     * @param reqInfo the request information (IP, user-agent, cookies)
     */
    public void create(
            AuthContext ctx,
            AuthReqInfo reqInfo
    ) {
        try {
            AuthRecord record = new AuthRecord();

            Long userId = ctx.getAuthUser().id();
            record.setUserId(userId);

            record.setAuthHash(TokenUtil.hashToken(ctx.getTokenValue(SecurityKeys.TOKEN_AUTH)));
            record.setDeviceId(TokenUtil.hashToken(ctx.getTokenValue(SecurityKeys.TOKEN_DEVICE_ID)));
            record.setStatus(TokenStatus.ACTIVE);

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiredAt = ctx.getSessionInfo().getExpiredAt();

            record.setCreatedAt(now);
            record.setExpiredAt(expiredAt);
            record.setLastRotatedAt(now);

            record.setIpLast(reqInfo.getIpAddress());
            record.setUserAgent(reqInfo.getUserAgent());

            revokeAllByUserId(userId);
            recordDAO.save(record);

        } catch (SQLException e) {
            throw new AppException("FAIL TO INSERT NEW AUTH RECORD: " + e.getMessage(), e);
        }
    }

    /**
     * Update an existing auth record using the latest context data.
     *
     * @param ctx     the new authentication context
     * @param reqInfo the request information
     */
    public void updateByContext(
            AuthContext ctx,
            AuthReqInfo reqInfo
    ) {

        if (ctx == null || !ctx.isValid())
            return;

        try {
            String authToken = ctx.getTokenValue(SecurityKeys.TOKEN_AUTH);
            String authHash = TokenUtil.hashToken(authToken);

            Optional<AuthRecord> recordOpt = recordDAO.findValidByAuthHash(authHash);
            if (recordOpt.isEmpty())
                return;

            AuthRecord record = recordOpt.get();

            record.setAuthHash(authHash);
            record.setIpLast(reqInfo.getIpAddress());
            record.setUserAgent(reqInfo.getUserAgent());
            record.setLastRotatedAt(ctx.getSessionInfo().getLastRotatedAt());

            recordDAO.save(record);

        } catch (SQLException e) {
            throw new AppException("UPDATE AUTH RECORD FAIL: " + e.getMessage(), e);
        }
    }

    /**
     * Update an auth record using the previous token value.
     *
     * @param ctx      the new authentication context
     * @param reqInfo  the request information
     * @param oldToken the previous raw token value
     */
    public void updateByToken(
            AuthContext ctx,
            AuthReqInfo reqInfo,
            String oldToken
    ) {
        if (ctx == null || !ctx.isValid())
            return;

        try {
            String oldHash = TokenUtil.hashToken(oldToken);
            Optional<AuthRecord> tokenOpt = recordDAO.findValidByAuthHash(oldHash);

            if (tokenOpt.isEmpty())
                return;

            AuthRecord token = tokenOpt.get();

            String newToken = ctx.getTokenValue(SecurityKeys.TOKEN_AUTH);
            String newHash = TokenUtil.hashToken(newToken);

            token.setAuthHash(newHash);
            token.setIpLast(reqInfo.getIpAddress());
            token.setUserAgent(reqInfo.getUserAgent());
            token.setLastRotatedAt(ctx.getSessionInfo().getLastRotatedAt());

            recordDAO.save(token);

        } catch (SQLException e) {
            throw new AppException("UPDATE AUTH RECORD FAIL: " + e.getMessage(), e);
        }
    }

    /**
     * Revoke all active auth records belonging to the given user.
     *
     * @param userId the user ID
     */
    public void revokeAllByUserId(Long userId) {
        if (userId == null)
            return;

        try {
            recordDAO.revokeAllByUserId(userId);
        } catch (SQLException e) {
            throw new AppException("FAILED TO REVOKE RECORD BY USER_ID=" + userId, e);
        }
    }

    /**
     * Revoke a record using a raw token from the client.
     *
     * @param rawToken the token value from cookies
     */
    public void revokeByRawToken(String rawToken) {
        if (rawToken == null || rawToken.isBlank())
            return;

        String hash = TokenUtil.hashToken(rawToken);

        try {
            recordDAO.revokeByAuthHash(hash);
        } catch (SQLException e) {
            throw new AppException("FAILED TO FIND VALID AUTH RECORD: " + e.getMessage(), e);
        }
    }

    /**
     * Find the valid (not expired and not revoked) record by raw token.
     *
     * @param rawToken the token value from cookies
     * @return an optional containing the valid record, or empty if none found
     */
    public Optional<AuthRecord> findValidByRawToken(String rawToken) {
        if (rawToken == null || rawToken.isBlank())
            return Optional.empty();

        try {
            String hash = TokenUtil.hashToken(rawToken);
            return recordDAO.findValidByAuthHash(hash);

        } catch (SQLException e) {
            throw new AppException("FAILED TO FIND VALID AUTH RECORD: " + e.getMessage(), e);
        }
    }
}