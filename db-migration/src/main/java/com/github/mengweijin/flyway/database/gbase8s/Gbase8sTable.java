package com.github.mengweijin.flyway.database.gbase8s;

import org.flywaydb.core.internal.database.base.Table;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;

import java.sql.SQLException;

/**
 * Gbase8s-specific table.
 * @author mengweijin
 */
public class Gbase8sTable extends Table<Gbase8sDatabase, Gbase8sSchema> {
    /**
     * Creates a new Gbase8s table.
     *
     * @param jdbcTemplate The Jdbc Template for communicating with the DB.
     * @param database The database-specific support.
     * @param schema The schema this table lives in.
     * @param name The name of the table.
     */
    Gbase8sTable(JdbcTemplate jdbcTemplate, Gbase8sDatabase database, Gbase8sSchema schema, String name) {
        super(jdbcTemplate, database, schema, name);
    }

    @Override
    protected void doDrop() throws SQLException {
        jdbcTemplate.execute("DROP TABLE " + name);
    }

    @Override
    protected boolean doExists() throws SQLException {
        return exists(null, schema, name);
    }

    @Override
    protected void doLock() throws SQLException {
        jdbcTemplate.update("lock table " + this + " in exclusive mode");
    }

    @Override
    public String toString() {
        return name;
    }
}