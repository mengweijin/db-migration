package com.github.mengweijin.liquibase.gbase8s.snapshot;

import liquibase.Scope;
import liquibase.database.Database;
import liquibase.database.core.Gbase8sDatabase;
import liquibase.database.core.Gbase8sOracleDatabase;
import liquibase.exception.DatabaseException;
import liquibase.executor.ExecutorService;
import liquibase.snapshot.CachedRow;
import liquibase.snapshot.SnapshotGenerator;
import liquibase.snapshot.jvm.ColumnSnapshotGenerator;
import liquibase.snapshot.jvm.ColumnSnapshotGeneratorInformix;
import liquibase.statement.core.RawParameterizedSqlStatement;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Column;
import liquibase.structure.core.DataType;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Keeps the GBase-specific DATETIME qualifier workaround outside Liquibase core classes.
 */
public class Gbase8sColumnSnapshotGenerator extends ColumnSnapshotGeneratorInformix {

    private static final Map<Integer, String> QUALIFIERS = new HashMap<>();

    static {
        QUALIFIERS.put(0, "YEAR");
        QUALIFIERS.put(2, "MONTH");
        QUALIFIERS.put(4, "DAY");
        QUALIFIERS.put(6, "HOUR");
        QUALIFIERS.put(8, "MINUTE");
        QUALIFIERS.put(10, "SECOND");
        QUALIFIERS.put(11, "FRACTION(1)");
        QUALIFIERS.put(12, "FRACTION(2)");
        QUALIFIERS.put(13, "FRACTION(3)");
        QUALIFIERS.put(14, "FRACTION(4)");
        QUALIFIERS.put(15, "FRACTION(5)");
    }

    @Override
    public int getPriority(Class<? extends DatabaseObject> objectType, Database database) {
        if (!isGbase(database)) {
            return PRIORITY_NONE;
        }
        Class<? extends DatabaseObject>[] containers = addsTo();
        if (containers != null) {
            for (Class<? extends DatabaseObject> container : containers) {
                if (container.isAssignableFrom(objectType)) {
                    return PRIORITY_ADDITIONAL + 1;
                }
            }
        }
        int corePriority = super.getPriority(objectType, database);
        return corePriority == PRIORITY_NONE ? PRIORITY_NONE : corePriority + 1;
    }

    @Override
    public Class<? extends SnapshotGenerator>[] replaces() {
        return new Class[]{ColumnSnapshotGenerator.class, ColumnSnapshotGeneratorInformix.class};
    }

    @Override
    protected DataType readDataType(CachedRow row, Column column, Database database) throws DatabaseException {
        String typeName = row.getString("TYPE_NAME").toUpperCase(Locale.ROOT);
        if (!"DATETIME".equals(typeName) && !"INTERVAL".equals(typeName)) {
            return super.readDataType(row, column, database);
        }

        int encodedLength = queryEncodedLength(database, row);
        int firstCode = (encodedLength % 256) / 16;
        int lastCode = (encodedLength % 256) % 16;
        String first = QUALIFIERS.get(firstCode);
        String last = QUALIFIERS.get(lastCode);
        if (first == null || last == null) {
            throw new DatabaseException("Unknown GBase datetime qualifier encoding " + encodedLength
                    + " for column " + column);
        }
        DataType dataType = new DataType(typeName + " " + first + " TO " + last);
        dataType.setColumnSizeUnit(DataType.ColumnSizeUnit.BYTE);
        return dataType;
    }

    private int queryEncodedLength(Database database, CachedRow row) {
        Integer fallback = row.getInt("COLUMN_SIZE");
        try {
            String sql = "SELECT c.collength FROM syscolumns c JOIN systables t ON t.tabid=c.tabid "
                    + "WHERE lower(t.tabname)=lower(?) AND lower(c.colname)=lower(?)";
            Integer result = Scope.getCurrentScope().getSingleton(ExecutorService.class).getExecutor("jdbc", database)
                    .queryForObject(new RawParameterizedSqlStatement(sql, row.getString("TABLE_NAME"),
                            row.getString("COLUMN_NAME")), Integer.class);
            return result == null ? fallback : result;
        } catch (Exception e) {
            Scope.getCurrentScope().getLog(getClass()).fine("Could not query GBase collength", e);
            return fallback;
        }
    }

    private boolean isGbase(Database database) {
        return database instanceof Gbase8sDatabase || database instanceof Gbase8sOracleDatabase;
    }
}
