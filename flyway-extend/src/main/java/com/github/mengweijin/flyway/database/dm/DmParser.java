package com.github.mengweijin.flyway.database.dm;

import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.internal.database.oracle.OracleParser;
import org.flywaydb.core.internal.parser.ParsingContext;

/**
 * @author mengweijin
 */
public class DmParser extends OracleParser {
    public DmParser(Configuration configuration, ParsingContext parsingContext) {
        super(configuration, parsingContext);
    }

}
