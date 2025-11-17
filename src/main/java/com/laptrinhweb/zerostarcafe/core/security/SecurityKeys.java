package com.laptrinhweb.zerostarcafe.core.security;

/**
 * <h2>Description:</h2>
 * <p>
 * Centralizes authentication constants.
 * This class defines keys for cookies, session attributes,
 * request attributes, and application-scoped attributes
 * to improve maintainability and avoid magic strings.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 16/11/2025
 * @since 1.0.0
 */
public final class SecurityKeys {

    private SecurityKeys() {
    }

    public static final String COOKIE_AUTH_SESSION = "auth_token";
    public static final String COOKIE_DEVICE_ID = "device_id";
    public static final String SESSION_AUTH_USER = "authUser";
    public static final String SESSION_EXPIRED_AT = "expiredAt";
    public static final String SESSION_LAST_ROTATED = "lastRotatedAt";
    public static final String SESSION_DEVICE_ID = "deviceId";
    public static final String SESSION_AUTH_TOKEN = "authToken";
    public static final String CTX_AUTH_SESSION_SERVICE = "authSessionService";
}
