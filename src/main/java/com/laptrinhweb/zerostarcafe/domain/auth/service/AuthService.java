package com.laptrinhweb.zerostarcafe.domain.auth.service;

import com.laptrinhweb.zerostarcafe.core.database.DBConnection;
import com.laptrinhweb.zerostarcafe.core.exception.AppException;
import com.laptrinhweb.zerostarcafe.core.security.PasswordUtil;
import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;
import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import com.laptrinhweb.zerostarcafe.domain.auth.dto.LoginDTO;
import com.laptrinhweb.zerostarcafe.domain.auth.dto.RegisterDTO;
import com.laptrinhweb.zerostarcafe.domain.auth.model.*;
import com.laptrinhweb.zerostarcafe.domain.auth.record.AuthRecord;
import com.laptrinhweb.zerostarcafe.domain.auth.record.AuthRecordService;
import com.laptrinhweb.zerostarcafe.domain.auth.request.AuthReqInfo;
import com.laptrinhweb.zerostarcafe.domain.user.model.User;
import com.laptrinhweb.zerostarcafe.domain.user.model.UserMapper;
import com.laptrinhweb.zerostarcafe.domain.user.service.UserService;
import com.laptrinhweb.zerostarcafe.domain.user_role.UserStoreRole;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <h2>Description:</h2>
 * <p>
 * Provides core authentication business logic including registration,
 * login, credential verification, session/context creation, token validation,
 * rotation handling, and restoration from persisted auth records.
 * <br/><br/>
 * All methods return {@link AuthResult} or {@link AuthContext} to ensure
 * a unified and predictable contract for the web layer.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * AuthService auth = new AuthService();
 * LoginDTO dto = new LoginDTO(username, password);
 *
 * AuthResult<AuthStatus, AuthContext> result =
 *         auth.authenticate(dto, reqInfo);
 *
 * if (result.success()) {
 *     // Use AuthContext ...
 * }
 * }</pre>
 *
 * @author Dang Van Trung
 * @version 1.2.1
 * @lastModified 07/12/2025
 * @since 1.0.0
 */
public final class AuthService {

    /**
     * Registers a new user.
     *
     * @param form registration input
     * @return AuthResult indicating success or failure
     */
    public AuthResult<AuthStatus, Void> register(RegisterDTO form) {
        try (Connection conn = DBConnection.getConnection()) {
            UserService userService = new UserService(conn);

            // Check duplicate
            if (userService.existsByEmail(form.email()))
                return AuthResult.fail(AuthStatus.EMAIL_EXISTS);

            if (userService.existsByUsername(form.username()))
                return AuthResult.fail(AuthStatus.USERNAME_EXISTS);

            // Hash password securely (Argon2)
            String hashedPassword = PasswordUtil.hash(form.password());

            // Create and persist new user
            User newUser = UserMapper.fromRegister(form);
            newUser.setPasswordHash(hashedPassword);

            boolean created = userService.save(newUser);
            if (!created)
                return AuthResult.fail(AuthStatus.REGISTER_FAILED);

            LoggerUtil.info(AuthService.class,
                    "NEW USER REGISTERED: " + newUser.getUsername());
            return AuthResult.ok(AuthStatus.REGISTER_SUCCESS);

        } catch (Exception e) {
            LoggerUtil.error(AuthService.class, e.getMessage(), e);
            return AuthResult.fail(AuthStatus.REGISTER_FAILED);
        }
    }

    /**
     * Authenticates a user and creates a new AuthContext.
     *
     * @param form    login input
     * @param reqInfo request metadata (ip, agent, device-id)
     * @return AuthResult with AuthContext or failure status
     */
    public AuthResult<AuthStatus, AuthContext> authenticate(LoginDTO form, AuthReqInfo reqInfo) {
        try (Connection conn = DBConnection.getConnection()) {
            AuthContextService contextService = new AuthContextService();
            AuthRecordService recordService = new AuthRecordService(conn);

            AuthUser authUser = verifyCredential(form);
            if (authUser == null)
                return AuthResult.fail(AuthStatus.INVALID_CREDENTIALS);

            AuthContext context = contextService.create(authUser, reqInfo);
            recordService.create(context, reqInfo);

            return AuthResult.ok(AuthStatus.LOGIN_SUCCESS, context);
        } catch (Exception e) {
            LoggerUtil.error(AuthService.class, e.getMessage(), e);
            return AuthResult.fail(AuthStatus.LOGIN_FAILED);
        }
    }

