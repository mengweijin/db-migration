package liquibase.sqlgenerator.core;

import liquibase.database.core.Gbase8sDatabase;
import liquibase.database.Database;
import liquibase.statement.core.AddAutoIncrementStatement;

/**
 * @author mengweijin
 */
public class AddAutoIncrementGeneratorGbase8s extends AddAutoIncrementGeneratorInformix {

    @Override
    public boolean supports(AddAutoIncrementStatement statement, Database database) {
        return database instanceof Gbase8sDatabase;
    }
}
