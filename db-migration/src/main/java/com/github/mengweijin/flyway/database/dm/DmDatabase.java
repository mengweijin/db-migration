package com.github.mengweijin.flyway.database.dm;

import com.github.mengweijin.flyway.ISupportDatabase;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.extensibility.Tier;
import org.flywaydb.core.internal.database.base.Database;
import org.flywaydb.core.internal.database.base.Table;
import org.flywaydb.core.internal.jdbc.JdbcConnectionFactory;
import org.flywaydb.core.internal.jdbc.StatementInterceptor;
import org.flywaydb.core.internal.util.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author mengweijin
 */
public class DmDatabase extends Database<DmConnection> implements ISupportDatabase {
    private static final String DM_NET_TNS_ADMIN = "dm.net.tns_admin";

    /**
     * If the TNS_ADMIN environment variable is set, enable tnsnames.ora support for the Oracle JDBC driver.
     * See http://www.orafaq.com/wiki/TNS_ADMIN
     */
    public static void enableTnsnamesOraSupport() {
        String tnsAdminEnvVar = System.getenv("TNS_ADMIN");
        String tnsAdminSysProp = System.getProperty(DM_NET_TNS_ADMIN);
        if (StringUtils.hasLength(tnsAdminEnvVar) && tnsAdminSysProp == null) {
            System.setProperty(DM_NET_TNS_ADMIN, tnsAdminEnvVar);
        }
    }

    public DmDatabase(Configuration configuration, JdbcConnectionFactory jdbcConnectionFactory, StatementInterceptor statementInterceptor) {
        super(configuration, jdbcConnectionFactory, statementInterceptor);
    }

    @Override
    protected DmConnection doGetConnection(Connection connection) {
        return new DmConnection(this, connection);
    }

    @Override
    public boolean supportsChangingCurrentSchema() {
        return true;
    }

    /**
     * 兼容低版本
     * @param identifier identifier
     * @return String
     */
    @Override
    public String doQuote(String identifier) {
        return "\"" + identifier + "\"";
    }

    @Override
    public void ensureSupported() {
        //最小支持版本7
        ensureDatabaseIsRecentEnough("7");
        //最新支持版本8.1
        //ensureDatabaseNotOlderThanOtherwiseRecommendUpgradeToFlywayEdition("8.1", org.flywaydb.core.internal.license.Edition.ENTERPRISE);
        recommendFlywayUpgradeIfNecessary("8.1");
    }

    @Override
    public void ensureSupported(Configuration configuration) {
        ensureDatabaseIsRecentEnough("7");
        ensureDatabaseNotOlderThanOtherwiseRecommendUpgradeToFlywayEdition("8.1", Tier.PREMIUM, configuration);
        recommendFlywayUpgradeIfNecessaryForMajorVersion("8.1");
    }

    @Override
    public String getRawCreateScript(Table table, boolean baseline) {
        String tablespace = configuration.getTablespace() == null
                ? ""
                : " TABLESPACE \"" + configuration.getTablespace() + "\"";

        return "CREATE TABLE " + table + " (\n" +
                "    \"installed_rank\" INT NOT NULL,\n" +
                "    \"version\" VARCHAR2(50),\n" +
                "    \"description\" VARCHAR2(200) NOT NULL,\n" +
                "    \"type\" VARCHAR2(20) NOT NULL,\n" +
                "    \"script\" VARCHAR2(1000) NOT NULL,\n" +
                "    \"checksum\" INT,\n" +
                "    \"installed_by\" VARCHAR2(100) NOT NULL,\n" +
                "    \"installed_on\" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,\n" +
                "    \"execution_time\" INT NOT NULL,\n" +
                "    \"success\" NUMBER(1) NOT NULL,\n" +
                "    CONSTRAINT \"" + table.getName() + "_pk\" PRIMARY KEY (\"installed_rank\")\n" +
                ")" + tablespace + ";\n" +
                (baseline ? getBaselineStatement(table) + ";\n" : "") +
                "CREATE INDEX \"" + table.getSchema().getName() + "\".\"" + table.getName() + "_s_idx\" ON " + table + " (\"success\");\n";
    }

    @Override
    public boolean supportsEmptyMigrationDescription() {
        // DM will convert the empty string to NULL implicitly, and throw an exception as the column is NOT NULL
        return false;
    }

    @Override
    protected String doGetCatalog() throws SQLException {
        // Dm's JDBC driver returns a hard-coded NULL from getCatalog()
        // return getMainConnection().getJdbcTemplate().queryForString("SELECT GLOBAL_NAME FROM GLOBAL_NAME");
        return super.doGetCatalog();
    }

    @Override
    protected String doGetCurrentUser() throws SQLException {
        return getMainConnection().getJdbcTemplate().queryForString("SELECT USER FROM DUAL");
    }

    @Override
    public boolean supportsDdlTransactions() {
        return false;
    }

    @Override
    public String getBooleanTrue() {
        return "1";
    }

