package com.github.mengweijin.flyway.database.gbase8s;

import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.internal.parser.Parser;
import org.flywaydb.core.internal.parser.ParserContext;
import org.flywaydb.core.internal.parser.ParsingContext;
import org.flywaydb.core.internal.parser.PeekingReader;
import org.flywaydb.core.internal.parser.Token;
import java.io.IOException;
import java.util.List;

/**
 * @author mengweijin
 */
public class Gbase8sParser extends Parser {
    public Gbase8sParser(Configuration configuration, ParsingContext parsingContext) {
        super(configuration, parsingContext, 2);
    }

    @Override
    protected void adjustBlockDepth(ParserContext context, List<Token> tokens, Token keyword, PeekingReader reader) throws IOException {
        int lastKeywordIndex = getLastKeywordIndex(tokens);
        if (lastKeywordIndex < 0) {
            return;
        }

        String current = keyword.getText();
        if ("FUNCTION".equals(current) || "PROCEDURE".equals(current)) {
            String previous = tokens.get(lastKeywordIndex).getText();

            // CREATE( DBA)? (FUNCTION|PROCEDURE)
            if ("CREATE".equals(previous) || "DBA".equals(previous)) {
                context.increaseBlockDepth(previous);
            } else if ("END".equals(previous)) {
                context.decreaseBlockDepth();
            }
        }
    }
}