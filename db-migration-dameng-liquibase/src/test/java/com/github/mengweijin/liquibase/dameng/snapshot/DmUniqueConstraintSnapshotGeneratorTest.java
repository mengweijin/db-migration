package com.github.mengweijin.liquibase.dameng.snapshot;

import liquibase.snapshot.CachedRow;
import liquibase.structure.core.Table;
import liquibase.structure.core.UniqueConstraint;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DmUniqueConstraintSnapshotGeneratorTest {

    @Test
    void leavesBackingIndexUnsetWhenMetadataHasNoIndexName() {
        Table table = new Table().setName("DM_LB_PARENT");
        UniqueConstraint constraint = new UniqueConstraint().setName("UK_DM_LB_PARENT").setRelation(table);

        DmUniqueConstraintSnapshotGenerator.setBackingIndexIfPresent(
                constraint, new CachedRow(new HashMap<>()), table);

        assertNull(constraint.getBackingIndex());
    }

    @Test
    void createsBackingIndexWhenMetadataHasAnIndexName() {
        Table table = new Table().setName("DM_LB_PARENT");
        UniqueConstraint constraint = new UniqueConstraint().setName("UK_DM_LB_PARENT").setRelation(table);
        CachedRow row = new CachedRow(new HashMap<>(Map.of(
                "INDEX_NAME", "UK_DM_LB_PARENT_IDX",
                "INDEX_CATALOG", "DM_USER",
                "CONSTRAINT_SCHEM", "DM_USER"
        )));

        DmUniqueConstraintSnapshotGenerator.setBackingIndexIfPresent(constraint, row, table);

        assertEquals("UK_DM_LB_PARENT_IDX", constraint.getBackingIndex().getName());
        assertEquals("DM_LB_PARENT", constraint.getBackingIndex().getRelation().getName());
    }
}
