package liquibase.snapshot.jvm;

import liquibase.Scope;
import liquibase.database.core.Gbase8sDatabase;
import liquibase.database.Database;
import liquibase.exception.DatabaseException;
import liquibase.executor.ExecutorService;
import liquibase.snapshot.CachedRow;
import liquibase.statement.core.RawSqlStatement;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Column;
import liquibase.structure.core.DataType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mengweijin
 */
public class ColumnSnapshotGeneratorGbase8s extends ColumnSnapshotGeneratorInformix {

    private static final Map<Integer, String> qualifiers = new HashMap<>();

    static {
        qualifiers.put(0, "YEAR");
        qualifiers.put(2, "MONTH");
        qualifiers.put(4, "DAY");
        qualifiers.put(6, "HOUR");
        qualifiers.put(8, "MINUTE");
        qualifiers.put(10, "SECOND");
        qualifiers.put(11, "FRACTION(1)");
        qualifiers.put(12, "FRACTION(2)");
        qualifiers.put(13, "FRACTION(3)");
        qualifiers.put(14, "FRACTION(4)");
        qualifiers.put(15, "FRACTION(5)");
    }
    @Override
    public int getPriority(Class<? extends DatabaseObject> objectType, Database database) {
        if (database instanceof Gbase8sDatabase) {
            return PRIORITY_DATABASE;
        } else {
            return PRIORITY_NONE; // Other DB? Let the generic handler do it.
        }
    }

    /**
     * https://github.com/liquibase/liquibase/issues/1462
     * @param database database
     * @param defaultValue defaultValue
     * @return collength
     */
    private int collengthQuery(Database database, int defaultValue) {
        try {
            String query = "SELECT collength FROM syscolumns WHERE tabid = (SELECT tabid FROM systables WHERE tabname = 'databasechangelog') AND colname = 'dateexecuted'";
            return Scope.getCurrentScope().getSingleton(ExecutorService.class).getExecutor("jdbc", database).queryForObject(new RawSqlStatement(query), Integer.class);
        } catch (Exception e) {
            Scope.getCurrentScope().getLog(getClass()).info("Error query collength!", e);
        }
        return defaultValue;
    }

    @Override
    protected DataType readDataType(CachedRow columnMetadataResultSet, Column column, Database database) throws DatabaseException {
        // For an explanation of the information encoded in the column length, please see
        // https://www.ibm.com/support/knowledgecenter/SSGU8G_11.50.0/com.ibm.sqlr.doc/ids_sqr_027.htm
        String typeName = columnMetadataResultSet.getString("TYPE_NAME").toUpperCase();
        if ("DATETIME".equals(typeName) || "INTERVAL".equals(typeName)) {
            int colLength = columnMetadataResultSet.getInt("COLUMN_SIZE");
            colLength = collengthQuery(database, colLength);
            int firstQualifierType = (colLength % 256) / 16;
            int lastQualifierType = (colLength % 256) % 16;
            String type = "DATETIME".equals(typeName) ? "DATETIME" : "INTERVAL";

            String firstQualifier = qualifiers.get(firstQualifierType);
            String lastQualifier = qualifiers.get(lastQualifierType);

            if (firstQualifier == null) {
                throw new liquibase.exception.DatabaseException(
                        String.format(
                                "Encountered unknown firstQualifier code (%d) for column '%s', basic date type '%s', " +
                                        "while trying to decipher information encoded in the column length (%d)",
                                firstQualifierType, column.toString(), typeName, colLength)
                );
            }

            if (lastQualifier == null) {
                throw new liquibase.exception.DatabaseException(
                        String.format(
                                "Encountered unknown lastQualifier code (%d) for column '%s', basic date type '%s', " +
                                        "while trying to decipher information encoded in the column length (%d)",
                                firstQualifierType, column.toString(), typeName, colLength)
                );
            }

            DataType dataTypeMetaData = new DataType(type + " " + firstQualifier + " TO " + lastQualifier);
            dataTypeMetaData.setColumnSizeUnit(DataType.ColumnSizeUnit.BYTE);

            return dataTypeMetaData;
        } else {
            return super.readDataType(columnMetadataResultSet, column, database);
        }
    }
}
