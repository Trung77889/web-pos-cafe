package com.laptrinhweb.zerostarcafe.domain.auth_token;

import java.sql.*;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * JDBC-based implementation of {@link AuthTokenDAO}.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 16/11/2025
 * @since 1.0.0
 */
public class AuthTokenDAOImpl implements AuthTokenDAO {

    private final Connection conn;

    public AuthTokenDAOImpl(Connection conn) {
        this.conn = conn;
    }

    // ==========================================================
    // SAVE (Insert or Update)
    // ==========================================================

    @Override
    public void save(AuthToken token) throws SQLException {
        String sql = """
                    INSERT INTO auth_tokens (
                        user_id, auth_hash, device_id, status,
                        expired_at, last_rotated_at,
                        ip_last, user_agent
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, token.getUserId());
            ps.setString(2, token.getAuthHash());
            ps.setString(3, token.getDeviceId());
            ps.setString(4, token.getStatus().name());
            ps.setTimestamp(5, Timestamp.valueOf(token.getExpiredAt()));
            ps.setTimestamp(6, Timestamp.valueOf(token.getLastRotatedAt()));
            ps.setString(7, token.getIpLast());
            ps.setString(8, token.getUserAgent());
            ps.executeUpdate();
        }
    }

    // ==========================================================
    // RETRIEVAL
    // ==========================================================

    @Override
    public Optional<AuthToken> findValidByAuthHash(String authHash) throws SQLException {
        String sql = """
                    SELECT * FROM auth_tokens
                    WHERE auth_hash = ?
                      AND status = 'ACTIVE'
                      AND expired_at > NOW()
                    LIMIT 1
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, authHash);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(rowMapper(rs));
            }
        }
    }

    // ==========================================================
    // REVOKE ALL BY USER ID
    // ==========================================================

    @Override
    public void revokeAllByUserId(Long userId) throws SQLException {
        String sql = """
                    UPDATE auth_tokens
                    SET status = 'REVOKED',
                        revoked_at = NOW(),
                        revoked_reason = 'revoked_by_system'
                    WHERE user_id = ?
                      AND status = 'ACTIVE'
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.executeUpdate();
        }
    }

    // =======================================================================
    // REVOKE SINGLE TOKEN
    // =======================================================================

    @Override
    public void revokeByAuthHash(String authHash) throws SQLException {
        String sql = """
                    UPDATE auth_tokens
                    SET status = 'REVOKED',
                        revoked_at = NOW(),
                        revoked_reason = 'revoked_single'
                    WHERE auth_hash = ?
                      AND status = 'ACTIVE'
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, authHash);
            ps.executeUpdate();
        }
    }

    // =======================================================================
    // ROW MAPPER
    // =======================================================================

    private AuthToken rowMapper(ResultSet rs) throws SQLException {
        AuthToken t = new AuthToken();

        t.setId(rs.getLong("id"));
        t.setUserId(rs.getLong("user_id"));
        t.setAuthHash(rs.getString("auth_hash"));
        t.setDeviceId(rs.getString("device_id"));

        String statusStr = rs.getString("status");
        if (statusStr != null) t.setStatus(TokenStatus.valueOf(statusStr));

        Timestamp expired = rs.getTimestamp("expired_at");
        if (expired != null) t.setExpiredAt(expired.toLocalDateTime());

        Timestamp rotated = rs.getTimestamp("last_rotated_at");
        if (rotated != null) t.setLastRotatedAt(rotated.toLocalDateTime());

        t.setIpLast(rs.getString("ip_last"));
        t.setUserAgent(rs.getString("user_agent"));

        Timestamp created = rs.getTimestamp("created_at");
        if (created != null) t.setCreatedAt(created.toLocalDateTime());

        Timestamp revokedAt = rs.getTimestamp("revoked_at");
        if (revokedAt != null) t.setRevokedAt(revokedAt.toLocalDateTime());

        t.setRevokedReason(rs.getString("revoked_reason"));

        return t;
    }
}