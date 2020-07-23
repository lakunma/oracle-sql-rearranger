package kg.statements;

import kg.antlr.plsql.PlSqlParser;

public class CreateView extends CategorizedStatement<PlSqlParser.Create_viewContext> {
    public final String tableName;

    public CreateView(PlSqlParser.Complete_statementContext ctx) {
        super(ctx);
        tableName = getSpecificStatement().tableview_name().getText();
    }
}
