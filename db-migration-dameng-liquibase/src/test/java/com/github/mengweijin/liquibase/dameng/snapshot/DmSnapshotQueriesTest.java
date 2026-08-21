package com.github.mengweijin.liquibase.dameng.snapshot;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DmSnapshotQueriesTest {

    @Test
    void tablesResolveDefaultTablespaceFromSchemaAuthorizationUser() {
        String tablesSql = DmSnapshotQueries.tablesSql();
        String ownerSql = DmSnapshotQueries.schemaOwnerDefaultTablespaceSql();

        assertAll(
                () -> assertFalse(tablesSql.contains("USER_USERS")),
                () -> assertFalse(tablesSql.contains("SYSOBJECTS")),
                () -> assertFalse(tablesSql.contains("DBA_USERS")),
                () -> assertTrue(ownerSql.contains("SYSOBJECTS s JOIN DBA_USERS u ON u.USER_ID=s.PID")),
                () -> assertTrue(ownerSql.contains("s.NAME=? AND s.TYPE$='SCH'"))
        );
    }
}
