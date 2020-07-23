package kg.statements.executables;

import kg.antlr.plsql.PlSqlParser;
import kg.statements.CategorizedStatement;

public class CreateType extends CategorizedStatement<PlSqlParser.Create_typeContext> {

    public final String typeName;

    public CreateType(PlSqlParser.Complete_statementContext ctx) {
        super(ctx);
        typeName = getSpecificStatement().type_name().getText();
    }
}
