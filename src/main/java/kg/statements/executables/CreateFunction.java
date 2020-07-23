package kg.statements.executables;

import kg.antlr.plsql.PlSqlParser;
import kg.statements.CategorizedStatement;

public class CreateFunction extends CategorizedStatement<PlSqlParser.Create_function_bodyContext> {
    public final String functionName;

    public CreateFunction(PlSqlParser.Complete_statementContext ctx) {
        super(ctx);
        functionName = getSpecificStatement().function_name().getText();
    }
}
