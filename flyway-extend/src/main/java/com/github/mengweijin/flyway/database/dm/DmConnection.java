package com.github.mengweijin.flyway.database.dm;

import org.flywaydb.core.internal.database.base.Connection;
import org.flywaydb.core.internal.database.base.Schema;

import java.sql.SQLException;

/**
 * @author mengweijin
 */
public class DmConnection extends Connection<DmDatabase> {

    DmConnection(DmDatabase database, java.sql.Connection connection) {
        super(database, connection);
    }

    @Override
    protected String getCurrentSchemaNameOrSearchPath() throws SQLException {
        return this.jdbcTemplate.queryForString("SELECT SYS_CONTEXT('USERENV', 'CURRENT_SCHEMA') FROM DUAL");
    }

    @Override
    public void doChangeCurrentSchemaOrSearchPathTo(String schema) throws SQLException {
        this.jdbcTemplate.execute("ALTER SESSION SET CURRENT_SCHEMA=" + this.database.quote(schema));
    }

    @Override
    public Schema<DmDatabase, DmTable> getSchema(String name) {
        return new DmSchema(this.jdbcTemplate, this.database, name);
    }
}