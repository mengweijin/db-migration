package com.github.mengweijin.liquibase.dameng.snapshot;

import com.github.mengweijin.liquibase.dameng.database.DmDatabase;
import liquibase.CatalogAndSchema;
import liquibase.database.AbstractJdbcDatabase;
import liquibase.database.Database;
import liquibase.exception.DatabaseException;
import liquibase.snapshot.CachedRow;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.InvalidExampleException;
import liquibase.snapshot.SnapshotGenerator;
import liquibase.snapshot.jvm.PrimaryKeySnapshotGenerator;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Column;
import liquibase.structure.core.Index;
import liquibase.structure.core.PrimaryKey;
import liquibase.structure.core.Schema;
import liquibase.structure.core.Table;

import java.util.List;

public class DmPrimaryKeySnapshotGenerator extends PrimaryKeySnapshotGenerator {

    @Override
    public int getPriority(Class<? extends DatabaseObject> objectType, Database database) {
        return database instanceof DmDatabase
                ? DmSnapshotGeneratorPriority.fromCore(this, objectType, super.getPriority(objectType, database))
                : PRIORITY_NONE;
    }

    @Override
    public Class<? extends SnapshotGenerator>[] replaces() {
        return new Class[]{PrimaryKeySnapshotGenerator.class};
    }

    @Override
    protected DatabaseObject snapshotObject(DatabaseObject example, DatabaseSnapshot snapshot)
            throws DatabaseException, InvalidExampleException {
        PrimaryKey requested = (PrimaryKey) example;
        String tableName = requested.getTable() == null ? null : requested.getTable().getName();
        if (tableName == null && example.getName() == null) {
            return null;
        }
        List<CachedRow> rows = DmSnapshotQueries.primaryKeys(snapshot, example.getSchema(), tableName);
        PrimaryKey result = null;
        for (CachedRow row : rows) {
            if (example.getName() != null && !example.getName().equalsIgnoreCase(row.getString("PK_NAME"))) {
                continue;
            }
            if (result == null) {
                result = new PrimaryKey().setName(row.getString("PK_NAME"));
                CatalogAndSchema tableSchema = ((AbstractJdbcDatabase) snapshot.getDatabase()).getSchemaFromJdbcInfo(
                        row.getString("TABLE_CAT"), row.getString("TABLE_SCHEM"));
                result.setTable((Table) new Table().setName(row.getString("TABLE_NAME"))
                        .setSchema(new Schema(tableSchema.getCatalogName(), tableSchema.getSchemaName())));
                result.setShouldValidate("VALIDATED".equalsIgnoreCase(row.getString("VALIDATED")));
            }
            int position = row.getShort("KEY_SEQ");
            result.addColumn(position - 1, new Column(row.getString("COLUMN_NAME")).setRelation(result.getTable()));
        }
        if (result != null) {
            Index backingIndex = new Index().setRelation(result.getTable());
            backingIndex.setColumns(result.getColumns());
            result.setBackingIndex(backingIndex);
        }
        return result;
    }

    @Override
    protected void addTo(DatabaseObject foundObject, DatabaseSnapshot snapshot) throws DatabaseException {
        if (!(foundObject instanceof Table) || !snapshot.getSnapshotControl().shouldInclude(PrimaryKey.class)) {
            return;
        }
        Table table = (Table) foundObject;
        List<CachedRow> rows = DmSnapshotQueries.primaryKeys(snapshot, table.getSchema(), table.getName());
        if (!rows.isEmpty()) {
            PrimaryKey primaryKey = new PrimaryKey().setName(rows.get(0).getString("PK_NAME")).setTable(table);
            if (!snapshot.getDatabase().isSystemObject(primaryKey)) {
                table.setPrimaryKey(primaryKey);
            }
        }
    }
}
