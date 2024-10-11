package liquibase.sqlgenerator.core;

import liquibase.database.Database;
import liquibase.database.core.DmDatabase;
import liquibase.statement.core.GetViewDefinitionStatement;

/**
 * @author mengweijin
 */
public class GetViewDefinitionGeneratorDm extends GetViewDefinitionGeneratorOracle {
    @Override
    public boolean supports(GetViewDefinitionStatement statement, Database database) {
        return database instanceof DmDatabase;
    }
}
