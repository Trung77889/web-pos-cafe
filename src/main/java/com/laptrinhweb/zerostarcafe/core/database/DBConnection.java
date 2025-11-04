package com.laptrinhweb.zerostarcafe.core.database;

import com.laptrinhweb.zerostarcafe.core.utils.AppLoggerUtil;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Description: Utility class for getting database connections via the Tomcat JNDI DataSource.
 * <p>
 * This class retrieves the DataSource configured in Tomcat's {@code context.xml}
 * (usually declared as {@code <Resource name="jdbc/ZeroStarDB" ... />})
 * and provides a single static method {@link #getConnection()} to get pooled
 * connections instead of creating new ones for each request.
 * </p>
 *
 * <h2>Configuration Requirements</h2>
 * <ul>
 *   <li>The MySQL JDBC driver must be placed in <code>$CATALINA_HOME/lib</code>.</li>
 *   <li>The DataSource must be declared in <code>META-INF/context.xml</code>:</li>
 * </ul>
 *
 * <pre>{@code
 * <Resource name="jdbc/ZeroStarDB"
 *           auth="Container"
 *           type="javax.sql.DataSource"
 *           driverClassName="com.mysql.cj.jdbc.Driver"
 *           url="jdbc:mysql://localhost:3306/zerostar_cf"
 *           username="root"
 *           password=""
 *           ...
 * }</pre>
 *
 * <p>
 * When deployed, Tomcat automatically manages connection pooling through
 * Apache Commons DBCP 2. Calling {@link #getConnection()} will always return a
 * connection from that pool.
 * </p>
 *
 * <h2> Example Usage: Always use try-with-resources for JDBC code </h2>
 * <pre>{@code
 * try (Connection conn = DBConnection.getConnection()) {
 *     PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users");
 *     ResultSet rs = stmt.executeQuery();
 *     ...
 * }
 * }</pre>
 *
 * @author Dang Van Trung
 * @date 21/10/2025
 */
public class DBConnection {

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
            AppLoggerUtil.info(DBConnection.class, "✅ JDBC driver detected in classpath.");
        } catch (ClassNotFoundException e) {
            AppLoggerUtil.error(DBConnection.class,
                    "❌ JDBC driver not found! Please copy 'mysql-connector-j-8.x.x.jar' into $CATALINA_HOME/lib.", e);
        }
    }

    // Prevent instantiation
    private DBConnection() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated.");
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
            AppLoggerUtil.info(DBConnection.class, "✅ Database connection acquired from DataSource pool.");
            return connection;
        } catch (NamingException e) {
            AppLoggerUtil.error(DBConnection.class, "❌ Failed to lookup DataSource: " + JNDI_NAME, e);
            throw new SQLException("DataSource lookup failed.", e);
        } catch (SQLException e) {
            AppLoggerUtil.error(DBConnection.class, "❌ Failed to get connection from DataSource.", e);
            throw e;
        }
    }
}