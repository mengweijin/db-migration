package liquibase.sqlgenerator.core;

import liquibase.database.Database;
import liquibase.database.core.DmDatabase;
import liquibase.statement.core.InsertOrUpdateStatement;

/**
 * @author mengweijin
 */
public class InsertOrUpdateGeneratorDm extends InsertOrUpdateGeneratorOracle {
    @Override
    public boolean supports(InsertOrUpdateStatement statement, Database database) {
        return database instanceof DmDatabase;
    }
}
