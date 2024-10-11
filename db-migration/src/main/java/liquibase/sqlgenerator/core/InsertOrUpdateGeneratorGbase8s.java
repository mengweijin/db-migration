package liquibase.sqlgenerator.core;

import liquibase.database.core.Gbase8sDatabase;
import liquibase.database.Database;
import liquibase.statement.core.InsertOrUpdateStatement;

/**
 * @author mengweijin
 */
public class InsertOrUpdateGeneratorGbase8s extends InsertOrUpdateGeneratorInformix {
    @Override
    public boolean supports(InsertOrUpdateStatement statement, Database database) {
        return database instanceof Gbase8sDatabase;
    }

}
