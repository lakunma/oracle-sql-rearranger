package kg.statements.executables;

import kg.antlr.plsql.PlSqlParser;
import kg.statements.CategorizedStatement;

public class CreateProcedure extends CategorizedStatement<PlSqlParser.Create_procedure_bodyContext> {
    public final String procedureName;

    public CreateProcedure(PlSqlParser.Complete_statementContext ctx) {
        super(ctx);
        procedureName = getSpecificStatement().procedure_name().getText();
    }
}
