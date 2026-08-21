package com.github.mengweijin.liquibase.dameng;

import com.github.mengweijin.liquibase.dameng.database.DmDatabase;
import com.github.mengweijin.liquibase.dameng.datatype.DmBooleanType;
import com.github.mengweijin.liquibase.dameng.datatype.DmCharType;
import com.github.mengweijin.liquibase.dameng.snapshot.DmColumnSnapshotGenerator;
import com.github.mengweijin.liquibase.dameng.snapshot.DmForeignKeySnapshotGenerator;
import com.github.mengweijin.liquibase.dameng.snapshot.DmIndexSnapshotGenerator;
import com.github.mengweijin.liquibase.dameng.snapshot.DmPrimaryKeySnapshotGenerator;
import com.github.mengweijin.liquibase.dameng.snapshot.DmTableSnapshotGenerator;
import com.github.mengweijin.liquibase.dameng.snapshot.DmUniqueConstraintSnapshotGenerator;
import com.github.mengweijin.liquibase.dameng.snapshot.DmViewSnapshotGenerator;
import liquibase.Scope;
import liquibase.database.Database;
import liquibase.database.core.MockDatabase;
import liquibase.database.core.OracleDatabase;
import liquibase.datatype.DataTypeFactory;
import liquibase.datatype.LiquibaseDataType;
import liquibase.snapshot.SnapshotGenerator;
import liquibase.snapshot.SnapshotGeneratorFactory;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Column;
import liquibase.structure.core.ForeignKey;
import liquibase.structure.core.Index;
import liquibase.structure.core.PrimaryKey;
import liquibase.structure.core.Schema;
import liquibase.structure.core.Table;
import liquibase.structure.core.UniqueConstraint;
import liquibase.structure.core.View;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DmExtensionTest {

    @AfterEach
    void resetSnapshotGeneratorFactory() {
        SnapshotGeneratorFactory.resetAll();
    }

    @Test
    void databaseAndServicesAreRegistered() throws Exception {
        Set<String> databases = Scope.getCurrentScope().getServiceLocator().findInstances(Database.class).stream()
                .map(instance -> instance.getClass().getName())
                .collect(Collectors.toSet());
        Set<String> snapshotGenerators = Scope.getCurrentScope().getServiceLocator()
                .findInstances(SnapshotGenerator.class).stream()
                .map(instance -> instance.getClass().getName())
                .collect(Collectors.toSet());
        Set<String> dataTypes = Scope.getCurrentScope().getServiceLocator()
                .findInstances(LiquibaseDataType.class).stream()
                .map(instance -> instance.getClass().getName())
                .collect(Collectors.toSet());

        assertTrue(databases.contains(DmDatabase.class.getName()));
        assertTrue(snapshotGenerators.containsAll(Set.of(
                DmColumnSnapshotGenerator.class.getName(), DmTableSnapshotGenerator.class.getName(),
                DmViewSnapshotGenerator.class.getName(), DmIndexSnapshotGenerator.class.getName(),
                DmPrimaryKeySnapshotGenerator.class.getName(), DmForeignKeySnapshotGenerator.class.getName(),
                DmUniqueConstraintSnapshotGenerator.class.getName())));
        assertTrue(dataTypes.contains(DmBooleanType.class.getName()));
        assertTrue(dataTypes.contains(DmCharType.class.getName()));
    }

    @Test
    void dataTypesOnlySupportDm() {
        DmDatabase dm = new DmDatabase();
        DmBooleanType booleanType = new DmBooleanType();
        DmCharType charType = new DmCharType();

        assertTrue(booleanType.supports(dm));
        assertFalse(booleanType.supports(new OracleDatabase()));
        assertTrue(charType.supports(dm));
        assertFalse(charType.supports(new OracleDatabase()));
        assertEquals("NUMBER(1)", booleanType.toDatabaseDataType(dm).toString());
        assertEquals("'a''b'", charType.objectToSql("a'b", dm));
        assertTrue(charType.objectToSql("x".repeat(4001), dm).startsWith("to_clob("));
        assertEquals(DmBooleanType.class, DataTypeFactory.getInstance().fromDescription("boolean", dm).getClass());
        assertEquals(DmCharType.class, DataTypeFactory.getInstance().fromDescription("char(16)", dm).getClass());
    }

    @Test
    void snapshotGeneratorsRejectOtherDatabases() {
        SnapshotGeneratorFactory.getInstance();
        SnapshotGenerator generator = new DmColumnSnapshotGenerator();
        assertTrue(generator.getPriority(Column.class, new DmDatabase()) > SnapshotGenerator.PRIORITY_NONE);
        assertEquals(SnapshotGenerator.PRIORITY_NONE,
                generator.getPriority(Column.class, new MockDatabase()));
        assertEquals(SnapshotGenerator.PRIORITY_NONE,
                generator.getPriority(Column.class, new OracleDatabase()));
    }

    @Test
    void containerPrioritiesRemainAboveObjectPriorities() {
        DmDatabase dm = new DmDatabase();
        assertContainerPriority(new DmColumnSnapshotGenerator(), Table.class, Column.class, dm);
        assertContainerPriority(new DmTableSnapshotGenerator(), Schema.class, Table.class, dm);
        assertContainerPriority(new DmViewSnapshotGenerator(), Schema.class, View.class, dm);
        assertContainerPriority(new DmIndexSnapshotGenerator(), Table.class, Index.class, dm);
        assertContainerPriority(new DmPrimaryKeySnapshotGenerator(), Table.class, PrimaryKey.class, dm);
        assertContainerPriority(new DmForeignKeySnapshotGenerator(), Table.class, ForeignKey.class, dm);
        assertContainerPriority(new DmUniqueConstraintSnapshotGenerator(), Table.class, UniqueConstraint.class, dm);
    }

    private void assertContainerPriority(SnapshotGenerator generator,
                                         Class<? extends DatabaseObject> containerType,
                                         Class<? extends DatabaseObject> objectType,
                                         Database database) {
        assertTrue(generator.getPriority(containerType, database) > generator.getPriority(objectType, database));
    }
}
