package com.laptrinhweb.zerostarcafe.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 * <p>
 * Providing centralized logging functionality using SLF4J.
 * This class wraps SLF4J logging methods to provide a consistent
 * logging interface across the application.
 * </p>
 *
 * <h2> Example usage: </h2>
 * <pre>{@code
 * LoggerUtil.info(MyClass.class, "Application started");
 * LoggerUtil.error(MyClass.class, "Database error", e);
 * }</pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 21/10/2025
 * @since 1.0.0
 */
public final class LoggerUtil {
    private LoggerUtil() {
    }

    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    public static void info(Class<?> clazz, String message) {
        getLogger(clazz).info(message);
    }

    public static void error(Class<?> clazz, String message, Throwable e) {
        getLogger(clazz).error(message, e);
    }

    public static void warn(Class<?> clazz, String message) {
        getLogger(clazz).warn(message);
    }

    public static void debug(Class<?> clazz, String message) {
        getLogger(clazz).debug(message);
    }
}

