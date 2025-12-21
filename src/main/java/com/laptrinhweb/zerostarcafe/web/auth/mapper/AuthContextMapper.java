package com.laptrinhweb.zerostarcafe.web.auth.mapper;

import com.laptrinhweb.zerostarcafe.core.security.SecurityKeys;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthContext;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthSession;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthToken;
import com.laptrinhweb.zerostarcafe.domain.auth.model.AuthUser;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2>Description:</h2>
 * <p>
 * Maps data stored in {@link HttpSession} and the current request
 * into a domain {@link AuthContext}.
 * </p>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 22/11/2025
 * @since 1.0.0
 */
public final class AuthContextMapper {

    private AuthContextMapper() {
    }

    public static AuthContext from(HttpSession session) {
        if (session == null)
            return null;

        AuthUser authUser = (AuthUser)
                session.getAttribute(SecurityKeys.SESSION_AUTH_USER);

        AuthSession sessionInfo = (AuthSession)
                session.getAttribute(SecurityKeys.SESSION_AUTH_SESSION);

        @SuppressWarnings("unchecked")
        List<AuthToken> tokens = (List<AuthToken>)
                session.getAttribute(SecurityKeys.SESSION_AUTH_TOKENS);
        if (tokens == null) {
            tokens = new ArrayList<>();
        }

        AuthContext ctx = new AuthContext();
        ctx.setAuthUser(authUser);
        ctx.setSessionInfo(sessionInfo);
        ctx.setTokens(tokens);

        return ctx;
    }
}
