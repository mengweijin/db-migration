package com.github.mengweijin.flyway.database.gbase8s;

import org.flywaydb.core.internal.database.base.Connection;
import org.flywaydb.core.internal.database.base.Schema;

import java.sql.SQLException;

/**
 * Gbase8s connection.
 * @author mengweijin
 */
public class Gbase8sConnection extends Connection<Gbase8sDatabase> {
    Gbase8sConnection(Gbase8sDatabase database, java.sql.Connection connection) {
        super(database, connection);
    }

    @Override
    protected String getCurrentSchemaNameOrSearchPath() throws SQLException {
        return getJdbcConnection().getMetaData().getUserName();
    }

    @Override
    public Schema getSchema(String name) {
        return new Gbase8sSchema(jdbcTemplate, database, name);
    }

    @Override
    public void changeCurrentSchemaTo(Schema schema) {
        // Informix doesn't support schemas
    }
}