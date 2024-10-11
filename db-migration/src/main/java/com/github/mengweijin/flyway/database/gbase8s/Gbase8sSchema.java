package com.github.mengweijin.flyway.database.gbase8s;

import org.flywaydb.core.internal.database.base.Schema;
import org.flywaydb.core.internal.database.base.Table;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;

import java.sql.SQLException;
import java.util.List;

/**
 * Gbase8s implementation of Schema.
 * @author mengweijin
 */
public class Gbase8sSchema extends Schema<Gbase8sDatabase, Gbase8sTable> {
    /**
     * Creates a new Gbase8s schema.
     *
     * @param jdbcTemplate The Jdbc Template for communicating with the DB.
     * @param database The database-specific support.
     * @param name The name of the schema.
     */
    Gbase8sSchema(JdbcTemplate jdbcTemplate, Gbase8sDatabase database, String name) {
        super(jdbcTemplate, database, name);
    }

    @Override
    protected boolean doExists() throws SQLException {
        return jdbcTemplate.queryForInt("SELECT COUNT(*) FROM systables where owner = ? and tabid > 99", name) > 0;
    }

    @Override
    protected boolean doEmpty() throws SQLException {
        return doAllTables().length == 0;
    }

    @Override
    protected void doCreate() {
    }

    @Override
    protected void doDrop() throws SQLException {
        clean();
    }

    @Override
    protected void doClean() throws SQLException {
        List<String> procedures = jdbcTemplate.queryForStringList("SELECT t.procname FROM \"gbasedbt\".sysprocedures AS t" +
                                                                          " WHERE t.owner=? AND t.mode='O' AND t.externalname IS NULL" +
                                                                          " AND t.procname NOT IN (" +
                                                                          // Exclude Gbase8s TimeSeries procs
                                                                          " 'tscontainerusage', 'tscontainertotalused', 'tscontainertotalpages'," +
                                                                          " 'tscontainernelems', 'tscontainerpctused', 'tsl_flushstatus', 'tsmakenullstamp'" +
                                                                          ")", name);
        for (String procedure : procedures) {
            jdbcTemplate.execute("DROP PROCEDURE " + procedure);
        }

        for (Table table : allTables()) {
            table.drop();
        }

        List<String> sequences = jdbcTemplate.queryForStringList("SELECT t.tabname FROM \"gbasedbt\".systables AS t" +
                                                                         " WHERE owner=? AND t.tabid > 99 AND t.tabtype='Q'" +
                                                                         " AND t.tabname NOT IN ('iot_data_seq')", name);
        for (String sequence : sequences) {
            jdbcTemplate.execute("DROP SEQUENCE " + sequence);
        }
    }

    private Gbase8sTable[] findTables(String sqlQuery, String... params) throws SQLException {
        List<String> tableNames = jdbcTemplate.queryForStringList(sqlQuery, params);
        Gbase8sTable[] tables = new Gbase8sTable[tableNames.size()];
        for (int i = 0; i < tableNames.size(); i++) {
            tables[i] = new Gbase8sTable(jdbcTemplate, database, this, tableNames.get(i));
        }
        return tables;
    }

    @Override
    protected Gbase8sTable[] doAllTables() throws SQLException {
        return findTables("SELECT t.tabname FROM \"gbasedbt\".systables AS t" +
                                  " WHERE owner=? AND t.tabid > 99 AND t.tabtype='T'" +
                                  " AND t.tabname NOT IN (" +
                                  // Exclude Gbase8s TimeSeries tables
                                  " 'calendarpatterns', 'calendartable'," +
                                  " 'tscontainertable', 'tscontainerwindowtable', 'tsinstancetable', " +
                                  " 'tscontainerusageactivewindowvti', 'tscontainerusagedormantwindowvti'" +
                                  ")", name);
    }

    @Override
    public Table getTable(String tableName) {
        return new Gbase8sTable(jdbcTemplate, database, this, tableName);
    }
}