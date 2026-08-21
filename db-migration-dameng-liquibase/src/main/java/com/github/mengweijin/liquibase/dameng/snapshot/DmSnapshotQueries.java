package com.github.mengweijin.liquibase.dameng.snapshot;

import liquibase.Scope;
import liquibase.database.AbstractJdbcDatabase;
import liquibase.database.Database;
import liquibase.executor.ExecutorService;
import liquibase.exception.DatabaseException;
import liquibase.snapshot.CachedRow;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.statement.core.RawParameterizedSqlStatement;
import liquibase.structure.core.Schema;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

final class DmSnapshotQueries {

    private DmSnapshotQueries() {
    }

    static String schemaName(Database database, Schema schema) {
        return ((AbstractJdbcDatabase) database).getJdbcSchemaName(schema);
    }

    static List<CachedRow> columns(DatabaseSnapshot snapshot, Schema schema, String tableName, String columnName)
            throws DatabaseException {
        StringBuilder sql = new StringBuilder(
                "SELECT NULL AS TABLE_CAT, c.OWNER AS TABLE_SCHEM, 'NO' AS IS_AUTOINCREMENT, " +
                "cc.COMMENTS AS REMARKS, c.TABLE_NAME, c.COLUMN_NAME, c.DATA_TYPE AS DATA_TYPE_NAME, " +
                "DECODE(c.DATA_TYPE, 'CHAR', 1, 'VARCHAR2', 12, 'NUMBER', 3, 'LONG', -1, 'DATE', 93, " +
                "'RAW', -3, 'LONG RAW', -4, 'BLOB', 2004, 'CLOB', 2005, 'BFILE', -13, 'FLOAT', 6, " +
                "'TIMESTAMP(6)', 93, 'TIMESTAMP(6) WITH TIME ZONE', -101, " +
                "'TIMESTAMP(6) WITH LOCAL TIME ZONE', -102, 'INTERVAL YEAR(2) TO MONTH', -103, " +
                "'INTERVAL DAY(2) TO SECOND(6)', -104, 'BINARY_FLOAT', 100, 'BINARY_DOUBLE', 101, " +
                "'XMLTYPE', 2009, 1111) AS DATA_TYPE, " +
                "DECODE(c.CHAR_USED, 'C', c.CHAR_LENGTH, c.DATA_LENGTH) AS DATA_LENGTH, " +
                "c.DATA_PRECISION, c.DATA_SCALE, c.NULLABLE, c.COLUMN_ID AS ORDINAL_POSITION, " +
                "c.DATA_DEFAULT AS COLUMN_DEF, c.CHAR_LENGTH, c.CHAR_USED, c.VIRTUAL_COLUMN " +
                "FROM ALL_TAB_COLS c LEFT JOIN ALL_COL_COMMENTS cc " +
                "ON cc.OWNER=c.OWNER AND cc.TABLE_NAME=c.TABLE_NAME AND cc.COLUMN_NAME=c.COLUMN_NAME " +
                "WHERE c.OWNER=? AND c.HIDDEN_COLUMN='NO'");
        List<Object> parameters = new ArrayList<>();
        parameters.add(schemaName(snapshot.getDatabase(), schema));
        appendFilter(sql, parameters, "c.TABLE_NAME", tableName);
        appendFilter(sql, parameters, "c.COLUMN_NAME", columnName);
        sql.append(" ORDER BY c.TABLE_NAME, c.COLUMN_ID");
        return query(snapshot.getDatabase(), sql.toString(), parameters);
    }

    static List<CachedRow> tables(DatabaseSnapshot snapshot, Schema schema, String tableName) throws DatabaseException {
        StringBuilder sql = new StringBuilder(tablesSql());
        List<Object> parameters = new ArrayList<>();
        Database database = snapshot.getDatabase();
        String schemaName = schemaName(database, schema);
        parameters.add(schemaName);
        appendFilter(sql, parameters, "a.TABLE_NAME", tableName);
        sql.append(" ORDER BY a.TABLE_NAME");
        List<CachedRow> rows = query(database, sql.toString(), parameters);
        String defaultTablespace = defaultTablespace(database, schemaName);
        for (CachedRow row : rows) {
            String tablespace = row.getString("TABLESPACE_NAME");
            row.set("DEFAULT_TABLESPACE", tablespace != null && defaultTablespace != null
                    && tablespace.equalsIgnoreCase(defaultTablespace) ? "true" : null);
        }
        return rows;
    }

