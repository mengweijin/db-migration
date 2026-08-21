package com.github.mengweijin.liquibase.dameng.snapshot;

import liquibase.snapshot.SnapshotGenerator;
import liquibase.structure.DatabaseObject;

final class DmSnapshotGeneratorPriority {

    private DmSnapshotGeneratorPriority() {
    }

    static int fromCore(SnapshotGenerator generator, Class<? extends DatabaseObject> objectType, int corePriority) {
        if (corePriority == SnapshotGenerator.PRIORITY_NONE) {
            return SnapshotGenerator.PRIORITY_NONE;
        }
        Class<? extends DatabaseObject>[] containers = generator.addsTo();
        if (objectType != null && containers != null) {
            for (Class<? extends DatabaseObject> container : containers) {
                if (container.isAssignableFrom(objectType)) {
                    return SnapshotGenerator.PRIORITY_ADDITIONAL;
                }
            }
        }
        return corePriority;
    }
}
