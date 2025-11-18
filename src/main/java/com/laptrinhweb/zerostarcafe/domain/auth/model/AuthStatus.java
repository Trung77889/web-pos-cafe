package com.laptrinhweb.zerostarcafe.domain.auth.model;

/**
 * <h2>Description:</h2>
 * <p>
 * Defines common result statuses for authentication actions
 * such as login and registration.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 12/11/2025
 * @since 1.0.0
 */
public enum AuthStatus {
    LOGIN_SUCCESS,
    REGISTER_SUCCESS,
    SERVER_ERROR,
    EMAIL_EXISTS,
    USERNAME_EXISTS,
    REGISTER_FAILED,
    ACCOUNT_NOT_FOUND,
    INVALID_CREDENTIALS,
    ACCOUNT_INACTIVE
}