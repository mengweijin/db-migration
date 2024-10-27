package liquibase.database.core;

import liquibase.Scope;
import liquibase.database.DatabaseConnection;
import liquibase.database.OfflineConnection;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.executor.ExecutorService;
import liquibase.statement.core.RawSqlStatement;
import liquibase.structure.DatabaseObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mengweijin
 */
public class Gbase8sOracleDatabase extends InformixDatabase {

    private static final String PRODUCT_NAME = "GBase 8s Server";

    private static final String PRODUCT_NAME_DB2JCC_PREFIX = "IDS";

    @Override
    protected String getDefaultDatabaseProductName() {
        return "GBase 8s Server";
    }

    @Override
    public Integer getDefaultPort() {
        return 9088;
    }

    @Override
    public String getDefaultDriver(final String url) {
        if (url.startsWith("jdbc:gbasedbt-sqli")) {
            return "com.gbasedbt.jdbc.Driver";
        }
        return null;
    }

    @Override
    public String getShortName() {
        return "gbase8sOracle";
    }

    @Override
    public boolean isCorrectDatabaseImplementation(final DatabaseConnection conn)
            throws DatabaseException {
        boolean correct = false;
        String name = conn.getDatabaseProductName();
        if (name != null && (name.equals(PRODUCT_NAME) || name.startsWith(PRODUCT_NAME_DB2JCC_PREFIX)) && "oracle".equalsIgnoreCase(this.getSqlMode(conn.getURL()))) {
            correct = true;
        }
        return correct;
    }

    public String getSqlMode(String url) {
        String sqlModeValue = "gbase";
        String regex = "(?i)SQLMODE=([^;]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            sqlModeValue = matcher.group(1);
        }

        return sqlModeValue;
    }

    @Override
    public String escapeObjectName(final String catalogName, final String schemaName, final String objectName, final Class<? extends DatabaseObject> objectType) {
        String name = super.escapeObjectName(catalogName, schemaName, objectName, objectType);
        if (name == null) {
            return null;
        }
        if (name.matches(".*\\..*\\..*")) {
            name = name.replaceFirst("\\.", ".");
        }
        return name;
    }

    @Override
    public String getSystemSchema() {
        return null;
    }

    @Override
    protected String getConnectionSchemaName() {
        if ((getConnection() == null) || (getConnection() instanceof OfflineConnection)) {
            return null;
        }
        try {
            String schemaName = Scope.getCurrentScope().getSingleton(ExecutorService.class).getExecutor("jdbc", this).queryForObject(new RawSqlStatement("select username from sysmaster.syssessions where sid = dbinfo('sessionid')"), String.class);
            if (schemaName != null) {
                return schemaName.trim();
            }
        } catch (Exception e) {
            Scope.getCurrentScope().getLog(getClass()).info("Error getting connection schema", e);
        }
        return null;
    }

    @Override
    public boolean supportsSchemas() {
        return false;
    }

    @Override
    protected String getConnectionCatalogName() {
        if (this.getConnection() instanceof OfflineConnection) {
            try {
                return this.getConnection().getCatalog();
            } catch (DatabaseException var3) {
            }
        }

        if (!(this.getConnection() instanceof JdbcConnection)) {
            return this.defaultCatalogName;
        } else {
            try {
                String schemaName = Scope.getCurrentScope().getSingleton(ExecutorService.class).getExecutor("jdbc", this).queryForObject(new RawSqlStatement("select DBINFO('dbname') from dual;"), String.class);
                return schemaName != null ? schemaName.trim() : null;
            } catch (DatabaseException var2) {
                return null;
            }
        }
    }
}
