package com.github.mengweijin.liquibase.dameng.snapshot;

import com.github.mengweijin.liquibase.dameng.database.DmDatabase;
import liquibase.database.Database;
import liquibase.exception.DatabaseException;
import liquibase.snapshot.CachedRow;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.SnapshotGenerator;
import liquibase.snapshot.jvm.ColumnSnapshotGenerator;
import liquibase.snapshot.jvm.ColumnSnapshotGeneratorOracle;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Column;
import liquibase.structure.core.Relation;

import java.sql.SQLException;
import java.util.List;

public class DmColumnSnapshotGenerator extends ColumnSnapshotGeneratorOracle {

    private static final String COMPLETE_ATTRIBUTE = "dm-liquibase-complete";

    @Override
    public int getPriority(Class<? extends DatabaseObject> objectType, Database database) {
        return database instanceof DmDatabase
                ? DmSnapshotGeneratorPriority.fromCore(this, objectType, super.getPriority(objectType, database))
                : PRIORITY_NONE;
    }

    @Override
    public Class<? extends SnapshotGenerator>[] replaces() {
        return new Class[]{ColumnSnapshotGenerator.class, ColumnSnapshotGeneratorOracle.class};
    }

    @Override
    protected DatabaseObject snapshotObject(DatabaseObject example, DatabaseSnapshot snapshot) throws DatabaseException {
        Column exampleColumn = (Column) example;
        if (exampleColumn.getAttribute(COMPLETE_ATTRIBUTE, false)) {
            exampleColumn.setAttribute(COMPLETE_ATTRIBUTE, null);
            return exampleColumn;
        }
        Relation relation = exampleColumn.getRelation();
        List<CachedRow> rows = DmSnapshotQueries.columns(snapshot, relation.getSchema(), relation.getName(),
                exampleColumn.getName());
        if (rows.isEmpty()) {
            return null;
        }
        try {
            Column column = readColumn(rows.get(0), relation, snapshot.getDatabase());
            setAutoIncrementDetails(column, snapshot.getDatabase(), snapshot);
            return column;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    protected void addTo(DatabaseObject foundObject, DatabaseSnapshot snapshot) throws DatabaseException {
        if (!(foundObject instanceof Relation) || !snapshot.getSnapshotControl().shouldInclude(Column.class)) {
            return;
        }
        Relation relation = (Relation) foundObject;
        try {
            for (CachedRow row : DmSnapshotQueries.columns(snapshot, relation.getSchema(), relation.getName(), null)) {
                Column column = readColumn(row, relation, snapshot.getDatabase());
                setAutoIncrementDetails(column, snapshot.getDatabase(), snapshot);
                column.setAttribute(COMPLETE_ATTRIBUTE, true);
                relation.getColumns().add(column);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
