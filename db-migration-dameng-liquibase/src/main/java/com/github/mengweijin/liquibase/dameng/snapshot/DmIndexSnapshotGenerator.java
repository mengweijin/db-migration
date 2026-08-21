package com.github.mengweijin.liquibase.dameng.snapshot;

import com.github.mengweijin.liquibase.dameng.database.DmDatabase;
import liquibase.database.Database;
import liquibase.diff.compare.DatabaseObjectComparatorFactory;
import liquibase.exception.DatabaseException;
import liquibase.snapshot.CachedRow;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.InvalidExampleException;
import liquibase.snapshot.SnapshotGenerator;
import liquibase.snapshot.jvm.IndexSnapshotGenerator;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Column;
import liquibase.structure.core.ForeignKey;
import liquibase.structure.core.Index;
import liquibase.structure.core.Relation;
import liquibase.structure.core.Schema;
import liquibase.structure.core.Table;
import liquibase.structure.core.UniqueConstraint;
import liquibase.structure.core.View;

import java.sql.DatabaseMetaData;
import java.util.LinkedHashMap;
import java.util.Map;

public class DmIndexSnapshotGenerator extends IndexSnapshotGenerator {

    @Override
    public int getPriority(Class<? extends DatabaseObject> objectType, Database database) {
        return database instanceof DmDatabase
                ? DmSnapshotGeneratorPriority.fromCore(this, objectType, super.getPriority(objectType, database))
                : PRIORITY_NONE;
    }

    @Override
    public Class<? extends SnapshotGenerator>[] replaces() {
        return new Class[]{IndexSnapshotGenerator.class};
    }

    @Override
    protected void addTo(DatabaseObject foundObject, DatabaseSnapshot snapshot)
            throws DatabaseException, InvalidExampleException {
        if (foundObject instanceof Table || foundObject instanceof View) {
            Relation relation = (Relation) foundObject;
            for (Index index : readIndexes(snapshot, relation.getSchema(), relation.getName(), null).values()) {
                index.setRelation(relation);
                if (!containsEquivalent(relation, index, snapshot)) {
                    relation.getIndexes().add(index);
                }
            }
        } else if (foundObject instanceof UniqueConstraint
                && ((UniqueConstraint) foundObject).getBackingIndex() == null) {
            UniqueConstraint constraint = (UniqueConstraint) foundObject;
            Index index = new Index().setRelation(constraint.getRelation());
            index.getColumns().addAll(constraint.getColumns());
            constraint.setBackingIndex(index);
        } else if (foundObject instanceof ForeignKey && ((ForeignKey) foundObject).getBackingIndex() == null) {
            ForeignKey foreignKey = (ForeignKey) foundObject;
            Index index = new Index().setRelation(foreignKey.getForeignKeyTable());
            index.getColumns().addAll(foreignKey.getForeignKeyColumns());
            foreignKey.setBackingIndex(index);
        }
    }

    @Override
    protected DatabaseObject snapshotObject(DatabaseObject example, DatabaseSnapshot snapshot) throws DatabaseException {
        Index exampleIndex = (Index) example;
        Relation relation = exampleIndex.getRelation();
        Schema schema = relation == null ? new Schema(snapshot.getDatabase().getDefaultCatalogName(),
                snapshot.getDatabase().getDefaultSchemaName()) : relation.getSchema();
        String tableName = relation == null ? null : relation.getName();
        String indexName = exampleIndex.getName() == null ? null
                : snapshot.getDatabase().correctObjectName(exampleIndex.getName(), Index.class);
        Map<String, Index> indexes = readIndexes(snapshot, schema, tableName, indexName);

        if (indexName != null) {
            return indexes.get(indexName);
        }
        for (Index candidate : indexes.values()) {
            if (relation != null && DatabaseObjectComparatorFactory.getInstance().isSameObject(
                    candidate.getRelation(), relation, snapshot.getSchemaComparisons(), snapshot.getDatabase())
                    && candidate.getColumnNames().equalsIgnoreCase(exampleIndex.getColumnNames())) {
                return finalizeIndex(schema, tableName, candidate, snapshot);
            }
        }
        return null;
    }

    private Map<String, Index> readIndexes(DatabaseSnapshot snapshot, Schema schema, String tableName,
                                           String indexName) throws DatabaseException {
        Database database = snapshot.getDatabase();
        Map<String, Index> indexes = new LinkedHashMap<>();
        for (CachedRow row : DmSnapshotQueries.indexes(snapshot, schema, tableName, indexName)) {
            String name = cleanNameFromDatabase(row.getString("INDEX_NAME"), database);
            if (name == null) {
                continue;
            }
            Index index = indexes.computeIfAbsent(database.correctObjectName(name, Index.class), key -> {
                Relation indexedRelation = new Table().setName(row.getString("TABLE_NAME")).setSchema(schema);
                Index created = new Index().setName(name).setRelation(indexedRelation);
                Boolean nonUnique = row.getBoolean("NON_UNIQUE");
                created.setUnique(nonUnique != null && !nonUnique);
                created.setTablespace(row.getString("TABLESPACE_NAME"));
                Short type = row.getShort("TYPE");
                created.setClustered(type != null && type == DatabaseMetaData.tableIndexClustered);
                return created;
            });
            String direction = row.getString("ASC_OR_DESC");
            Boolean descending = "D".equals(direction) ? Boolean.TRUE : ("A".equals(direction) ? Boolean.FALSE : null);
            index.addColumn(new Column(row.getString("COLUMN_NAME")).setDescending(descending)
                    .setComputed(false).setRelation(index.getRelation()));
        }
        return indexes;
    }

    private boolean containsEquivalent(Relation relation, Index candidate, DatabaseSnapshot snapshot) {
        for (Index existing : relation.getIndexes()) {
            if (DatabaseObjectComparatorFactory.getInstance().isSameObject(
                    existing, candidate, snapshot.getSchemaComparisons(), snapshot.getDatabase())) {
                return true;
            }
        }
        return false;
    }
}