    @Override
    public String getBooleanFalse() {
        return "0";
    }

    @Override
    public boolean catalogIsSchema() {
        return false;
    }

    /**
     * Checks whether the specified query returns rows or not. Wraps the query in EXISTS() SQL function and executes it.
     * This is more preferable to opening a cursor for the original query, because a query inside EXISTS() is implicitly
     * optimized to return the first row and because the client never fetches more than 1 row despite the fetch size
     * value.
     *
     * @param query The query to check.
     * @param params The query parameters.
     * @return {@code true} if the query returns rows, {@code false} if not.
     * @throws SQLException when the query execution failed.
     */
    boolean queryReturnsRows(String query, String... params) throws SQLException {
        return getMainConnection().getJdbcTemplate().queryForBoolean("SELECT CASE WHEN EXISTS(" + query + ") THEN 1 ELSE 0 END FROM DUAL", params);
    }

    /**
     * Checks whether the specified privilege or role is granted to the current user.
     *
     * @return {@code true} if it is granted, {@code false} if not.
     * @throws SQLException if the check failed.
     */
    boolean isPrivOrRoleGranted(String name) throws SQLException {
        return queryReturnsRows("SELECT 1 FROM SESSION_PRIVS WHERE PRIVILEGE = ? UNION ALL " +
                                        "SELECT 1 FROM SESSION_ROLES WHERE ROLE = ?", name, name);
    }

    /**
     * Checks whether the specified data dictionary view in the specified system schema is accessible (directly or
     * through a role) or not.
     *
     * @param owner the schema name, unquoted case-sensitive.
     * @param name the data dictionary view name to check, unquoted case-sensitive.
     * @return {@code true} if it is accessible, {@code false} if not.
     * @throws SQLException if the check failed.
     */
    private boolean isDataDictViewAccessible(String owner, String name) throws SQLException {
        // return queryReturnsRows("SELECT * FROM ALL_TAB_PRIVS WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ? AND PRIVILEGE = 'SELECT'", owner, name);
        return queryReturnsRows("SELECT * FROM ALL_TAB_PRIVS WHERE OWNER = ? AND TABLE_NAME = ? AND PRIVILEGE = 'SELECT'", owner, name);
    }

    /**
     * Checks whether the specified SYS view is accessible (directly or through a role) or not.
     *
     * @param name the data dictionary view name to check, unquoted case-sensitive.
     * @return {@code true} if it is accessible, {@code false} if not.
     * @throws SQLException if the check failed.
     */
    boolean isDataDictViewAccessible(String name) throws SQLException {
        return isDataDictViewAccessible("SYS", name);
    }

    /**
     * Returns the specified data dictionary view name prefixed with DBA_ or ALL_ depending on its accessibility.
     *
     * @param baseName the data dictionary view base name, unquoted case-sensitive, e.g. OBJECTS, TABLES.
     * @return the full name of the view with the proper prefix.
     * @throws SQLException if the check failed.
     */
    String dbaOrAll(String baseName) throws SQLException {
        return isPrivOrRoleGranted("SELECT ANY DICTIONARY") || isDataDictViewAccessible("DBA_" + baseName)
                ? "DBA_" + baseName
                : "ALL_" + baseName;
    }

    /**
     * Returns the set of Oracle options available on the target database.
     *
     * @return the set of option titles.
     * @throws SQLException if retrieving of options failed.
     */
    private Set<String> getAvailableOptions() throws SQLException {
        // return new HashSet<>(getMainConnection().getJdbcTemplate().queryForStringList("SELECT PARAMETER FROM V$OPTION WHERE VALUE = 'TRUE'"));
        return new HashSet<>(getMainConnection().getJdbcTemplate().queryForStringList("SELECT PARA_NAME FROM V$OPTION WHERE PARA_VALUE = 'TRUE'"));
    }

    /**
     * Checks whether Flashback Data Archive option is available or not.
     *
     * @return {@code true} if it is available, {@code false} if not.
     * @throws SQLException when checking availability of the feature failed.
     */
    boolean isFlashbackDataArchiveAvailable() throws SQLException {
        return getAvailableOptions().contains("Flashback Data Archive");
    }

    /**
     * Checks whether XDB component is available or not.
     *
     * @return {@code true} if it is available, {@code false} if not.
     * @throws SQLException when checking availability of the component failed.
     */
    boolean isXmlDbAvailable() throws SQLException {
        return isDataDictViewAccessible("ALL_XML_TABLES");
    }

    /**
     * Checks whether Data Mining option is available or not.
     *
     * @return {@code true} if it is available, {@code false} if not.
     * @throws SQLException when checking availability of the feature failed.
     */
    boolean isDataMiningAvailable() throws SQLException {
        return getAvailableOptions().contains("Data Mining");
    }

