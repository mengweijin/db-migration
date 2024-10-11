package liquibase.sqlgenerator.core;

import liquibase.database.core.Gbase8sDatabase;
import liquibase.database.Database;
import liquibase.statement.core.CreateViewStatement;

/**
 * @author mengweijin
 */
public class CreateViewGeneratorGbase8s extends CreateViewGeneratorInformix {
    @Override
    public boolean supports(CreateViewStatement statement, Database database) {
        return database instanceof Gbase8sDatabase;
    }
}
