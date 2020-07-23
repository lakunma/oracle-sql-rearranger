package kg.statements.table;

import kg.antlr.plsql.PlSqlParser;
import kg.statements.CategorizedStatement;

public class CommentOnTableColumn
        extends CategorizedStatement<PlSqlParser.Comment_on_columnContext>
        implements Comparable<CommentOnTableColumn> {

    public final String tableName;
    public final String column_name;

    public CommentOnTableColumn(PlSqlParser.Complete_statementContext ctx) {
        super(ctx);
        var columnNameCtx = getSpecificStatement().column_name();
        tableName = columnNameCtx.identifier().getText() + "." + columnNameCtx.id_expression(0).getText();
        column_name = columnNameCtx.id_expression(1).getText();
    }

    @Override
    public int compareTo(CommentOnTableColumn o) {
        int tableNameComparison = this.tableName.compareTo(o.tableName);
        if (tableNameComparison != 0) {
            return tableNameComparison;
        }
        return this.column_name.compareTo(o.column_name);
    }
}
