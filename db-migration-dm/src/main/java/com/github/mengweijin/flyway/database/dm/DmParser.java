package com.github.mengweijin.flyway.database.dm;

import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.internal.database.oracle.OracleParser;
import org.flywaydb.core.internal.parser.ParserContext;
import org.flywaydb.core.internal.parser.ParsingContext;
import org.flywaydb.core.internal.parser.PeekingReader;
import org.flywaydb.core.internal.parser.Token;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mengweijin
 */
public class DmParser extends OracleParser {

    private static final String IF = "IF";
    private static final String EXISTS = "EXISTS";

    private static final String CREATE_OR_DROP_IF_OR_NOT = "(CREATE|DROP)\\s(TABLE|PROCEDURE|FUNCTION)\\sIF(\\sNOT)?";


    public DmParser(Configuration configuration, ParsingContext parsingContext) {
        super(configuration, parsingContext);
    }

    @Override
    protected void adjustBlockDepth(ParserContext context, List<Token> tokens, Token keyword, PeekingReader reader) {
        String keywordText = keyword.getText();
        if (EXISTS.equals(keywordText) && context.getBlockDepth() > 0) {
            String sql = makeSql(tokens);
            if (sql.matches(CREATE_OR_DROP_IF_OR_NOT)) {
                context.decreaseBlockDepth();
                return;
            }
        }
        super.adjustBlockDepth(context, tokens, keyword, reader);
    }

    private String makeSql(List<Token> tokens) {
        return tokens
                .stream()
                .map(Token::getText)
                .collect(Collectors.joining(" "));
    }
}
