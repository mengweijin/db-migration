package com.github.mengweijin.liquibase.dameng.snapshot;

import com.github.mengweijin.liquibase.dameng.database.DmDatabase;
import liquibase.database.Database;
import liquibase.exception.DatabaseException;
import liquibase.snapshot.CachedRow;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.InvalidExampleException;
import liquibase.snapshot.SnapshotGenerator;
import liquibase.snapshot.jvm.TableSnapshotGenerator;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Schema;
import liquibase.structure.core.Table;

import java.sql.SQLException;
import java.util.List;

public class DmTableSnapshotGenerator extends TableSnapshotGenerator {

    @Override
    public int getPriority(Class<? extends DatabaseObject> objectType, Database database) {
        return database instanceof DmDatabase
                ? DmSnapshotGeneratorPriority.fromCore(this, objectType, super.getPriority(objectType, database))
                : PRIORITY_NONE;
    }

    @Override
    public Class<? extends SnapshotGenerator>[] replaces() {
        return new Class[]{TableSnapshotGenerator.class};
    }

    @Override
    protected DatabaseObject snapshotObject(DatabaseObject example, DatabaseSnapshot snapshot) throws DatabaseException {
        List<CachedRow> rows = DmSnapshotQueries.tables(snapshot, example.getSchema(), example.getName());
        if (rows.isEmpty()) {
            return null;
        }
        try {
            return readTable(rows.get(0), snapshot.getDatabase());
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    protected void addTo(DatabaseObject foundObject, DatabaseSnapshot snapshot)
            throws DatabaseException, InvalidExampleException {
        if (!(foundObject instanceof Schema) || !snapshot.getSnapshotControl().shouldInclude(Table.class)) {
            return;
        }
        Schema schema = (Schema) foundObject;
        for (CachedRow row : DmSnapshotQueries.tables(snapshot, schema, null)) {
            schema.addDatabaseObject(new Table().setName(row.getString("TABLE_NAME")).setSchema(schema));
        }
    }
}
