package com.laptrinhweb.zerostarcafe.core.utils;

import jakarta.servlet.ServletContext;

import java.util.*;

/**
 * <h2>Description:</h2>
 * <p>
 * Provides centralized internationalization (i18n) management for the web application.
 * It loads translation bundles according to the current locale and exposes a simple
 * {@link #trans(String)} method to resolve translation keys.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * // Inside LocaleFilter:
 * I18n i18n = new I18n(locale, request.getServletContext());
 * session.setAttribute("i18n", i18n);
 *
 * // In JSP:
 * ${i18n.trans('form.username')}
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 10/11/2025
 * @since 1.0.0
 */
public final class I18n {

    // Default translation bundle namespaces used across the app.
    private static final List<String> BUNDLES = List.of("form", "message", "general");

    private final Locale locale;
    private final Map<String, ResourceBundle> bundles = new HashMap<>();

    /**
     * Creates an {@code I18n} manager for the given locale.
     * It automatically loads all bundles defined in {@code web.xml} context-param.
     *
     * @param locale  the active {@link Locale} for this session
     * @param context the {@link ServletContext} used to read global configuration
     */
    public I18n(Locale locale, ServletContext context) {
        this.locale = locale;
        loadBundles(context);
    }

    /**
     * Loads all ResourceBundles defined in the web.xml context-param
     *
     * @param context the servlet context containing i18n configuration
     */
    private void loadBundles(ServletContext context) {
        for (String name : BUNDLES) {
            String path = String.format("translate/%s/%s", locale.toLanguageTag(), name);
            try {
                bundles.put(name, ResourceBundle.getBundle(path));
            } catch (MissingResourceException e) {
                LoggerUtil.warn(I18n.class, "Missing bundle: " + path);
            }
        }
    }

    /**
     * Translates a given key using the appropriate bundle based on its namespace.
     * Example: "form.username" will look for key "form.username" in bundle "form".
     *
     * @param key the full translation key in the format "bundleName.keyName"
     * @return the translated string if found; otherwise, the key itself
     */
    public String trans(String key) {
        if (key == null || key.isBlank()) return "";
        int dot = key.indexOf('.');
        if (dot == -1) return key;

        String ns = key.substring(0, dot);
        ResourceBundle rb = bundles.get(ns);
        if (rb == null) return key;

        return rb.containsKey(key) ? rb.getString(key) : key;
    }

    /**
     * Gets the current locale used by this {@code I18n} instance.
     *
     * @return the active {@link Locale}
     */
    public Locale getLocale() {
        return locale;
    }
}
