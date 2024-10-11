package com.github.mengweijin.flyway.database.dm;

import org.flywaydb.core.internal.database.base.Table;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;

import java.sql.SQLException;

/**
 * DM-specific table.
 * @author mengweijin
 */
public class DmTable extends Table<DmDatabase, DmSchema> {
    /**
     * Creates a new DM table.
     *
     * @param jdbcTemplate The Jdbc Template for communicating with the DB.
     * @param database The database-specific support.
     * @param schema The schema this table lives in.
     * @param name The name of the table.
     */
    public DmTable(JdbcTemplate jdbcTemplate, DmDatabase database, DmSchema schema, String name) {
        super(jdbcTemplate, database, schema, name);
    }

    @Override
    protected void doDrop() throws SQLException {
        jdbcTemplate.execute("DROP TABLE " + database.quote(schema.getName(), name) + " CASCADE CONSTRAINTS PURGE");
    }

    @Override
    protected boolean doExists() throws SQLException {
        return exists(null, schema, name);
    }

    @Override
    protected void doLock() throws SQLException {
        jdbcTemplate.execute("LOCK TABLE " + this + " IN EXCLUSIVE MODE");
    }
}