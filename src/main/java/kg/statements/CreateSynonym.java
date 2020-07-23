package kg.statements;

import kg.antlr.plsql.PlSqlParser;

public class CreateSynonym extends CategorizedStatement<PlSqlParser.Create_synonymContext> {

    public final String name;

    public CreateSynonym(PlSqlParser.Complete_statementContext ctx) {
        super(ctx);
        this.name = getSpecificStatement().synonym_name().getText();
    }

}
