package com.github.mengweijin.flyway.database.dm;

import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.internal.database.oracle.OracleParser;
import org.flywaydb.core.internal.parser.ParserContext;
import org.flywaydb.core.internal.parser.ParsingContext;
import org.flywaydb.core.internal.parser.PeekingReader;
import org.flywaydb.core.internal.parser.Token;

import java.util.List;

/**
 * @author mengweijin
 */
public class DmParser extends OracleParser {

    private static final String IF = "IF";
    private static final String EXISTS = "EXISTS";


    public DmParser(Configuration configuration, ParsingContext parsingContext) {
        super(configuration, parsingContext);
    }

    @Override
    protected void adjustBlockDepth(ParserContext context, List<Token> tokens, Token keyword, PeekingReader reader) {
        String keywordText = keyword.getText();
        Token previousToken = getPreviousToken(tokens, keyword.getParensDepth());
        if (EXISTS.equalsIgnoreCase(keywordText) && IF.equalsIgnoreCase(previousToken.getText())) {
            context.decreaseBlockDepth();
        } else {
            super.adjustBlockDepth(context, tokens, keyword, reader);
        }
    }
}
