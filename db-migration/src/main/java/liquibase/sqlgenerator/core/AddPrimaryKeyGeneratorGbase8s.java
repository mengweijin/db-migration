package liquibase.sqlgenerator.core;

import liquibase.database.core.Gbase8sDatabase;
import liquibase.database.Database;
import liquibase.statement.core.AddPrimaryKeyStatement;

/**
 * @author mengweijin
 */
public class AddPrimaryKeyGeneratorGbase8s extends AddPrimaryKeyGeneratorInformix {
    @Override
    public boolean supports(AddPrimaryKeyStatement statement, Database database) {
        return (database instanceof Gbase8sDatabase);
    }
}
