package com.github.mengweijin.flyway.database.dm;


import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.api.resource.LoadableResource;
import org.flywaydb.core.internal.parser.ParsingContext;
import org.flywaydb.core.internal.resource.filesystem.FileSystemResource;
import org.flywaydb.core.internal.sqlscript.SqlStatementIterator;
import org.junit.jupiter.api.Test;
import java.nio.charset.Charset;

public class DmParserTest {

    public static String SQL_FILE_1 = "src/test/resources/db/migration/V1__demo.sql";

    public static String SQL_FILE_2 = "src/test/resources/db/migration/V2__demo.sql";

    @Test
    public void testParse1() {
        parse(SQL_FILE_1);
    }

    /**
     * <a href="https://gitee.com/mengweijin/db-migration/issues/IBJH0F">https://gitee.com/mengweijin/db-migration/issues/IBJH0F</a>
     */
    @Test
    public void testParse2() {
        parse(SQL_FILE_2);
    }

    public void parse(String sqlFile) {
        Flyway flyway = Flyway.configure().load();
        Configuration configuration = flyway.getConfiguration();
        DmParser parser = new DmParser(configuration, new ParsingContext());
        LoadableResource scanForResource = new FileSystemResource(
                configuration.getLocations()[0],
                sqlFile,
                Charset.defaultCharset(),
                false
        );
        try (SqlStatementIterator sqlStatementIterator = parser.parse(scanForResource)) {
            while (sqlStatementIterator.hasNext()) {
                System.out.println(sqlStatementIterator.next().getSql());
                System.out.println("==============================================");
            }
        }
    }
}