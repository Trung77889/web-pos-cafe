package com.laptrinhweb.zerostarcafe.web.common.listeners;

import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * <h2>Description:</h2>
 * <p>
 * Application lifecycle listener that handles proper cleanup of resources
 * when the web application is started and stopped.
 * </p>
 *
 * <ul>
 *     This listener specifically addresses:
 *     <li>JDBC driver registration/unregistration</li>
 *     <li>MySQL connection cleanup thread</li>
 * </ul>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 28/12/2025
 * @since 1.0.0
 */
@WebListener
public class ApplicationLifecycleListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LoggerUtil.info(ApplicationLifecycleListener.class,
                "🚀 Zero Star Cafe application is ready !");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LoggerUtil.info(ApplicationLifecycleListener.class,
                "🔄 Zero Star Cafe application is shutting down...");

        // Clean up MySQL connection cleanup thread first
        cleanupMySQLConnectionCleanupThread();

        // Unregister all JDBC drivers to prevent memory leaks
        unregisterJDBCDrivers();

        LoggerUtil.info(ApplicationLifecycleListener.class,
                "✅ Zero Star Cafe application shutdown completed successfully.");
    }

    /**
     * Shuts down the MySQL abandoned connection cleanup thread to prevent memory leaks.
     * This addresses the specific warning about mysql-cj-abandoned-connection-cleanup thread.
     */
    private void cleanupMySQLConnectionCleanupThread() {
        try {
            LoggerUtil.info(ApplicationLifecycleListener.class,
                    "🧹 Shutting down MySQL abandoned connection cleanup thread...");

            AbandonedConnectionCleanupThread.checkedShutdown();

            LoggerUtil.info(ApplicationLifecycleListener.class,
                    "✅ MySQL cleanup thread shutdown completed.");
        } catch (Exception e) {
            LoggerUtil.warn(ApplicationLifecycleListener.class,
                    "⚠️ Error while shutting down MySQL cleanup thread: " + e.getMessage());
        }
    }

    /**
     * Unregisters all JDBC drivers that were registered by this web application.
     * This prevents the "failed to unregister JDBC driver" warnings during shutdown.
     */
    private void unregisterJDBCDrivers() {
        ClassLoader webappClassLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<Driver> drivers = DriverManager.getDrivers();

        LoggerUtil.info(ApplicationLifecycleListener.class,
                "🧹 Unregistering JDBC drivers...");

        int unregisteredCount = 0;

        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();

            // Only unregister drivers loaded by this web application's class loader
            if (driver.getClass().getClassLoader() == webappClassLoader) {
                try {
                    LoggerUtil.info(ApplicationLifecycleListener.class,
                            "Unregistering JDBC driver: " + driver.getClass().getName());

                    DriverManager.deregisterDriver(driver);
                    unregisteredCount++;

                } catch (SQLException e) {
                    LoggerUtil.error(ApplicationLifecycleListener.class,
                            "❌ Failed to unregister JDBC driver: " + driver.getClass().getName(), e);
                }
            }
        }

        if (unregisteredCount > 0) {
            LoggerUtil.info(ApplicationLifecycleListener.class,
                    "✅ Successfully unregistered " + unregisteredCount + " JDBC driver(s).");
        } else {
            LoggerUtil.info(ApplicationLifecycleListener.class,
                    "ℹ️ No JDBC drivers to unregister for this web application.");
        }
    }
}
