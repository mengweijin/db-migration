package com.github.mengweijin.flyway.database.dm;


import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.Location;
import org.flywaydb.core.api.resource.LoadableResource;
import org.flywaydb.core.internal.parser.ParsingContext;
import org.flywaydb.core.internal.scanner.filesystem.FileSystemScanner;
import org.flywaydb.core.internal.sqlscript.SqlStatementIterator;
import org.junit.Test;

import java.nio.charset.Charset;

public class DmParserTest {

    @Test
    public void testParse() {
        FileSystemScanner fileSystemScanner = new FileSystemScanner(Charset.defaultCharset(), false, false, true);
        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:dm://localhost:5236", "SYSDBA", "")
                .locations("src/test/resources/db/migration")
                .load();
        Location[] locations = flyway.getConfiguration().getLocations();
        DmParser parser = new DmParser(flyway.getConfiguration(), new ParsingContext());
        for (LoadableResource scanForResource : fileSystemScanner.scanForResources(locations[0])) {
            SqlStatementIterator sqlStatementIterator = parser.parse(scanForResource);
            while (sqlStatementIterator.hasNext()) {
                System.out.println(sqlStatementIterator.next().getSql());
                System.out.println("================");
            }
        }
    }
}