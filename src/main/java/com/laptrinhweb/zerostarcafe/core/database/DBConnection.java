package com.laptrinhweb.zerostarcafe.core.database;

import com.laptrinhweb.zerostarcafe.core.utils.LoggerUtil;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Description:
 * Utility class for retrieving database connections from the Tomcat-managed JNDI DataSource.
 *
 * <h2>Example Usage:</h2>
 * <pre>{@code
 * try (Connection conn = DBConnection.getConnection()) {
 *     ...
 * }
 * }</pre>
 * <p>
 * Notes:
 * - Requires MySQL driver inside $CATALINA_HOME/lib
 * - Requires DataSource defined in context.xml with name "jdbc/ZeroStarDB"
 *
 * @author Dang Van Trung
 * @version 1.0.1
 * @lastModified 16/11/2025
 * @since 1.0.0
 */
public final class DBConnection {

    /**
     * The JNDI (Java Naming and Directory Interface)
     * name of the configured DataSource in Tomcat context.xml.
     */
    private static final String JNDI_NAME = "java:comp/env/jdbc/ZeroStarDB";

    // “one-time” initialization checks
    // Make sure the JDBC driver is present in Tomcat's lib directory
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            LoggerUtil.info(DBConnection.class, "✅ MySQL JDBC driver loaded.");
        } catch (ClassNotFoundException e) {
            LoggerUtil.error(DBConnection.class,
                    "❌ MySQL JDBC driver not found. Add 'mysql-connector-j' to $CATALINA_HOME/lib.", e);
        }
    }

    // Prevent instantiation
    private DBConnection() {
    }

    /**
     * Retrieves a connection from the JNDI DataSource.
     *
     * @return a {@link Connection} from the Tomcat-managed connection pool
     * @throws SQLException if acquiring a connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup(JNDI_NAME);
            Connection connection = ds.getConnection();

            LoggerUtil.info(DBConnection.class,
                    "✅ DBConnection acquired from pool.");
            return connection;

        } catch (NamingException e) {
            LoggerUtil.error(DBConnection.class,
                    "❌ Failed to lookup DataSource: " + JNDI_NAME, e);
            throw new SQLException("DataSource lookup failed.", e);

        } catch (SQLException e) {
            LoggerUtil.error(DBConnection.class,
                    "❌ Failed to get connection from DataSource.", e);
            throw e;
        }
    }
}