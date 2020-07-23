package kg.statements.table;

import kg.antlr.plsql.PlSqlParser;
import kg.statements.CategorizedStatement;

public class CommentOnTable extends CategorizedStatement<PlSqlParser.Comment_on_tableContext> {

    public final String tableName;

    public CommentOnTable(PlSqlParser.Complete_statementContext ctx) {
        super(ctx);
        tableName = getSpecificStatement().tableview_name().getText();
    }
}