    /**
     * Checks whether DM Locator component is available or not.
     *
     * @return {@code true} if it is available, {@code false} if not.
     * @throws SQLException when checking availability of the component failed.
     */
    boolean isLocatorAvailable() throws SQLException {
        return isDataDictViewAccessible("MDSYS", "ALL_SDO_GEOM_METADATA");
    }

    /**
     * Returns the list of schemas that were created and are maintained by Oracle-supplied scripts and must not be
     * changed in any other way. The list is composed of default schemas mentioned in the official documentation for
     * Oracle Database versions from 10.1 to 12.2, and is dynamically extended with schemas from DBA_REGISTRY and
     * ALL_USERS (marked with ORACLE_MAINTAINED = 'Y' in Oracle 12c).
     *
     * @return the set of system schema names
     */
    Set<String> getSystemSchemas() throws SQLException {

        // The list of known default system schemas
        Set<String> result = new HashSet<>(Arrays.asList(
                "SYS", "SYSTEM", // Standard system accounts
                "SYSBACKUP", "SYSDG", "SYSKM", "SYSRAC", "SYS$UMF", // Auxiliary system accounts
                "DBSNMP", "MGMT_VIEW", "SYSMAN", // Enterprise Manager accounts
                "OUTLN", // Stored outlines
                "AUDSYS", // Unified auditing
                "ORACLE_OCM", // DM Configuration Manager
                "APPQOSSYS", // DM Database QoS Management
                "OJVMSYS", // DM JavaVM
                "DVF", "DVSYS", // DM Database Vault
                "DBSFWUSER", // Database Service Firewall
                "REMOTE_SCHEDULER_AGENT", // Remote scheduler agent
                "DIP", // DM Directory Integration Platform
                "APEX_PUBLIC_USER", "FLOWS_FILES", /*"APEX_######", "FLOWS_######",*/ // DM Application Express
                "ANONYMOUS", "XDB", "XS$NULL", // DM XML Database
                "CTXSYS", // DM Text
                "LBACSYS", // DM Label Security
                "EXFSYS", // DM Rules Manager and Expression Filter
                "MDDATA", "MDSYS", "SPATIAL_CSW_ADMIN_USR", "SPATIAL_WFS_ADMIN_USR", // DM Locator and Spatial
                "ORDDATA", "ORDPLUGINS", "ORDSYS", "SI_INFORMTN_SCHEMA", // DM Multimedia
                "WMSYS", // DM Workspace Manager
                "OLAPSYS", // DM OLAP catalogs
                "OWBSYS", "OWBSYS_AUDIT", // DM Warehouse Builder
                "GSMADMIN_INTERNAL", "GSMCATUSER", "GSMUSER", // Global Data Services
                "GGSYS", // Oracle GoldenGate
                "WK_TEST", "WKSYS", "WKPROXY", // DM Ultra Search
                "ODM", "ODM_MTR", "DMSYS", // DM Data Mining
                "TSMSYS" // Transparent Session Migration
                                                        ));

        // APEX has a schema with a different name for each version, so get it from ALL_USERS. In addition, starting
        // from Oracle 12.1, there is a special column in ALL_USERS that marks Oracle-maintained schemas.
        //boolean oracle12cOrHigher = getVersion().isAtLeast("12");
        result.addAll(getMainConnection().getJdbcTemplate().queryForStringList("SELECT USERNAME FROM ALL_USERS " +
                                                                                       "WHERE REGEXP_LIKE(USERNAME, '^(APEX|FLOWS)_\\d+$')" +



                                                                                               " OR DM_MAINTAINED = 'Y'"



                                                                              ));

        // For earlier Oracle versions check also DBA_REGISTRY if possible.
        // if (!oracle12cOrHigher && isDataDictViewAccessible("DBA_REGISTRY")) {
        //     List<List<String>> schemaSuperList = getMainConnection().getJdbcTemplate().query(
        //             "SELECT SCHEMA, OTHER_SCHEMAS FROM DBA_REGISTRY",
        //             new RowMapper<List<String>>() {
        //                 @Override
        //                 public List<String> mapRow(ResultSet rs) throws SQLException {
        //                     List<String> schemaList = new ArrayList<>();
        //                     schemaList.add(rs.getString("SCHEMA"));
        //                     String otherSchemas = rs.getString("OTHER_SCHEMAS");
        //                     if (otherSchemas != null && !otherSchemas.trim().isEmpty()) {
        //                         schemaList.addAll(Arrays.asList(otherSchemas.trim().split("\\s*,\\s*")));
        //                     }
        //                     return schemaList;
        //                 }
        //             });
        //     for (List<String> schemaList : schemaSuperList) {
        //         result.addAll(schemaList);
        //     }
        // }

        return result;
    }

    /**
     * Fixed Github issue 17：当有 initSql 时，initSql会被执行2次
     * <a href="https://github.com/mengweijin/db-migration/issues/17">https://github.com/mengweijin/db-migration/issues/17</a>
     */
    @Override
    public boolean useSingleConnection() {
        return true;
    }
}