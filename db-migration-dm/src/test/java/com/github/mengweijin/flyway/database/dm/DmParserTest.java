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

    @Test
    public void testParse() {
        Flyway flyway = Flyway.configure().load();
        Configuration configuration = flyway.getConfiguration();
        DmParser parser = new DmParser(configuration, new ParsingContext());
        LoadableResource scanForResource = new FileSystemResource(
                configuration.getLocations()[0],
                "src/test/resources/db/migration/V1__demo.sql",
                Charset.defaultCharset(),
                false
        );
        SqlStatementIterator sqlStatementIterator = parser.parse(scanForResource);
        while (sqlStatementIterator.hasNext()) {
            System.out.println(sqlStatementIterator.next().getSql());
            System.out.println("================");
        }
    }
}