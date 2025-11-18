package com.laptrinhweb.zerostarcafe.domain.auth_token;

import java.sql.SQLException;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * Provides data access operations for the {@link AuthToken} entity,
 * representing long-lived authentication tokens used for session management.
 * </p>
 *
 * <h2>Responsibilities:</h2>
 * <ul>
 *   <li>Persist new authentication tokens</li>
 *   <li>Lookup valid tokens by authHash</li>
 *   <li>Revoke specific tokens</li>
 *   <li>Revoke all tokens belonging to a user</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * AuthTokenDAO dao = new AuthTokenDAOImpl(connection);
 * dao.save(token);
 *
 * Optional<AuthToken> t = dao.findValidByAuthHash(hash);
 * }</pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 16/11/2025
 * @since 1.0.0
 */
public interface AuthTokenDAO {

    /**
     * Persists a new {@link AuthToken}.
     *
     * @param token the token to insert
     * @throws SQLException if a database access error occurs
     */
    void save(AuthToken token) throws SQLException;

    /**
     * Retrieves a valid token by its authHash.
     * A token is valid if it is not expired and not revoked.
     *
     * @param authHash unique token hash
     * @return optional found token
     * @throws SQLException if a database access error occurs
     */
    Optional<AuthToken> findValidByAuthHash(String authHash) throws SQLException;

    /**
     * Revokes all tokens associated with a user.
     *
     * @param userId the user ID
     * @throws SQLException if a database access error occurs
     */
    void revokeAllByUserId(Long userId) throws SQLException;

    /**
     * Revokes a single token by auth hash.
     *
     * @param authHash the hash of the token
     * @throws SQLException if a database access error occurs
     */
    void revokeByAuthHash(String authHash) throws SQLException;
}