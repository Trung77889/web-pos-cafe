package com.laptrinhweb.zerostarcafe.core.database;

import com.laptrinhweb.zerostarcafe.core.exception.AppException;
import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * <h2>Description:</h2>
 * <p>
 * DBAction helper for database operations.
 * Executes a block of database work using a single connection.
 * Commits on success and rolls back if any exception is thrown.
 * </p>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * {@code
 * try {
 *      return DBAction.run(
 *          conn -> {
 *              // Do some database work here.
 *          }
 *      );
 * } catch (AppException e) {
 *      // Error handling.
 * }
 * }
 * </pre>
 *
 * @author Dang Van Trung
 * @version 1.0.0
 * @lastModified 13/12/2025
 * @since 1.0.0
 */
public class DBAction {

    private DBAction() {
    }

    @FunctionalInterface
    public interface DBWork<T> {
        T execute(Connection conn) throws SQLException;
    }

    /**
     * Runs database work inside a single JDBC transaction.
     *
     * @param work database logic executed with the same connection
     * @param <T>  result type
     * @return result returned by the work
     */
    public static <T> T run(DBWork<T> work) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            T result = work.execute(conn);
            conn.commit();
            return result;

        } catch (SQLException e) {
            safeRollback(conn);
            throw new AppException("Database error: " + e.getMessage(), e);

        } catch (RuntimeException e) {
            safeRollback(conn);
            throw e; // NPE, IllegalArgument, AppException...

        } finally {
            safeClose(conn);
        }
    }

    private static void safeRollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                LoggerUtil.error(DBAction.class, "Rollback failed", ex);
            }
        }
    }

    private static void safeClose(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                LoggerUtil.error(DBAction.class, "Close conn failed", ex);
            }
        }
    }
}
