package com.github.mengweijin.liquibase.dameng.snapshot;

import com.github.mengweijin.liquibase.dameng.database.DmDatabase;
import liquibase.database.Database;
import liquibase.exception.DatabaseException;
import liquibase.snapshot.CachedRow;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.SnapshotGenerator;
import liquibase.snapshot.jvm.UniqueConstraintSnapshotGenerator;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Column;
import liquibase.structure.core.Index;
import liquibase.structure.core.Relation;
import liquibase.structure.core.Table;
import liquibase.structure.core.UniqueConstraint;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DmUniqueConstraintSnapshotGenerator extends UniqueConstraintSnapshotGenerator {

    @Override
    public int getPriority(Class<? extends DatabaseObject> objectType, Database database) {
        return database instanceof DmDatabase
                ? DmSnapshotGeneratorPriority.fromCore(this, objectType, super.getPriority(objectType, database))
                : PRIORITY_NONE;
    }

    @Override
    public Class<? extends SnapshotGenerator>[] replaces() {
        return new Class[]{UniqueConstraintSnapshotGenerator.class};
    }

    @Override
    protected DatabaseObject snapshotObject(DatabaseObject example, DatabaseSnapshot snapshot) throws DatabaseException {
        UniqueConstraint requested = (UniqueConstraint) example;
        Relation relation = requested.getRelation();
        List<CachedRow> rows = DmSnapshotQueries.uniqueConstraints(snapshot, relation.getSchema(), relation.getName(),
                example.getName());
        if (rows.isEmpty()) {
            return null;
        }
        CachedRow first = rows.get(0);
        UniqueConstraint result = new UniqueConstraint().setName(first.getString("CONSTRAINT_NAME"))
                .setRelation(relation);
        setBackingIndexIfPresent(result, first, relation);
        result.setShouldValidate("VALIDATED".equalsIgnoreCase(first.getString("CONSTRAINT_VALIDATE")));
        for (CachedRow row : rows) {
            String columnName = row.getString("COLUMN_NAME");
            if (columnName == null) {
                continue;
            }
            String direction = row.getString("ASC_OR_DESC");
            Boolean descending = "D".equals(direction) ? Boolean.TRUE : ("A".equals(direction) ? Boolean.FALSE : null);
            result.getColumns().add(new Column(columnName).setDescending(descending)
                    .setRelation(relation));
        }
        return result;
    }

    @Override
    protected void addTo(DatabaseObject foundObject, DatabaseSnapshot snapshot) throws DatabaseException {
        if (!(foundObject instanceof Table)
                || !snapshot.getSnapshotControl().shouldInclude(UniqueConstraint.class)) {
            return;
        }
        Table table = (Table) foundObject;
        Set<String> seen = new HashSet<>();
        List<CachedRow> rows = DmSnapshotQueries.uniqueConstraints(snapshot, table.getSchema(), table.getName(), null);
        for (CachedRow row : rows) {
            String name = row.getString("CONSTRAINT_NAME");
            if (seen.add(name)) {
                UniqueConstraint constraint = new UniqueConstraint().setName(name).setRelation(table);
                setBackingIndexIfPresent(constraint, row, table);
                table.getUniqueConstraints().add(constraint);
            }
        }
    }

    static void setBackingIndexIfPresent(UniqueConstraint constraint, CachedRow row, Relation relation) {
        String indexName = row.getString("INDEX_NAME");
        if (indexName == null || indexName.isBlank()) {
            return;
        }
        constraint.setBackingIndex(new Index(indexName, row.getString("INDEX_CATALOG"),
                row.getString("CONSTRAINT_SCHEM"), relation.getName()));
    }
}