    static String tablesSql() {
        return "SELECT NULL AS TABLE_CAT, a.OWNER AS TABLE_SCHEM, a.TABLE_NAME, a.TEMPORARY, a.DURATION, " +
                "'TABLE' AS TABLE_TYPE, c.COMMENTS AS REMARKS, a.TABLESPACE_NAME " +
                "FROM ALL_TABLES a LEFT JOIN ALL_TAB_COMMENTS c " +
                "ON c.OWNER=a.OWNER AND c.TABLE_NAME=a.TABLE_NAME WHERE a.OWNER=?";
    }

    static String schemaOwnerDefaultTablespaceSql() {
        return "SELECT u.DEFAULT_TABLESPACE FROM SYSOBJECTS s JOIN DBA_USERS u ON u.USER_ID=s.PID " +
                "WHERE s.NAME=? AND s.TYPE$='SCH'";
    }

    private static String defaultTablespace(Database database, String schemaName) throws DatabaseException {
        String connectionUser = database.getConnection().getConnectionUserName();
        boolean currentUserSchema = equalsIgnoreCase(schemaName, connectionUser);
        if (currentUserSchema) {
            return firstValue(query(database, "SELECT DEFAULT_TABLESPACE FROM USER_USERS", List.of()),
                    "DEFAULT_TABLESPACE");
        }

        try {
            return firstValue(query(database, schemaOwnerDefaultTablespaceSql(), List.of(schemaName)),
                    "DEFAULT_TABLESPACE");
        } catch (DatabaseException e) {
            if (!isMissingCatalogPrivilege(e)) {
                throw e;
            }
            Scope.getCurrentScope().getLog(DmSnapshotQueries.class).fine(
                    "Unable to read the default tablespace for DM schema " + schemaName, e);
            return null;
        }
    }

    private static String firstValue(List<CachedRow> rows, String column) {
        return rows.isEmpty() ? null : rows.get(0).getString(column);
    }

    private static boolean equalsIgnoreCase(String left, String right) {
        return left != null && right != null && left.equalsIgnoreCase(right);
    }

