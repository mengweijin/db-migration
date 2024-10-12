package com.github.mengweijin.flyway.database.dm;

import org.flywaydb.core.internal.callback.CallbackExecutor;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;
import org.flywaydb.core.internal.jdbc.StatementInterceptor;
import org.flywaydb.core.internal.sqlscript.DefaultSqlScriptExecutor;

public class DmSqlScriptExecutor extends DefaultSqlScriptExecutor {

    public DmSqlScriptExecutor(JdbcTemplate jdbcTemplate,
                               CallbackExecutor callbackExecutor,
                               boolean undo,
                               boolean batch,
                               boolean outputQueryResults,
                               StatementInterceptor statementInterceptor) {
        super(jdbcTemplate, callbackExecutor, undo, batch, outputQueryResults, statementInterceptor);
    }


}