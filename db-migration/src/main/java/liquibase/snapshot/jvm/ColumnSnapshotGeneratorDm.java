package liquibase.snapshot.jvm;

import liquibase.database.Database;
import liquibase.database.core.DmDatabase;
import liquibase.structure.DatabaseObject;

/**
 * @author mengweijin
 */
public class ColumnSnapshotGeneratorDm extends ColumnSnapshotGeneratorOracle {
    @Override
    public int getPriority(Class<? extends DatabaseObject> objectType, Database database) {
        if (database instanceof DmDatabase) {
            return PRIORITY_DATABASE;
        } else {
            return PRIORITY_NONE; // Other DB? Let the generic handler do it.
        }
    }
}
