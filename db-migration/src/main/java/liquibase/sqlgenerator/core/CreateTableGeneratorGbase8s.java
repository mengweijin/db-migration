package liquibase.sqlgenerator.core;

import liquibase.database.core.Gbase8sDatabase;
import liquibase.database.Database;
import liquibase.statement.core.CreateTableStatement;

/**
 * @author mengweijin
 */
public class CreateTableGeneratorGbase8s extends CreateTableGeneratorInformix {

    @Override
    public boolean supports(CreateTableStatement statement, Database database) {
        return database instanceof Gbase8sDatabase;
    }
}
