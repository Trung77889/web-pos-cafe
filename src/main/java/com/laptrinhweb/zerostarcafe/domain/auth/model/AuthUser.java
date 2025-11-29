package com.laptrinhweb.zerostarcafe.domain.auth.model;

import com.laptrinhweb.zerostarcafe.domain.user.model.UserRole;

import java.io.Serializable;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * Session view of an authenticated user.
 * This object is stored inside {@link jakarta.servlet.http.HttpSession}
 * and is used for authorization checks within the application.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * AuthUser user = new AuthUser(id, username, email, isSuperAdmin, roles);
 * session.setAttribute("AUTH_USER", user);
 * }</pre>
 *
 * @author Dang Van Trung
 * @version 1.0.1
 * @lastModified 22/11/2025
 * @since 1.0.0
 */
public record AuthUser(Long id, String username, String email, boolean isSuperAdmin,
                       List<String> roleCodes) implements Serializable {

    public AuthUser(Long id,
                    String username,
                    String email,
                    boolean isSuperAdmin,
                    List<String> roleCodes) {

        this.id = id;
        this.username = username;
        this.email = email;
        this.isSuperAdmin = isSuperAdmin;
        this.roleCodes = (roleCodes == null)
                ? List.of()
                : List.copyOf(roleCodes);
    }

    /**
     * Checks if this user owns the given {@link UserRole}.
     *
     * @param role type-safe role enum
     * @return {@code true} if the role is present
     */
    public boolean hasRole(UserRole role) {
        return role != null && this.roleCodes.contains(role.getCode());
    }
}