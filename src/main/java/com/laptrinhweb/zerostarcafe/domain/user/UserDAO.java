package com.laptrinhweb.zerostarcafe.domain.user;

import java.sql.*;
import java.util.Optional;

/**
 * <p>
 * Provides low-level CRUD operations for interacting with the
 * <code>users</code> table, such as creating new accounts,
 * checking for duplicates, retrieving user data, and
 * updating basic fields like status or password.
 * </p>
 *
 * <h2>Responsibilities:</h2>
 * <ul>
 *   <li>Insert new user records and retrieve auto-generated IDs.</li>
 *   <li>Check if a username or email already exists.</li>
 *   <li>Fetch user details by username.</li>
 *   <li>Update status or password fields.</li>
 *   <li>Map {@link ResultSet} rows into {@link User} objects.</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * try (Connection conn = DBConnection.getConnection()) {
 *     UserDAO dao = new UserDAO(conn);
 *
 *     User newUser = new User("alice@example.com", "alice", hash("123"));
 *     if (!dao.existsByUsername(newUser.getUsername())) {
 *         dao.insert(newUser);
 *     }
 *
 *     Optional<User> found = dao.findByUsername("alice");
 *     found.ifPresent(u -> System.out.println(u.getEmail()));
 * }
 * }</pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 28/10/2025
 * @since 1.0.0
 */
public class UserDAO {
    private final Connection conn;

    /**
     * Constructs a new {@code UserDAO} with the given JDBC connection.
     * <p>
     * The connection should be obtained from the Tomcat-managed
     * DataSource via {@code DBConnection.getConnection()}.
     * </p>
     *
     * @param conn active JDBC connection (should not be null)
     */
    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    // ==========================================================
    // INSERT
    // ==========================================================

    /**
     * Inserts a new user record into the database.
     * Automatically retrieves the generated user ID
     * and sets it on the provided {@link User} object.
     *
     * @param user the {@link User} entity to insert
     * @return {@code true} if insertion succeeded, {@code false} otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean insert(User user) throws SQLException {
        String sql = """
                INSERT INTO users (email, username, password_hash, status, is_super_admin)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id"})) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getStatus());
            ps.setBoolean(5, user.isSuperAdmin());

            int affected = ps.executeUpdate();
            if (affected == 0) return false;

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getLong(1));
                }
            }
            return true;
        }
    }

    // ==========================================================
    // EXISTENCE CHECKS
    // ==========================================================

    /**
     * Checks whether a username already exists in the database.
     *
     * @param username the username to check
     * @return {@code true} if a matching user exists, {@code false} otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean existsByUsername(String username) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE username = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Checks whether an email address already exists in the database.
     *
     * @param email the email address to check
     * @return {@code true} if a user with this email exists, {@code false} otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE email = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // ==========================================================
    // RETRIEVAL
    // ==========================================================

    /**
     * Retrieves a user by their unique username.
     * <p>
     * Returns an {@link Optional} that either contains the
     * {@link User} object or is empty if no matching record is found.
     * </p>
     *
     * @param username the username to search for
     * @return an {@link Optional} containing the found user, or empty if not found
     * @throws SQLException if a database access error occurs
     */
    public Optional<User> findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return Optional.of(rowMapper(rs));
            }
        }
        return Optional.empty();
    }

    // ==========================================================
    // UPDATES
    // ==========================================================

    /**
     * Updates a user's account status (e.g., active, suspended, deleted).
     *
     * @param userId    the user ID
     * @param newStatus the new status string
     * @return {@code true} if the update succeeded, {@code false} otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean updateStatus(long userId, String newStatus) throws SQLException {
        String sql = "UPDATE users SET status = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setLong(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Updates a user's password hash.
     * Automatically refreshes the {@code updated_at} timestamp in the database.
     *
     * @param userId          the ID of the user
     * @param newPasswordHash the new hashed password
     * @return {@code true} if the update succeeded, {@code false} otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean updatePassword(long userId, String newPasswordHash) throws SQLException {
        String sql = "UPDATE users SET password_hash = ?, updated_at = NOW() WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPasswordHash);
            ps.setLong(2, userId);
            return ps.executeUpdate() > 0;
        }
    }

    // ==========================================================
    // MAPPING UTIL
    // ==========================================================

    /**
     * Maps a single {@link ResultSet} row into a {@link User} object.
     * Converts SQL {@link Timestamp} values into {@link java.time.LocalDateTime}.
     *
     * @param rs the {@link ResultSet} positioned at a valid row
     * @return a populated {@link User} instance
     * @throws SQLException if a column read fails
     */
    private User rowMapper(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getLong("id"));
        u.setEmail(rs.getString("email"));
        u.setUsername(rs.getString("username"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setOauthProvider(rs.getString("oauth_provider"));
        u.setOauthId(rs.getString("oauth_id"));
        u.setStatus(rs.getString("status"));
        u.setSuperAdmin(rs.getBoolean("is_super_admin"));

        Timestamp cAt = rs.getTimestamp("created_at");
        Timestamp uAt = rs.getTimestamp("updated_at");
        if (cAt != null) u.setCreatedAt(cAt.toLocalDateTime());
        if (uAt != null) u.setUpdatedAt(uAt.toLocalDateTime());
        return u;
    }
}
