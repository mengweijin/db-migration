package com.github.mengweijin.liquibase.dameng.snapshot;

import liquibase.snapshot.CachedRow;
import liquibase.structure.core.ForeignKey;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DmForeignKeySnapshotGeneratorTest {

    @Test
    void mapsAllCatalogDeferrabilityStates() {
        ForeignKey initiallyDeferred = foreignKey("DEFERRABLE", "DEFERRED");
        ForeignKey initiallyImmediate = foreignKey("DEFERRABLE", "IMMEDIATE");
        ForeignKey notDeferrable = foreignKey("NOT DEFERRABLE", "IMMEDIATE");

        assertAll(
                () -> assertTrue(initiallyDeferred.isDeferrable()),
                () -> assertTrue(initiallyDeferred.isInitiallyDeferred()),
                () -> assertTrue(initiallyImmediate.isDeferrable()),
                () -> assertFalse(initiallyImmediate.isInitiallyDeferred()),
                () -> assertFalse(notDeferrable.isDeferrable()),
                () -> assertFalse(notDeferrable.isInitiallyDeferred())
        );
    }

    private ForeignKey foreignKey(String deferrable, String deferred) {
        CachedRow row = new CachedRow(new HashMap<>(Map.of(
                "FK_DEFERRABLE", deferrable,
                "FK_DEFERRED", deferred
        )));
        ForeignKey foreignKey = new ForeignKey();
        DmForeignKeySnapshotGenerator.setDeferrability(foreignKey, row);
        return foreignKey;
    }
}
