package kg.statements.table;

import kg.antlr.plsql.PlSqlParser;
import kg.statements.CategorizedStatement;

public class CreateTable extends CategorizedStatement<PlSqlParser.Create_tableContext> {

    public final String tableName;

    public CreateTable(PlSqlParser.Complete_statementContext ctx) {
        super(ctx);
        tableName = getSpecificStatement().tableview_name().getText();
    }
}
