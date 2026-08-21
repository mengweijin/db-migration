package com.github.mengweijin.liquibase.dameng.snapshot;

import com.github.mengweijin.liquibase.dameng.database.DmDatabase;
import liquibase.CatalogAndSchema;
import liquibase.database.AbstractJdbcDatabase;
import liquibase.database.Database;
import liquibase.diff.compare.DatabaseObjectComparatorFactory;
import liquibase.exception.DatabaseException;
import liquibase.snapshot.CachedRow;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.InvalidExampleException;
import liquibase.snapshot.SnapshotGenerator;
import liquibase.snapshot.jvm.ForeignKeySnapshotGenerator;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Column;
import liquibase.structure.core.ForeignKey;
import liquibase.structure.core.Index;
import liquibase.structure.core.Schema;
import liquibase.structure.core.Table;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DmForeignKeySnapshotGenerator extends ForeignKeySnapshotGenerator {

    @Override
    public int getPriority(Class<? extends DatabaseObject> objectType, Database database) {
        return database instanceof DmDatabase
                ? DmSnapshotGeneratorPriority.fromCore(this, objectType, super.getPriority(objectType, database))
                : PRIORITY_NONE;
    }

    @Override
    public Class<? extends SnapshotGenerator>[] replaces() {
        return new Class[]{ForeignKeySnapshotGenerator.class};
    }

    @Override
    protected void addTo(DatabaseObject foundObject, DatabaseSnapshot snapshot)
            throws DatabaseException, InvalidExampleException {
        if (!(foundObject instanceof Table) || !snapshot.getSnapshotControl().shouldInclude(ForeignKey.class)) {
            return;
        }
        Table table = (Table) foundObject;
        Set<String> seen = new HashSet<>();
        for (CachedRow row : DmSnapshotQueries.foreignKeys(snapshot, table.getSchema(), table.getName(), null)) {
            String name = row.getString("FK_NAME");
            if (seen.add(name)) {
                table.getOutgoingForeignKeys().add(new ForeignKey().setName(name).setForeignKeyTable(table));
            }
        }
    }

    @Override
    protected DatabaseObject snapshotObject(DatabaseObject example, DatabaseSnapshot snapshot)
            throws DatabaseException, InvalidExampleException {
        ForeignKey requested = (ForeignKey) example;
        Table requestedTable = requested.getForeignKeyTable();
        List<CachedRow> rows = DmSnapshotQueries.foreignKeys(snapshot, requestedTable.getSchema(),
                requestedTable.getName(), example.getName());
        ForeignKey result = null;
        for (CachedRow row : rows) {
            String name = cleanNameFromDatabase(row.getString("FK_NAME"), snapshot.getDatabase());
            if (example.getName() != null && !name.equalsIgnoreCase(example.getName())) {
                continue;
            }
            if (result == null) {
                result = createForeignKey(row, snapshot);
            }

            Column fkColumn = new Column(cleanNameFromDatabase(row.getString("FKCOLUMN_NAME"), snapshot.getDatabase()))
                    .setRelation(result.getForeignKeyTable());
            if (containsColumn(result, fkColumn, snapshot)) {
                continue;
            }
            Column pkColumn = new Column(cleanNameFromDatabase(row.getString("PKCOLUMN_NAME"), snapshot.getDatabase()))
                    .setRelation(result.getPrimaryKeyTable());
            result.addForeignKeyColumn(fkColumn);
            result.addPrimaryKeyColumn(pkColumn);
        }

        if (result != null) {
            Index backingIndex = new Index().setRelation(result.getForeignKeyTable());
            backingIndex.getColumns().addAll(result.getForeignKeyColumns());
            backingIndex.addAssociatedWith(Index.MARK_FOREIGN_KEY);
            result.setBackingIndex(backingIndex);
            if (snapshot.get(ForeignKey.class).contains(result)) {
                return null;
            }
        }
        return result;
    }

    private ForeignKey createForeignKey(CachedRow row, DatabaseSnapshot snapshot) throws DatabaseException {
        Database database = snapshot.getDatabase();
        ForeignKey foreignKey = new ForeignKey().setName(row.getString("FK_NAME"));
        CatalogAndSchema fkSchema = ((AbstractJdbcDatabase) database).getSchemaFromJdbcInfo(
                row.getString("FKTABLE_CAT"), row.getString("FKTABLE_SCHEM"));
        Table fkTable = new Table().setName(row.getString("FKTABLE_NAME"));
        fkTable.setSchema(new Schema(fkSchema.getCatalogName(), fkSchema.getSchemaName()));
        foreignKey.setForeignKeyTable(fkTable);

        CatalogAndSchema pkSchema = ((AbstractJdbcDatabase) database).getSchemaFromJdbcInfo(
                row.getString("PKTABLE_CAT"), row.getString("PKTABLE_SCHEM"));
        Table pkTable = (Table) new Table().setName(row.getString("PKTABLE_NAME"))
                .setSchema(new Schema(pkSchema.getCatalogName(), pkSchema.getSchemaName()));
        foreignKey.setPrimaryKeyTable(pkTable);
        foreignKey.setUpdateRule(convertToForeignKeyConstraintType(row.getInt("UPDATE_RULE"), database));
        foreignKey.setDeleteRule(convertToForeignKeyConstraintType(row.getInt("DELETE_RULE"), database));

        setDeferrability(foreignKey, row);
        foreignKey.setShouldValidate("VALIDATED".equalsIgnoreCase(row.getString("FK_VALIDATE")));
        return foreignKey;
    }

    static void setDeferrability(ForeignKey foreignKey, CachedRow row) {
        boolean deferrable = matchesCatalogValue(row.getString("FK_DEFERRABLE"), "DEFERRABLE");
        foreignKey.setDeferrable(deferrable);
        foreignKey.setInitiallyDeferred(deferrable
                && matchesCatalogValue(row.getString("FK_DEFERRED"), "DEFERRED"));
    }

    private static boolean matchesCatalogValue(String actual, String expected) {
        return actual != null && expected.equalsIgnoreCase(actual.trim());
    }

    private boolean containsColumn(ForeignKey foreignKey, Column candidate, DatabaseSnapshot snapshot) {
        for (Column existing : foreignKey.getForeignKeyColumns()) {
            if (DatabaseObjectComparatorFactory.getInstance().isSameObject(existing, candidate,
                    snapshot.getSchemaComparisons(), snapshot.getDatabase())) {
                return true;
            }
        }
        return false;
    }
}
