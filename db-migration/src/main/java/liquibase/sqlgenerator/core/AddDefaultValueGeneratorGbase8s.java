package liquibase.sqlgenerator.core;

import liquibase.database.core.Gbase8sDatabase;
import liquibase.database.Database;
import liquibase.statement.core.AddDefaultValueStatement;

/**
 * @author mengweijin
 */
public class AddDefaultValueGeneratorGbase8s extends AddDefaultValueGeneratorInformix {
    @Override
    public boolean supports(AddDefaultValueStatement statement, Database database) {
        return database instanceof Gbase8sDatabase;
    }
}
