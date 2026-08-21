package com.github.mengweijin.liquibase.dameng;

import com.github.mengweijin.liquibase.dameng.snapshot.DmPrimaryKeySnapshotGenerator;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.Scope;
import liquibase.changelog.ChangeLogHistoryServiceFactory;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.diff.DiffGeneratorFactory;
import liquibase.diff.DiffResult;
import liquibase.diff.compare.CompareControl;
import liquibase.executor.ExecutorService;
import liquibase.lockservice.LockService;
import liquibase.lockservice.LockServiceFactory;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.SnapshotControl;
import liquibase.snapshot.SnapshotGeneratorFactory;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.ForeignKey;
import liquibase.structure.core.Index;
import liquibase.structure.core.PrimaryKey;
import liquibase.structure.core.Table;
import liquibase.structure.core.UniqueConstraint;
import liquibase.structure.core.View;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DmLiquibaseIT {

    @Test
    void updateStatusRollbackReupdateLockSnapshotAndDiff() throws Exception {
        String url = requiredProperty("dm.jdbc.url");
        String username = requiredProperty("dm.jdbc.username");
        String password = requiredProperty("dm.jdbc.password");

        ensureClean(url, username, password);
        resetLiquibaseServices();
        updateStatusAndRollback(url, username, password);
        resetLiquibaseServices();
        reupdateLockSnapshotAndDiff(url, username, password);
    }

    private void ensureClean(String url, String username, String password) throws Exception {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            try (ClassLoaderResourceAccessor accessor = new ClassLoaderResourceAccessor();
                 Liquibase liquibase = new Liquibase("dm-integration-changelog.xml", accessor, database)) {
                Contexts contexts = new Contexts();
                LabelExpression labels = new LabelExpression();
                int totalChangeSets = liquibase.getDatabaseChangeLog().getChangeSets().size();
                int appliedChangeSets = totalChangeSets - liquibase.listUnrunChangeSets(contexts, labels).size();
                if (appliedChangeSets > 0) {
                    liquibase.rollback(appliedChangeSets, contexts, labels);
                }
            }
        }
    }

    private void resetLiquibaseServices() {
        ChangeLogHistoryServiceFactory.getInstance().resetAll();
        LockServiceFactory.getInstance().resetAll();
        Scope.getCurrentScope().getSingleton(ExecutorService.class).reset();
    }

    private void updateStatusAndRollback(String url, String username, String password) throws Exception {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            assertEquals("dm", database.getShortName());

            try (ClassLoaderResourceAccessor accessor = new ClassLoaderResourceAccessor();
                 Liquibase liquibase = new Liquibase("dm-integration-changelog.xml", accessor, database)) {
                Contexts contexts = new Contexts();
                LabelExpression labels = new LabelExpression();
                int totalChangeSets = liquibase.getDatabaseChangeLog().getChangeSets().size();

                liquibase.update(contexts, labels);
                assertTrue(liquibase.listUnrunChangeSets(contexts, labels).isEmpty());

                liquibase.rollback(totalChangeSets, contexts, labels);
                assertEquals(totalChangeSets, liquibase.listUnrunChangeSets(contexts, labels).size());
            }
        }
    }

    private void reupdateLockSnapshotAndDiff(String url, String username, String password) throws Exception {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            try (ClassLoaderResourceAccessor accessor = new ClassLoaderResourceAccessor();
                 Liquibase liquibase = new Liquibase("dm-integration-changelog.xml", accessor, database)) {
                Contexts contexts = new Contexts();
                LabelExpression labels = new LabelExpression();
                liquibase.update(contexts, labels);
                assertTrue(liquibase.listUnrunChangeSets(contexts, labels).isEmpty());

                LockService lockService = LockServiceFactory.getInstance().getLockService(database);
                lockService.init();
                lockService.forceReleaseLock();
                assertTrue(lockService.acquireLock());
                assertTrue(lockService.hasChangeLogLock());
                lockService.releaseLock();
                assertFalse(lockService.hasChangeLogLock());

                SnapshotControl control = new SnapshotControl(database);
                DatabaseSnapshot snapshot = SnapshotGeneratorFactory.getInstance()
                        .createSnapshot(database.getDefaultSchema(), database, control);
                Table parent = snapshot.get(Table.class).stream()
                        .filter(table -> "DM_LB_PARENT".equalsIgnoreCase(table.getName()))
                        .findFirst().orElseThrow();
                Table child = snapshot.get(Table.class).stream()
                        .filter(table -> "DM_LB_CHILD".equalsIgnoreCase(table.getName()))
                        .findFirst().orElseThrow();
                ForeignKey childToParent = child.getOutgoingForeignKeys().stream()
                        .filter(foreignKey -> "FK_DM_LB_CHILD_PARENT".equalsIgnoreCase(foreignKey.getName()))
                        .findFirst().orElseThrow();
                assertAll(
                        () -> assertTrue(control.shouldInclude(UniqueConstraint.class)),
                        () -> assertEquals(2, parent.getColumns().size()),
                        () -> assertTrue(parent.isDefaultTablespace()),
                        () -> assertTrue(parent.getPrimaryKey() != null),
                        () -> assertFalse(parent.getUniqueConstraints().isEmpty()),
                        () -> assertEquals(3, child.getColumns().size()),
                        () -> assertTrue(child.getPrimaryKey() != null),
                        () -> assertEquals(child.getSchema().getCatalogName(),
                                childToParent.getForeignKeyTable().getSchema().getCatalogName()),
                        () -> assertEquals(child.getSchema().getName(),
                                childToParent.getForeignKeyTable().getSchema().getName()),
                        () -> assertTrue(child.getIndexes().stream().map(Index::getName)
                                .anyMatch("IDX_DM_LB_CHILD_PARENT"::equalsIgnoreCase)),
                        () -> assertTrue(snapshot.get(View.class).stream()
                                .anyMatch(view -> "DM_LB_CHILD_VIEW".equalsIgnoreCase(view.getName())))
                );

                SnapshotControl tablesOnlyControl = new SnapshotControl(database, Table.class);
                DatabaseSnapshot tablesOnlySnapshot = SnapshotGeneratorFactory.getInstance()
                        .createSnapshot(database.getDefaultSchema(), database, tablesOnlyControl);
                assertFalse(tablesOnlyControl.shouldInclude(Index.class));
                assertTrue(tablesOnlySnapshot.get(Table.class).stream()
                        .allMatch(table -> table.getIndexes().isEmpty()));

                PrimaryKey unidentifiedPrimaryKey = new PrimaryKey();
                assertNull(new TestableDmPrimaryKeySnapshotGenerator()
                        .snapshotUnidentified(unidentifiedPrimaryKey, snapshot));

                DatabaseSnapshot comparison = SnapshotGeneratorFactory.getInstance()
                        .createSnapshot(database.getDefaultSchema(), database, control);
                DiffResult diff = DiffGeneratorFactory.getInstance()
                        .compare(snapshot, comparison, new CompareControl());
                assertTrue(diff.areEqual());
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

    private static final class TestableDmPrimaryKeySnapshotGenerator extends DmPrimaryKeySnapshotGenerator {

        private DatabaseObject snapshotUnidentified(PrimaryKey primaryKey, DatabaseSnapshot snapshot) throws Exception {
            return snapshotObject(primaryKey, snapshot);
        }
    }
}
