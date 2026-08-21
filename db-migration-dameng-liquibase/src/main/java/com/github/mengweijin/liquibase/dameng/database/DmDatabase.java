package com.github.mengweijin.liquibase.dameng.database;

import liquibase.GlobalConfiguration;
import liquibase.Scope;
import liquibase.database.DatabaseConnection;
import liquibase.database.OfflineConnection;
import liquibase.database.core.OracleDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.util.JdbcUtil;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Liquibase database extension for Dameng DM.
 */
public class DmDatabase extends OracleDatabase {

    public static final String PRODUCT_NAME = "DM DBMS";

    private Integer databaseMajorVersion;
    private Integer databaseMinorVersion;

    @Override
    public void setConnection(DatabaseConnection connection) {
        Connection jdbcConnection = null;
        if (!(connection instanceof OfflineConnection) && connection instanceof JdbcConnection) {
            try {
                jdbcConnection = ((JdbcConnection) connection).getWrappedConnection();
            } catch (Exception e) {
                throw new UnexpectedLiquibaseException(e);
            }
        }

        if (jdbcConnection != null) {
            initializeMetadata(jdbcConnection);
            configureDdlLockTimeout(jdbcConnection);
        }
        super.setConnection(connection);
    }

    private void initializeMetadata(Connection connection) {
        try {
            DatabaseMetaData metadata = connection.getMetaData();
            databaseMajorVersion = metadata.getDatabaseMajorVersion();
            databaseMinorVersion = metadata.getDatabaseMinorVersion();
        } catch (SQLException e) {
            Scope.getCurrentScope().getLog(getClass()).info("Could not determine DM database version: " + e.getMessage());
        }
    }

    private void configureDdlLockTimeout(Connection connection) {
        Integer timeout = GlobalConfiguration.DDL_LOCK_TIMEOUT.getCurrentValue();
        if (timeout == null) {
            return;
        }

        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute("ALTER SESSION SET DDL_LOCK_TIMEOUT=" + timeout);
        } catch (SQLException e) {
            Scope.getCurrentScope().getLog(getClass()).warning("Unable to set DM DDL_LOCK_TIMEOUT: " + e.getMessage(), e);
        } finally {
            JdbcUtil.closeStatement(statement);
        }
    }

    @Override
    public String getShortName() {
        return "dm";
    }

    @Override
    protected String getDefaultDatabaseProductName() {
        return PRODUCT_NAME;
    }

    @Override
    public Integer getDefaultPort() {
        return 5236;
    }

    @Override
    public int getDatabaseMajorVersion() throws DatabaseException {
        return databaseMajorVersion == null ? super.getDatabaseMajorVersion() : databaseMajorVersion;
    }

    @Override
    public int getDatabaseMinorVersion() throws DatabaseException {
        return databaseMinorVersion == null ? super.getDatabaseMinorVersion() : databaseMinorVersion;
    }

    @Override
    public boolean isCorrectDatabaseImplementation(DatabaseConnection connection) throws DatabaseException {
        return PRODUCT_NAME.equalsIgnoreCase(connection.getDatabaseProductName());
    }

    @Override
    public String getDefaultDriver(String url) {
        return url != null && url.startsWith("jdbc:dm") ? "dm.jdbc.driver.DmDriver" : null;
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
