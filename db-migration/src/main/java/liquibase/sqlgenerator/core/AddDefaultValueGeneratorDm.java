package liquibase.sqlgenerator.core;

import liquibase.database.Database;
import liquibase.database.core.DmDatabase;
import liquibase.statement.core.AddDefaultValueStatement;

/**
 * @author mengweijin
 */
public class AddDefaultValueGeneratorDm extends AddDefaultValueGeneratorOracle {
    @Override
    public boolean supports(AddDefaultValueStatement statement, Database database) {
        return database instanceof DmDatabase;
    }
}
