package kg.statements.executables;

import kg.antlr.plsql.PlSqlParser;
import kg.statements.CategorizedStatement;

public class CreateTypeBody extends CategorizedStatement<PlSqlParser.Create_type_bodyContext> {

    public final String typeName;

    public CreateTypeBody(PlSqlParser.Complete_statementContext ctx) {
        super(ctx);
        typeName = getSpecificStatement().type_name().getText();
    }
}
