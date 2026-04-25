package liquibase.database.core;

import liquibase.Scope;
import liquibase.database.DatabaseConnection;
import liquibase.database.OfflineConnection;
import liquibase.exception.DatabaseException;
import liquibase.executor.ExecutorService;
import liquibase.statement.core.RawSqlStatement;
import liquibase.structure.DatabaseObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mengweijin
 */
public class Gbase8sDatabase extends InformixDatabase {

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
        return "gbase8s";
    }

    @Override
    public boolean isCorrectDatabaseImplementation(final DatabaseConnection conn)
            throws DatabaseException {
        boolean correct = false;
        String name = conn.getDatabaseProductName();
        if (name != null && (name.equals(PRODUCT_NAME) || name.startsWith(PRODUCT_NAME_DB2JCC_PREFIX)) && "gbase".equalsIgnoreCase(this.getSqlMode(conn.getURL()))) {
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
    public String getSystemSchema() {
        return "gbasedbt";
    }

    @Override
    protected String getConnectionSchemaName() {
        if ((getConnection() == null) || (getConnection() instanceof OfflineConnection)) {
            return null;
        }
        try {
            // liquibase 4.27.0 + gbase8s:v8.8_3503x1_x64
            // String schemaName = Scope.getCurrentScope().getSingleton(ExecutorService.class).getExecutor("jdbc", this).queryForObject(new RawSqlStatement("select username from sysmaster:gbasedbt.syssessions where sid = dbinfo('sessionid')"), String.class);
            // liquibase 4.31.1 + gbase8s:v8.8_3633x21_csdk_x64 修复后的兼容SQL，替换原错误语法
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
    public String escapeObjectName(final String catalogName, final String schemaName, final String objectName, final Class<? extends DatabaseObject> objectType) {
        String name = super.escapeObjectName(catalogName, schemaName, objectName, objectType);
        if (name == null) {
            return null;
        }
        // informix uses : to separate catalog and schema. Like "catalog:schema.table"
        // liquibase 4.31.1 + gbase8s:v8.8_3633x21_csdk_x64 修复 schema 创建表时 :testdb.DATABASECHANGELOG
        if (name.startsWith(":")) {
            name = name.replaceFirst(":", "");
        }
        return name;
    }
}
