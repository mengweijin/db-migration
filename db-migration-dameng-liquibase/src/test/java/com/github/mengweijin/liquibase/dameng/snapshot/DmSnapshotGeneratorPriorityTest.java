package com.github.mengweijin.liquibase.dameng.snapshot;

import liquibase.snapshot.SnapshotGenerator;
import liquibase.structure.core.Schema;
import liquibase.structure.core.Table;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DmSnapshotGeneratorPriorityTest {

    private final DmTableSnapshotGenerator generator = new DmTableSnapshotGenerator();

    @Test
    void coreNoneDisablesObjectAndContainerPriorities() {
        assertEquals(SnapshotGenerator.PRIORITY_NONE,
                DmSnapshotGeneratorPriority.fromCore(generator, Table.class, SnapshotGenerator.PRIORITY_NONE));
        assertEquals(SnapshotGenerator.PRIORITY_NONE,
                DmSnapshotGeneratorPriority.fromCore(generator, Schema.class, SnapshotGenerator.PRIORITY_NONE));
    }

    @Test
    void replacementUsesStandardCoreAndContainerPriorities() {
        assertEquals(SnapshotGenerator.PRIORITY_DEFAULT,
                DmSnapshotGeneratorPriority.fromCore(generator, null, SnapshotGenerator.PRIORITY_DEFAULT));
        assertEquals(SnapshotGenerator.PRIORITY_DEFAULT,
                DmSnapshotGeneratorPriority.fromCore(generator, Table.class, SnapshotGenerator.PRIORITY_DEFAULT));
        assertEquals(SnapshotGenerator.PRIORITY_ADDITIONAL,
                DmSnapshotGeneratorPriority.fromCore(generator, Schema.class, SnapshotGenerator.PRIORITY_DEFAULT));
    }
}
