package liquibase.database.core;

import com.github.mengweijin.util.ReflectUtils;
import liquibase.GlobalConfiguration;
import liquibase.Scope;
import liquibase.database.DatabaseConnection;
import liquibase.database.OfflineConnection;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.util.JdbcUtil;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Encapsulates Oracle database support.
 */
public class DmDatabase extends OracleDatabase {

    public static final String PRODUCT_NAME = "DM DBMS";

    private final Set<String> reservedWords = new HashSet<>();

    private Integer databaseMajorVersion;

    private Integer databaseMinorVersion;

    @Override
    public void setConnection(DatabaseConnection conn) {
        //noinspection HardCodedStringLiteral,HardCodedStringLiteral,HardCodedStringLiteral,HardCodedStringLiteral,
        // HardCodedStringLiteral
        reservedWords.addAll(Arrays.asList("GROUP", "USER", "SESSION", "PASSWORD", "RESOURCE", "START", "SIZE", "UID", "DESC", "ORDER")); //more reserved words not returned by driver

        Connection sqlConn = null;
        if (!(conn instanceof OfflineConnection)) {
            try {
                /*
                 * Don't try to call getWrappedConnection if the conn instance is
                 * is not a JdbcConnection. This happens for OfflineConnection.
                 * see https://liquibase.jira.com/browse/CORE-2192
                 */
                if (conn instanceof JdbcConnection) {
                    sqlConn = ((JdbcConnection) conn).getWrappedConnection();
                }
            } catch (Exception e) {
                throw new UnexpectedLiquibaseException(e);
            }

            if (sqlConn != null) {
                //tryProxySession(conn.getURL(), sqlConn);

                try {
                    //noinspection HardCodedStringLiteral
                    reservedWords.addAll(Arrays.asList(sqlConn.getMetaData().getSQLKeywords().toUpperCase().split(",\\s*")));
                } catch (SQLException e) {
                    //noinspection HardCodedStringLiteral
                    Scope.getCurrentScope().getLog(getClass()).info("Could get sql keywords on OracleDatabase: " + e.getMessage());
                    //can not get keywords. Continue on
                }
                //try {
                //    Method method = sqlConn.getClass().getMethod("setRemarksReporting", Boolean.TYPE);
                //    method.setAccessible(true);
                //    method.invoke(sqlConn, true);
                //} catch (Exception e) {
                //    //noinspection HardCodedStringLiteral
                //    Scope.getCurrentScope().getLog(getClass()).info("Could not set remarks reporting on OracleDatabase: " + e.getMessage());
                //    //cannot set it. That is OK
                //}

                CallableStatement statement = null;
                try {
                    //noinspection HardCodedStringLiteral
                    // statement = sqlConn.prepareCall("{call DBMS_UTILITY.DB_VERSION(?,?)}");
                    // statement.registerOutParameter(1, Types.VARCHAR);
                    // statement.registerOutParameter(2, Types.VARCHAR);
                    // statement.execute();
                    //
                    // String compatibleVersion = statement.getString(2);
                    // if (compatibleVersion != null) {
                    //     Matcher majorVersionMatcher = VERSION_PATTERN.matcher(compatibleVersion);
                    //     if (majorVersionMatcher.matches()) {
                    //         this.databaseMajorVersion = Integer.valueOf(majorVersionMatcher.group(1));
                    //         this.databaseMinorVersion = Integer.valueOf(majorVersionMatcher.group(2));
                    //     }
                    // }

                    DatabaseMetaData metaData = sqlConn.getMetaData();
                    this.databaseMajorVersion = metaData.getDatabaseMajorVersion();
                    this.databaseMinorVersion = metaData.getDatabaseMinorVersion();
                } catch (SQLException e) {
                    @SuppressWarnings("HardCodedStringLiteral") String message = "Cannot read from DBMS_UTILITY.DB_VERSION: " + e.getMessage();

                    //noinspection HardCodedStringLiteral
                    Scope.getCurrentScope().getLog(getClass()).info("Could not set check compatibility mode on OracleDatabase, assuming not running in any sort of compatibility mode: " + message);
                } finally {
                    //JdbcUtil.closeStatement(statement);
                }

                if (ReflectUtils.hasStaticField(GlobalConfiguration.class, "DDL_LOCK_TIMEOUT") && GlobalConfiguration.DDL_LOCK_TIMEOUT.getCurrentValue() != null) {
                    int timeoutValue = GlobalConfiguration.DDL_LOCK_TIMEOUT.getCurrentValue();
                    Scope.getCurrentScope().getLog(getClass()).fine("Setting DDL_LOCK_TIMEOUT value to " + timeoutValue);
                    String sql = "ALTER SESSION SET DDL_LOCK_TIMEOUT=" + timeoutValue;
                    PreparedStatement ddlLockTimeoutStatement = null;
                    try {
                        ddlLockTimeoutStatement = sqlConn.prepareStatement(sql);
                        ddlLockTimeoutStatement.execute();
                    } catch (SQLException sqle) {
                        Scope.getCurrentScope().getUI().sendErrorMessage("Unable to set the DDL_LOCK_TIMEOUT_VALUE: " + sqle.getMessage(), sqle);
                        Scope.getCurrentScope().getLog(getClass()).warning("Unable to set the DDL_LOCK_TIMEOUT_VALUE: " + sqle.getMessage(), sqle);
                    } finally {
                        JdbcUtil.closeStatement(ddlLockTimeoutStatement);
                    }
                }
            }
        }
        // super.setConnection(conn);
        super.setSuperConnection(conn);
    }

    @Override
    public String getShortName() {
        //noinspection HardCodedStringLiteral
        return "dm";
    }

    @Override
    protected String getDefaultDatabaseProductName() {
        //noinspection HardCodedStringLiteral
        return PRODUCT_NAME;
    }

    @Override
    public Integer getDefaultPort() {
        return 5236;
    }

    @Override
    public int getDatabaseMajorVersion() throws DatabaseException {
        if (databaseMajorVersion == null) {
            return super.getDatabaseMajorVersion();
        } else {
            return databaseMajorVersion;
        }
    }

    @Override
    public int getDatabaseMinorVersion() throws DatabaseException {
        if (databaseMinorVersion == null) {
            return super.getDatabaseMinorVersion();
        } else {
            return databaseMinorVersion;
        }
    }

    @Override
    public boolean isCorrectDatabaseImplementation(DatabaseConnection conn) throws DatabaseException {
        return PRODUCT_NAME.equalsIgnoreCase(conn.getDatabaseProductName());
    }

    @Override
    public String getDefaultDriver(String url) {
        //noinspection HardCodedStringLiteral
        if (url.startsWith("jdbc:dm")) {
            return "dm.jdbc.driver.DmDriver";
        }
        return null;
    }

    @Override
    public boolean supportsAutoIncrement() {
        return true;
    }

    @Override
    public int getIdentifierMaximumLength() {
        return LONG_IDENTIFIERS_LEGNTH;
    }
}
