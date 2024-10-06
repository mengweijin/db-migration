package com.github.mengweijin.flyway.database.gbase;

import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.api.ResourceProvider;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.api.logging.Log;
import org.flywaydb.core.api.logging.LogFactory;
import org.flywaydb.core.internal.callback.CallbackExecutor;
import org.flywaydb.core.internal.database.DatabaseType;
import org.flywaydb.core.internal.database.base.BaseDatabaseType;
import org.flywaydb.core.internal.database.base.Database;
import org.flywaydb.core.internal.jdbc.JdbcConnectionFactory;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;
import org.flywaydb.core.internal.jdbc.StatementInterceptor;
import org.flywaydb.core.internal.parser.Parser;
import org.flywaydb.core.internal.parser.ParsingContext;
import org.flywaydb.core.internal.sqlscript.SqlScriptExecutor;
import org.flywaydb.core.internal.sqlscript.SqlScriptExecutorFactory;
import org.flywaydb.core.internal.util.ClassUtils;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.Types;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * @author mengweijin
 * @since 2024/9/28
 */
public class GbaseDatabaseType extends BaseDatabaseType {
    private static final Log LOG = LogFactory.getLog(GbaseDatabaseType.class);
    // Oracle usernames/passwords can be 1-30 chars, can only contain alphanumerics and # _ $
    // The first (and only) capture group represents the password
    private static final Pattern usernamePasswordPattern = Pattern.compile("^jdbc:oracle:thin:[a-zA-Z0-9#_$]+/([a-zA-Z0-9#_$]+)@.*");

    @Override
    public String getName() {
        return "Oracle";
    }

    @Override
    public int getNullType() {
        return Types.VARCHAR;
    }

    @Override
    public boolean handlesJDBCUrl(String url) {
        if (url.startsWith("jdbc-secretsmanager:oracle:")) {


            throw new org.flywaydb.core.internal.license.FlywayTeamsUpgradeRequiredException("jdbc-secretsmanager");

        }
        return url.startsWith("jdbc:oracle") || url.startsWith("jdbc:p6spy:oracle");
    }

    @Override
    public Pattern getJDBCCredentialsPattern() {
        return usernamePasswordPattern;
    }

    @Override
    public String getDriverClass(String url, ClassLoader classLoader) {


        if (url.startsWith("jdbc:p6spy:oracle:")) {
            return "com.p6spy.engine.spy.P6SpyDriver";
        }
        return "oracle.jdbc.OracleDriver";
    }

    @Override
    public boolean handlesDatabaseProductNameAndVersion(String databaseProductName, String databaseProductVersion, Connection connection) {
        return databaseProductName.startsWith("Oracle");
    }

    @Override
    public Database createDatabase(Configuration configuration, JdbcConnectionFactory jdbcConnectionFactory, StatementInterceptor statementInterceptor) {
        GbaseDatabase.enableTnsnamesOraSupport();

        return new GbaseDatabase(configuration, jdbcConnectionFactory, statementInterceptor);
    }

    @Override
    public Parser createParser(Configuration configuration, ResourceProvider resourceProvider, ParsingContext parsingContext) {
        return new GbaseParser(configuration, parsingContext);
    }

    @Override
    public SqlScriptExecutorFactory createSqlScriptExecutorFactory(JdbcConnectionFactory jdbcConnectionFactory,
                                                                   final CallbackExecutor callbackExecutor,
                                                                   final StatementInterceptor statementInterceptor) {


        final DatabaseType thisRef = this;

        return new SqlScriptExecutorFactory() {
            @Override
            public SqlScriptExecutor createSqlScriptExecutor(Connection connection, boolean undo, boolean batch, boolean outputQueryResults) {
                return new GbaseSqlScriptExecutor(new JdbcTemplate(connection, thisRef), callbackExecutor, undo, batch, outputQueryResults, statementInterceptor);
            }
        };
    }

    @Override
    public void setDefaultConnectionProps(String url, Properties props, ClassLoader classLoader) {
        String osUser = System.getProperty("user.name");
        props.put("v$session.osuser", osUser.substring(0, Math.min(osUser.length(), 30)));
        props.put("v$session.program", APPLICATION_NAME);
        props.put("oracle.net.keepAlive", "true");

        String oobb = ClassUtils.getStaticFieldValue("oracle.jdbc.OracleConnection", "CONNECTION_PROPERTY_THIN_NET_DISABLE_OUT_OF_BAND_BREAK", classLoader);
        props.put(oobb, "true");
    }

    @Override
    public void setConfigConnectionProps(Configuration config, Properties props, ClassLoader classLoader) {


    }


    @Override
    public boolean detectUserRequiredByUrl(String url) {
        return !usernamePasswordPattern.matcher(url).matches();
    }

    @Override
    public boolean detectPasswordRequiredByUrl(String url) {
        return !usernamePasswordPattern.matcher(url).matches();
    }

    /**
     * Workaround until this issue gets fixed: https://github.com/aws/aws-secretsmanager-jdbc/issues/44
     */
    private void registerOracleDriver() {
        try {
            Class<Driver> driver = (Class<Driver>) getClass().getClassLoader().loadClass("oracle.jdbc.OracleDriver");
            DriverManager.registerDriver(driver.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            throw new FlywayException("Unable to register Oracle driver. AWS Secrets Manager may not work", e);
        }
    }


}
