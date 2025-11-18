package com.laptrinhweb.zerostarcafe.domain.auth.service;

import com.laptrinhweb.zerostarcafe.core.database.DBConnection;
import com.laptrinhweb.zerostarcafe.core.security.PasswordUtil;
import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import com.laptrinhweb.zerostarcafe.domain.auth.dto.LoginDTO;
import com.laptrinhweb.zerostarcafe.domain.auth.dto.RegisterDTO;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthResult;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthStatus;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthUser;
import com.laptrinhweb.zerostarcafe.domain.user.UserMapper;
import com.laptrinhweb.zerostarcafe.domain.user.dao.UserDAO;
import com.laptrinhweb.zerostarcafe.domain.user.dao.UserDAOImpl;
import com.laptrinhweb.zerostarcafe.domain.user.model.User;
import com.laptrinhweb.zerostarcafe.domain.user.model.UserStatus;
import com.laptrinhweb.zerostarcafe.domain.user_role.UserStoreRole;
import com.laptrinhweb.zerostarcafe.domain.user_role.UserStoreRoleDAO;
import com.laptrinhweb.zerostarcafe.domain.user_role.UserStoreRoleDAOImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * Provides authentication business logic including registration and login.
 * Returns {@link AuthResult} to indicate success or failure.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * AuthService auth = new AuthService();
 * LoginDTO dto = new LoginDTO(username, password);
 *
 * AuthResult<AuthStatus, Void> result = auth.login(dto);
 * if (result.success()) {
 *     // Show success message ...
 * }
 * }</pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 14/11/2025
 * @since 1.0.0
 */
public final class AuthService {

    /**
     * Registers a new user.
     * Checks for duplicate email/username, hashes the password,
     * and saves the user to the database.
     *
     * @param form registration input
     * @return result showing success or failure
     */
    public AuthResult<AuthStatus, Void> register(RegisterDTO form) {
        try (Connection conn = DBConnection.getConnection()) {
            // Initialize data access objects
            UserDAO userDao = new UserDAOImpl(conn);

            // Check for duplicate email
            if (userDao.existsByEmail(form.email()))
                return AuthResult.fail(AuthStatus.EMAIL_EXISTS);

            // Check for duplicate username
            if (userDao.existsByUsername(form.username()))
                return AuthResult.fail(AuthStatus.USERNAME_EXISTS);

            // Hash password securely (Argon2)
            String hashedPassword = PasswordUtil.hash(form.password());

            // Create and persist new user
            User newUser = UserMapper.fromRegister(form);
            newUser.setPasswordHash(hashedPassword);

            boolean created = userDao.save(newUser);
            if (!created)
                return AuthResult.fail(AuthStatus.REGISTER_FAILED);

            LoggerUtil.info(AuthService.class, "NEW USER REGISTERED: " + newUser.getUsername());
            return AuthResult.ok(AuthStatus.REGISTER_SUCCESS);

        } catch (SQLException e) {
            LoggerUtil.warn(AuthService.class, "SQL ERROR: " + e.getMessage());
            return AuthResult.fail(AuthStatus.SERVER_ERROR);
        }
    }

    /**
     * Logs in a user.
     * Verifies credentials, checks account status and loads user roles.
     *
     * @param dto login input
     * @return result containing an {@link AuthUser} or failure status
     */
    public AuthResult<AuthStatus, AuthUser> login(LoginDTO dto) {
        try (Connection conn = DBConnection.getConnection()) {
            // Initialize data access objects
            UserDAO userDao = new UserDAOImpl(conn);
            UserStoreRoleDAO roleDao = new UserStoreRoleDAOImpl(conn);

            // Find user by username
            Optional<User> userOpt = userDao.findByUsername(dto.username());
            if (userOpt.isEmpty())
                return AuthResult.fail(AuthStatus.ACCOUNT_NOT_FOUND);

            User user = userOpt.get();

            // Verify password
            if (!PasswordUtil.verify(dto.password(), user.getPasswordHash()))
                return AuthResult.fail(AuthStatus.INVALID_CREDENTIALS);

            // Check account status
            if (!(user.getStatus() == UserStatus.ACTIVE))
                return AuthResult.fail(AuthStatus.ACCOUNT_INACTIVE);

            // Load user roles and map to session model
            List<UserStoreRole> roles = roleDao.findByUserId(user.getId());
            AuthUser authUser = UserMapper.toAuthenticatedUser(user, roles);

            LoggerUtil.info(AuthService.class, "LOGIN SUCCESS: User=" + user.getUsername());
            return AuthResult.ok(AuthStatus.LOGIN_SUCCESS, authUser);

        } catch (SQLException e) {
            LoggerUtil.warn(AuthService.class, "SQL ERROR: " + e.getMessage());
            return AuthResult.fail(AuthStatus.SERVER_ERROR);
        }
    }
}
