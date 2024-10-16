package liquibase.datatype.core;

import liquibase.change.core.LoadDataChange;
import liquibase.database.Database;
import liquibase.database.core.AbstractDb2Database;
import liquibase.database.core.DerbyDatabase;
import liquibase.database.core.DmDatabase;
import liquibase.database.core.Gbase8sDatabase;
import liquibase.database.core.InformixDatabase;
import liquibase.database.core.MSSQLDatabase;
import liquibase.database.core.MySQLDatabase;
import liquibase.database.core.OracleDatabase;
import liquibase.database.core.PostgresDatabase;
import liquibase.datatype.DataTypeInfo;
import liquibase.datatype.DatabaseDataType;
import liquibase.datatype.LiquibaseDataType;
import liquibase.statement.DatabaseFunction;
import liquibase.util.StringUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@DataTypeInfo(name = "time", aliases = {"java.sql.Types.TIME", "java.sql.Time", "timetz"}, minParameters = 0, maxParameters = 0, priority = LiquibaseDataType.PRIORITY_DEFAULT)
public class TimeType extends LiquibaseDataType {

    protected static final int MSSQL_TYPE_TIME_DEFAULT_PRECISION = 7;
    private static final String PARENTHESIS_REGEX = "(\\(\\d+\\))";
    public static final Pattern PARENTHESIS_PATTERN = Pattern.compile(PARENTHESIS_REGEX);

    @Override
    public DatabaseDataType toDatabaseDataType(Database database) {
        String originalDefinition = StringUtil.trimToEmpty(getRawDefinition());
        if (database instanceof InformixDatabase || database instanceof Gbase8sDatabase) {
            return new DatabaseDataType("DATETIME HOUR TO FRACTION", 5);
        }
        if (database instanceof MSSQLDatabase) {
            Object[] parameters = getParameters();

            // If the scale for time is the database default anyway, omit it.
            if ((parameters.length >= 1) &&
                    (Integer.parseInt(parameters[0].toString()) == (database.getDefaultScaleForNativeDataType("time")))) {
                parameters = new Object[0];
            }
            return new DatabaseDataType(database.escapeDataTypeName("time"), parameters);
        }

        if (database instanceof MySQLDatabase) {
            boolean supportsParameters = true;
            try {
                supportsParameters = (database.getDatabaseMajorVersion() >= 5) && (database.getDatabaseMinorVersion()
                        >= 6) && (((MySQLDatabase) database).getDatabasePatchVersion() >= 4);
            } catch (Exception ignore) {
                //assume supports parameters
            }
            if (supportsParameters && (getParameters().length > 0) && (Integer.parseInt(getParameters()[0].toString()
            ) <= 6)) {
                return new DatabaseDataType(getName(), getParameters());
            } else {
                return new DatabaseDataType(getName());
            }
        }
        if (database instanceof OracleDatabase) {
            return new DatabaseDataType("DATE");
        }
        if (database instanceof DmDatabase) {
            return new DatabaseDataType("DATE");
        }

        if (database instanceof PostgresDatabase) {
            DatabaseDataType datatype = new DatabaseDataType(getName(), getValidatedParameters());
            String rawDefinition = originalDefinition.toLowerCase(Locale.US);

            final Object[] parameters = getParameters();
            if (parameters == null || parameters.length == 0) {
                final Matcher precisionMatcher = PARENTHESIS_PATTERN.matcher(rawDefinition);
                if (precisionMatcher.find()) {
                    datatype = new DatabaseDataType(datatype.getType() + precisionMatcher.group(1));
                }
            } else if (getValidatedParameters().length == 0 && getParameters().length >= 2) {
                datatype = new DatabaseDataType(datatype.getType(), getParameters()[1]);

            }

            if (rawDefinition.contains("tz") || rawDefinition.contains("with time zone")) {
                datatype.addAdditionalInformation("WITH TIME ZONE");
            } else {
                datatype.addAdditionalInformation("WITHOUT TIME ZONE");
            }
            return datatype;
        }

        return new DatabaseDataType(getName());
    }

    Object[] getValidatedParameters() {
        Object[] parameters = getParameters();
        return (parameters.length > 0 && (Integer.parseInt(getParameters()[0].toString()) <= 6)) ? parameters : new Object[0];
    }

    @Override
    public String objectToSql(Object value, Database database) {
        if ((value == null) || "null".equals(value.toString().toLowerCase(Locale.US))) {
            return null;
        } else if (value instanceof DatabaseFunction) {
            return database.generateDatabaseFunctionValue((DatabaseFunction) value);
        } else if (value instanceof java.sql.Time) {
            return database.getTimeLiteral(((java.sql.Time) value));
        } else {
            return "'" + ((String) value).replaceAll("'", "''") + "'";
        }
    }

    @Override
    public Object sqlToObject(String value, Database database) {
        if (zeroTime(value)) {
            return value;
        }

        if (database instanceof AbstractDb2Database) {
            return value.replaceFirst("^\"SYSIBM\".\"TIME\"\\('", "").replaceFirst("'\\)", "");
        }
        if (database instanceof DerbyDatabase) {
            return value.replaceFirst("^TIME\\('", "").replaceFirst("'\\)", "");
        }

        try {
            DateFormat timeFormat = getTimeFormat(database);

            if ((database instanceof OracleDatabase) && value.matches("to_date\\('\\d+:\\d+:\\d+', 'HH24:MI:SS'\\)")) {
                timeFormat = new SimpleDateFormat("HH:mm:s");
                value = value.replaceFirst(".*?'", "").replaceFirst("',.*", "");
            }
            if ((database instanceof DmDatabase) && value.matches("to_date\\('\\d+:\\d+:\\d+', 'HH24:MI:SS'\\)")) {
                timeFormat = new SimpleDateFormat("HH:mm:s");
                value = value.replaceFirst(".*?'", "").replaceFirst("',.*", "");
            }

            return new java.sql.Time(timeFormat.parse(value).getTime());
        } catch (ParseException e) {
            return new DatabaseFunction(value);
        }
    }

    @Override
    public LoadDataChange.LOAD_DATA_TYPE getLoadTypeName() {
        return LoadDataChange.LOAD_DATA_TYPE.DATE;
    }

    private boolean zeroTime(String stringVal) {
        return "".equals(stringVal.replace("-", "").replace(":", "").replace(" ", "").replace("0", ""));
    }

    protected DateFormat getTimeFormat(Database database) {
        if (database instanceof AbstractDb2Database) {
            return new SimpleDateFormat("HH.mm.ss");
        }
        return new SimpleDateFormat("HH:mm:ss");
    }


}
