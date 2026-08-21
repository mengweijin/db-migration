package com.github.mengweijin.liquibase.dameng.snapshot;

import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.snapshot.CachedRow;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.JdbcDatabaseSnapshot;
import liquibase.snapshot.SnapshotControl;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Schema;
import liquibase.structure.core.Table;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class DmCrossSchemaTablespaceIT {

    @Test
    void tablesUseTheSchemaAuthorizationUsersDefaultTablespace() throws Exception {
        String url = requiredProperty("dm.jdbc.url");
        String username = requiredProperty("dm.jdbc.username");
        String password = requiredProperty("dm.jdbc.password");
        String crossSchema = System.getProperty("dm.cross.schema", "SYS");
        boolean expectDefault = Boolean.parseBoolean(System.getProperty("dm.cross.expect-default", "true"));

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            try {
                DatabaseSnapshot snapshot = new JdbcDatabaseSnapshot(new DatabaseObject[0], database,
                        new SnapshotControl(database, Table.class));
                List<CachedRow> crossSchemaTables = DmSnapshotQueries.tables(snapshot,
                        new Schema((String) null, crossSchema), null);

                assertFalse(crossSchemaTables.isEmpty());
                assertEquals(expectDefault, crossSchemaTables.stream()
                        .anyMatch(row -> "true".equalsIgnoreCase(row.getString("DEFAULT_TABLESPACE"))));
            } finally {
                database.close();
            }
        }
    }

    private String requiredProperty(String name) {
        String value = System.getProperty(name);
        if (value == null || value.isBlank() || value.startsWith("${")) {
            throw new IllegalStateException(name + " must be configured by the dm-integration Maven profile");
        }
        return value;
    }
}
