package liquibase.datatype.core;

import liquibase.GlobalConfiguration;
import liquibase.change.core.LoadDataChange;
import liquibase.database.Database;
import liquibase.database.core.AbstractDb2Database;
import liquibase.database.core.DerbyDatabase;
import liquibase.database.core.DmDatabase;
import liquibase.database.core.FirebirdDatabase;
import liquibase.database.core.Gbase8sDatabase;
import liquibase.database.core.H2Database;
import liquibase.database.core.HsqlDatabase;
import liquibase.database.core.InformixDatabase;
import liquibase.database.core.MSSQLDatabase;
import liquibase.database.core.MySQLDatabase;
import liquibase.database.core.OracleDatabase;
import liquibase.database.core.PostgresDatabase;
import liquibase.database.core.SybaseASADatabase;
import liquibase.database.core.SybaseDatabase;
import liquibase.datatype.DataTypeInfo;
import liquibase.datatype.DatabaseDataType;
import liquibase.datatype.LiquibaseDataType;
import liquibase.statement.DatabaseFunction;

import java.util.Locale;

/**
 * Represents a signed integer number using 64 bits of storage.
 */
@DataTypeInfo(name = "bigint", aliases = {"java.sql.Types.BIGINT", "java.math.BigInteger", "java.lang.Long", "integer8", "bigserial", "serial8", "int8"}, minParameters = 0, maxParameters = 1, priority = LiquibaseDataType.PRIORITY_DEFAULT)
public class BigIntType extends LiquibaseDataType {

    private boolean autoIncrement;

    @Override
    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    @Override
    public DatabaseDataType toDatabaseDataType(Database database) {
        if (database instanceof InformixDatabase || database instanceof Gbase8sDatabase) {
            if (isAutoIncrement()) {
                return new DatabaseDataType("SERIAL8");
            } else {
                return new DatabaseDataType("INT8");
            }
        }
        if (database instanceof OracleDatabase) {
            return new DatabaseDataType("NUMBER", 38, 0);
        }
        if (database instanceof DmDatabase) {
            return new DatabaseDataType("NUMBER", 38, 0);
        }
        if (database instanceof MSSQLDatabase) {
            return new DatabaseDataType(database.escapeDataTypeName("bigint"));
        }
        if (database instanceof MySQLDatabase) {
            DatabaseDataType type = new DatabaseDataType("BIGINT");
            type.addAdditionalInformation(getAdditionalInformation());
            return type;
        }
        if ((database instanceof AbstractDb2Database) || (database instanceof DerbyDatabase) || (database instanceof
                HsqlDatabase) || (database instanceof FirebirdDatabase) || (database instanceof SybaseDatabase)
                || (database instanceof SybaseASADatabase) || (database instanceof H2Database)) {
            return new DatabaseDataType("BIGINT");
        }
        if (database instanceof PostgresDatabase) {
            if (isAutoIncrement() && ((PostgresDatabase) database).useSerialDatatypes()) {
                return new DatabaseDataType("BIGSERIAL");
            } else {
                if (GlobalConfiguration.CONVERT_DATA_TYPES.getCurrentValue() || this.getRawDefinition() == null) {
                    DatabaseDataType type = new DatabaseDataType("BIGINT");
                    type.addAdditionalInformation(getAdditionalInformation());
                    return type;
                } else {
                    return new DatabaseDataType(this.getRawDefinition());
                }
            }
        }
        return super.toDatabaseDataType(database);
    }

    @Override
    public void finishInitialization(String originalDefinition) {
        super.finishInitialization(originalDefinition);

        if (originalDefinition.toLowerCase(Locale.US).contains("serial")) {
            autoIncrement = true;
        }
    }

    @Override
    public String objectToSql(Object value, Database database) {
        if ((value == null) || "null".equals(value.toString().toLowerCase(Locale.US))) {
            return null;
        }
        if (value instanceof DatabaseFunction) {
            return value.toString();
        }

        return formatNumber(value.toString());
    }

    @Override
    public LoadDataChange.LOAD_DATA_TYPE getLoadTypeName() {
        return LoadDataChange.LOAD_DATA_TYPE.NUMERIC;
    }
}