    /**
     * Re-validates an existing context and rotates tokens if required.
     *
     * @param context   current authentication context
     * @param reqInfo   request metadata
     * @param reqTokens tokens extracted from client cookies
     * @return true if the context is still valid after re-validation, false otherwise
     */
    public boolean reAuthenticate(
            AuthContext context,
            AuthReqInfo reqInfo,
            Map<String, String> reqTokens
    ) {
        if (context == null || !context.isValid())
            return false;

        AuthSession session = context.getSessionInfo();
        if (session == null || session.isExpired())
            return false;

        List<AuthToken> tokens = context.getTokens();
        if (!isValidTokens(reqTokens, tokens))
            return false;

        if (session.shouldRotated()) {
            String oldToken = context.getTokenValue(SecurityKeys.TOKEN_AUTH);

            AuthContextService contextService = new AuthContextService();
            contextService.refresh(context);

            try (Connection conn = DBConnection.getConnection()) {
                AuthRecordService recordService = new AuthRecordService(conn);
                recordService.updateByToken(context, reqInfo, oldToken);
            } catch (Exception e) {
                LoggerUtil.error(AuthService.class, e.getMessage(), e);
                return false;
            }
        }

        return true;
    }

    /**
     * Restores authentication context from persisted AuthRecord.
     *
     * @param reqInfo   request metadata
     * @param reqTokens tokens from client cookies
     * @return restored AuthContext or null if not restorable
     */
    public AuthContext restore(
            AuthReqInfo reqInfo,
            Map<String, String> reqTokens
    ) {
        if (reqInfo == null || reqTokens == null || reqTokens.isEmpty())
            return null;

        String authToken = reqTokens.get(SecurityKeys.TOKEN_AUTH);
        String deviceToken = reqTokens.get(SecurityKeys.TOKEN_DEVICE_ID);

        if (authToken == null || deviceToken == null)
            return null;

        try (Connection conn = DBConnection.getConnection()) {
            AuthContextService contextService = new AuthContextService();
            AuthRecordService recordService = new AuthRecordService(conn);
            UserService userService = new UserService(conn);

            Optional<AuthRecord> recordOpt = recordService.findValidByRawToken(authToken);
            if (recordOpt.isEmpty())
                return null;

            AuthRecord record = recordOpt.get();

            if (LocalDateTime.now().isAfter(record.getExpiredAt()))
                return null;

            if (!deviceToken.equals(record.getDeviceId()))
                return null;

            Long userId = record.getUserId();

            User user = userService.getActiveById(userId);
            if (user == null)
                return null;

            List<UserStoreRole> roles = userService.getRolesOf(user);
            AuthUser authUser = UserMapper.toAuthenticatedUser(user, roles);

            AuthContext newContext = contextService.create(authUser, reqInfo);

            AuthSession session = newContext.getSessionInfo();
            session.setExpiredAt(record.getExpiredAt());

            recordService.updateByContext(newContext, reqInfo);
            return newContext;

        } catch (Exception e) {
            LoggerUtil.error(AuthService.class, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Revokes authentication state associated with the given token.
     *
     * @param token raw auth token
     */
    public void clearAuthState(String token) {
        try (Connection conn = DBConnection.getConnection()) {
            AuthRecordService recordService = new AuthRecordService(conn);

            if (token == null || token.isBlank())
                return;

            recordService.revokeByRawToken(token);
        } catch (Exception e) {
            LoggerUtil.error(AuthService.class, e.getMessage(), e);
        }
    }

    /**
     * Verifies user credentials and loads roles.
     *
     * @param dto login input
     * @return authenticated AuthUser or null if invalid
     */
    public AuthUser verifyCredential(LoginDTO dto) {
        try (Connection conn = DBConnection.getConnection()) {
            UserService userService = new UserService(conn);

            User user = userService.getActiveByUsername(dto.username());
            if (user == null)
                return null;

            if (!PasswordUtil.verify(dto.password(), user.getPasswordHash()))
                return null;

            List<UserStoreRole> roles = userService.getRolesOf(user);

            return UserMapper.toAuthenticatedUser(user, roles);
        } catch (Exception e) {
            throw new AppException("FAIL TO VERIFY CREDENTIAL: " + e.getMessage(), e);
        }
    }

    /**
     * Validates request tokens against server tokens.
     *
     * @param reqTokens  tokens from the client
     * @param authTokens tokens stored on server
     * @return true if all tokens match and are valid
     */
    private boolean isValidTokens(
            Map<String, String> reqTokens,
            List<AuthToken> authTokens
    ) {
        if (reqTokens == null || reqTokens.isEmpty())
            return false;

        if (authTokens == null || authTokens.isEmpty())
            return false;

        for (AuthToken serverToken : authTokens) {
            String name = serverToken.getName();
            String value = reqTokens.get(name);

            if (value == null)
                return false;

            if (!value.equals(serverToken.getValue()))
                return false;

            if (serverToken.isExpired())
                return false;
        }

        return true;
    }
}