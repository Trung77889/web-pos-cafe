package com.laptrinhweb.zerostarcafe.web.common.filters;

import com.laptrinhweb.zerostarcafe.core.utils.I18n;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

/**
 * Resolves user locale (param → session → default) and prepares I18n for JSP.
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 10/11/2025
 * @since 1.0.0
 */
@WebFilter(filterName = "LocaleFilter", urlPatterns = {"/*"})
public class LocaleFilter implements Filter {

    private static final Set<String> SUPPORTED = Set.of("vi-VN", "en-US");
    private static final Locale DEFAULT = Locale.forLanguageTag("vi-VN");

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpSession session = request.getSession();

        // Resolve locale from param > session > default
        Locale locale = Optional.ofNullable(request.getParameter("lang"))
                .filter(SUPPORTED::contains)
                .map(Locale::forLanguageTag)
                .orElse((Locale) session.getAttribute("locale"));

        if (locale == null) locale = DEFAULT;
        session.setAttribute("locale", locale);

        // Ensure session I18n is up to date
        I18n i18n = (I18n) session.getAttribute("i18n");
        if (i18n == null || !i18n.getLocale().equals(locale)) {
            i18n = new I18n(locale, request.getServletContext());
            session.setAttribute("i18n", i18n);
        }

        chain.doFilter(req, resp);
    }
}