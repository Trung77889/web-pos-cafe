package com.laptrinhweb.zerostarcafe.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description: Utility class providing centralized logging functionality using SLF4J.
 * This class wraps SLF4J logging methods to provide a consistent logging interface across the application.
 * <p> This class cannot be instantiated or extended. </p>
 *
 * <h2> Example usage: </h2>
 * <pre>{@code
 * AppLogger.info(MyClass.class, "Application started");
 * AppLogger.error(MyClass.class, "Database error", e);
 * }</pre>
 *
 * @author Dang Van Trung
 * @date 21/10/2025
 */
public final class AppLogger {
    private AppLogger() {
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

