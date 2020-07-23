package kg.statements;

import kg.antlr.plsql.PlSqlParser;

public class CreateDbLink extends CategorizedStatement<PlSqlParser.Create_database_linkContext> {

    public final String dbLink;

    public CreateDbLink(PlSqlParser.Complete_statementContext ctx) {
        super(ctx);
        dbLink = getSpecificStatement().link_name().getText();
    }
}
