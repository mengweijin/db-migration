package liquibase.sqlgenerator.core;

import liquibase.database.core.Gbase8sDatabase;
import liquibase.database.Database;
import liquibase.statement.core.GetViewDefinitionStatement;

/**
 * @author mengweijin
 */
public class GetViewDefinitionGeneratorGbase8s extends GetViewDefinitionGeneratorInformix {
    @Override
    public boolean supports(GetViewDefinitionStatement statement, Database database) {
        return database instanceof Gbase8sDatabase;
    }
}
