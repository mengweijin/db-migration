package com.github.mengweijin.liquibase.dameng.datatype;

import com.github.mengweijin.liquibase.dameng.database.DmDatabase;
import liquibase.database.Database;
import liquibase.datatype.DataTypeInfo;
import liquibase.datatype.LiquibaseDataType;
import liquibase.datatype.core.CharType;

@DataTypeInfo(
        name = "char",
        aliases = {"java.sql.Types.CHAR", "bpchar", "character"},
        minParameters = 0,
        maxParameters = 1,
        priority = LiquibaseDataType.PRIORITY_DATABASE
)
public class DmCharType extends CharType {

    @Override
    public boolean supports(Database database) {
        return database instanceof DmDatabase;
    }
}