    private static boolean isMissingCatalogPrivilege(Throwable error) {
        Throwable current = error;
        while (current != null) {
            if (current instanceof SQLException && ((SQLException) current).getErrorCode() == -5504) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }

    static List<CachedRow> views(DatabaseSnapshot snapshot, Schema schema, String viewName) throws DatabaseException {
        StringBuilder sql = new StringBuilder(
                "SELECT NULL AS TABLE_CAT, a.OWNER AS TABLE_SCHEM, a.VIEW_NAME AS TABLE_NAME, " +
                "'VIEW' AS TABLE_TYPE, c.COMMENTS AS REMARKS, a.TEXT AS OBJECT_BODY " +
                "FROM ALL_VIEWS a LEFT JOIN ALL_TAB_COMMENTS c " +
                "ON c.OWNER=a.OWNER AND c.TABLE_NAME=a.VIEW_NAME WHERE a.OWNER=?");
        List<Object> parameters = new ArrayList<>();
        parameters.add(schemaName(snapshot.getDatabase(), schema));
        appendFilter(sql, parameters, "a.VIEW_NAME", viewName);
        sql.append(" ORDER BY a.VIEW_NAME");
        return query(snapshot.getDatabase(), sql.toString(), parameters);
    }

    static List<CachedRow> indexes(DatabaseSnapshot snapshot, Schema schema, String tableName, String indexName)
            throws DatabaseException {
        StringBuilder sql = new StringBuilder(
                "SELECT NULL AS TABLE_CAT, c.TABLE_OWNER AS TABLE_SCHEM, c.TABLE_NAME, c.INDEX_NAME, " +
                DatabaseMetaData.tableIndexOther + " AS TYPE, c.COLUMN_NAME, c.COLUMN_POSITION AS ORDINAL_POSITION, " +
                "CASE i.UNIQUENESS WHEN 'UNIQUE' THEN 0 ELSE 1 END AS NON_UNIQUE, " +
                "CASE c.DESCEND WHEN 'Y' THEN 'D' WHEN 'DESC' THEN 'D' ELSE 'A' END AS ASC_OR_DESC, " +
                "NULL AS FILTER_CONDITION, i.TABLESPACE_NAME " +
                "FROM ALL_IND_COLUMNS c JOIN ALL_INDEXES i " +
                "ON i.OWNER=c.INDEX_OWNER AND i.INDEX_NAME=c.INDEX_NAME " +
                "AND i.TABLE_OWNER=c.TABLE_OWNER AND i.TABLE_NAME=c.TABLE_NAME WHERE c.TABLE_OWNER=?");
        List<Object> parameters = new ArrayList<>();
        parameters.add(schemaName(snapshot.getDatabase(), schema));
        appendFilter(sql, parameters, "c.TABLE_NAME", tableName);
        appendFilter(sql, parameters, "c.INDEX_NAME", indexName);
        sql.append(" ORDER BY c.INDEX_NAME, c.COLUMN_POSITION");
        return query(snapshot.getDatabase(), sql.toString(), parameters);
    }

    static List<CachedRow> primaryKeys(DatabaseSnapshot snapshot, Schema schema, String tableName)
            throws DatabaseException {
        StringBuilder sql = new StringBuilder(
                "SELECT NULL AS TABLE_CAT, c.OWNER AS TABLE_SCHEM, c.TABLE_NAME, c.COLUMN_NAME, " +
                "c.POSITION AS KEY_SEQ, c.CONSTRAINT_NAME AS PK_NAME, k.VALIDATED " +
                "FROM ALL_CONS_COLUMNS c JOIN ALL_CONSTRAINTS k " +
                "ON k.OWNER=c.OWNER AND k.TABLE_NAME=c.TABLE_NAME AND k.CONSTRAINT_NAME=c.CONSTRAINT_NAME " +
                "WHERE k.CONSTRAINT_TYPE='P' AND k.OWNER=?");
        List<Object> parameters = new ArrayList<>();
        parameters.add(schemaName(snapshot.getDatabase(), schema));
        appendFilter(sql, parameters, "k.TABLE_NAME", tableName);
        sql.append(" ORDER BY c.CONSTRAINT_NAME, c.POSITION");
        return query(snapshot.getDatabase(), sql.toString(), parameters);
    }

    static List<CachedRow> uniqueConstraints(DatabaseSnapshot snapshot, Schema schema, String tableName,
                                             String constraintName) throws DatabaseException {
        StringBuilder sql = new StringBuilder(
                "SELECT uc.OWNER AS CONSTRAINT_SCHEM, uc.CONSTRAINT_NAME, uc.TABLE_NAME, uc.STATUS, " +
                "uc.DEFERRABLE, uc.DEFERRED, ui.TABLESPACE_NAME, ui.INDEX_NAME, ui.OWNER AS INDEX_CATALOG, " +
                "uc.VALIDATED AS CONSTRAINT_VALIDATE, ac.COLUMN_NAME, ac.POSITION, " +
                "CASE ic.DESCEND WHEN 'Y' THEN 'D' WHEN 'DESC' THEN 'D' ELSE 'A' END AS ASC_OR_DESC " +
                "FROM ALL_CONSTRAINTS uc LEFT JOIN ALL_INDEXES ui " +
                "ON ui.OWNER=uc.OWNER AND ui.TABLE_OWNER=uc.OWNER AND ui.TABLE_NAME=uc.TABLE_NAME " +
                "AND ui.INDEX_NAME=uc.INDEX_NAME LEFT JOIN ALL_CONS_COLUMNS ac " +
                "ON ac.OWNER=uc.OWNER AND ac.TABLE_NAME=uc.TABLE_NAME AND ac.CONSTRAINT_NAME=uc.CONSTRAINT_NAME " +
                "LEFT JOIN ALL_IND_COLUMNS ic ON ic.INDEX_OWNER=ui.OWNER AND ic.INDEX_NAME=ui.INDEX_NAME " +
                "AND ic.COLUMN_NAME=ac.COLUMN_NAME WHERE uc.CONSTRAINT_TYPE='U' AND uc.OWNER=?");
        List<Object> parameters = new ArrayList<>();
        parameters.add(schemaName(snapshot.getDatabase(), schema));
        appendFilter(sql, parameters, "uc.TABLE_NAME", tableName);
        appendFilter(sql, parameters, "uc.CONSTRAINT_NAME", constraintName);
        sql.append(" ORDER BY uc.CONSTRAINT_NAME, ac.POSITION");
        return query(snapshot.getDatabase(), sql.toString(), parameters);
    }

    static List<CachedRow> foreignKeys(DatabaseSnapshot snapshot, Schema schema, String tableName, String fkName)
            throws DatabaseException {
        StringBuilder sql = new StringBuilder(
                "SELECT NULL AS PKTABLE_CAT, pk.OWNER AS PKTABLE_SCHEM, pk.TABLE_NAME AS PKTABLE_NAME, " +
                "pkc.COLUMN_NAME AS PKCOLUMN_NAME, NULL AS FKTABLE_CAT, fk.OWNER AS FKTABLE_SCHEM, " +
                "fk.TABLE_NAME AS FKTABLE_NAME, fkc.COLUMN_NAME AS FKCOLUMN_NAME, fkc.POSITION AS KEY_SEQ, " +
                DatabaseMetaData.importedKeyNoAction + " AS UPDATE_RULE, " +
                "CASE fk.DELETE_RULE WHEN 'CASCADE' THEN " + DatabaseMetaData.importedKeyCascade +
                " WHEN 'SET NULL' THEN " + DatabaseMetaData.importedKeySetNull +
                " ELSE " + DatabaseMetaData.importedKeyNoAction + " END AS DELETE_RULE, " +
                "fk.CONSTRAINT_NAME AS FK_NAME, pk.CONSTRAINT_NAME AS PK_NAME, " +
                "fk.DEFERRABLE AS FK_DEFERRABLE, fk.DEFERRED AS FK_DEFERRED, " +
                "fk.VALIDATED AS FK_VALIDATE " +
                "FROM ALL_CONSTRAINTS fk JOIN ALL_CONS_COLUMNS fkc " +
                "ON fkc.OWNER=fk.OWNER AND fkc.TABLE_NAME=fk.TABLE_NAME AND fkc.CONSTRAINT_NAME=fk.CONSTRAINT_NAME " +
                "JOIN ALL_CONSTRAINTS pk ON pk.OWNER=fk.R_OWNER AND pk.CONSTRAINT_NAME=fk.R_CONSTRAINT_NAME " +
                "JOIN ALL_CONS_COLUMNS pkc ON pkc.OWNER=pk.OWNER AND pkc.TABLE_NAME=pk.TABLE_NAME " +
                "AND pkc.CONSTRAINT_NAME=pk.CONSTRAINT_NAME AND pkc.POSITION=fkc.POSITION " +
                "WHERE fk.CONSTRAINT_TYPE='R' AND fk.OWNER=?");
        List<Object> parameters = new ArrayList<>();
        parameters.add(schemaName(snapshot.getDatabase(), schema));
        appendFilter(sql, parameters, "fk.TABLE_NAME", tableName);
        appendFilter(sql, parameters, "fk.CONSTRAINT_NAME", fkName);
        sql.append(" ORDER BY fk.CONSTRAINT_NAME, fkc.POSITION");
        return query(snapshot.getDatabase(), sql.toString(), parameters);
    }

    private static void appendFilter(StringBuilder sql, List<Object> parameters, String column, String value) {
        if (value != null) {
            sql.append(" AND ").append(column).append("=?");
            parameters.add(value);
        }
    }

    private static List<CachedRow> query(Database database, String sql, List<Object> parameters)
            throws DatabaseException {
        List<Map<String, ?>> results = Scope.getCurrentScope().getSingleton(ExecutorService.class)
                .getExecutor("jdbc", database)
                .queryForList(new RawParameterizedSqlStatement(sql, parameters.toArray()));
        List<CachedRow> rows = new ArrayList<>(results.size());
        for (Map<String, ?> result : results) {
            Map<String, Object> normalized = new LinkedHashMap<>();
            result.forEach((key, value) -> normalized.put(key.toUpperCase(Locale.US), value));
            rows.add(new CachedRow(normalized));
        }
        return rows;
    }
}
