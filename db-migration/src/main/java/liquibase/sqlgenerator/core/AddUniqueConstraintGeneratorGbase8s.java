package liquibase.sqlgenerator.core;

import liquibase.database.core.Gbase8sDatabase;
import liquibase.database.Database;
import liquibase.statement.core.AddUniqueConstraintStatement;

/**
 * @author mengweijin
 */
public class AddUniqueConstraintGeneratorGbase8s extends AddUniqueConstraintGeneratorInformix {
    @Override
    public boolean supports(AddUniqueConstraintStatement statement, Database database) {
        return (database instanceof Gbase8sDatabase);
    }
}
