package com.laptrinhweb.zerostarcafe.web.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * <h2>Description:</h2>
 * <p>
 * LocaleFilter is responsible for determining the user's preferred language on every request.
 * The locale is resolved by the following priority: <br/>
 * Query parameter (?lang) -> Session attribute -> Default fallback of "vi-VN". <br/>
 * The resolved locale is then stored in the session and used to load ResourceBundle objects
 * exposed to JSP views for internationalized rendering.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 *   // Switch language via URL:
 *   http://localhost:8080/app?lang=en-US
 *
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 30/10/2025
 * @since 1.0.0
 */
@WebFilter(filterName = "LocaleFilter", urlPatterns = {"/*"})
public class LocaleFilter implements Filter {

    private static final Set<String> SUPPORTED = Set.of("vi-VN", "en-US");

    /**
     * Resolves locale (param > session > default), stores it in session,
     * loads translation bundles, attaches them to the request,
     * then continues the filter chain.
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        Locale locale = Optional.ofNullable(request.getParameter("lang"))
                .filter(SUPPORTED::contains)
                .map(Locale::forLanguageTag)
                .orElse((Locale) request.getSession().getAttribute("locale"));

        if (locale == null) {
            locale = Locale.forLanguageTag("vi-VN");
        }
        
        request.getSession().setAttribute("locale", locale);

        String[] bundleNames = {"message", "form", "general"};
        for (var bundle : bundleNames) {
            ResourceBundle rb = getResourceBundle(bundle, locale);
            request.setAttribute(bundle, rb);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private ResourceBundle getResourceBundle(String bundleName, Locale locale) {
        String BASE_PATH = "translate";
        String path = String.format("%s/%s/%s", BASE_PATH, locale.toLanguageTag(), bundleName);
        return ResourceBundle.getBundle(path);
    }
}
