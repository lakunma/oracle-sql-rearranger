package kg.statements.index;

import kg.antlr.plsql.PlSqlParser;
import kg.statements.CategorizedStatement;

public class CreateIndex extends CategorizedStatement<PlSqlParser.Create_indexContext> {
    public final String indexName;
    public final String tableName;

    public CreateIndex(PlSqlParser.Complete_statementContext createStatement) {
        super(createStatement);
        indexName = getSpecificStatement().index_name().getText();

        if (getSpecificStatement().table_index_clause() != null) {
            tableName = getSpecificStatement().table_index_clause().tableview_name().getText();
        } else if (getSpecificStatement().bitmap_join_index_clause() != null) {
            tableName = getSpecificStatement().bitmap_join_index_clause().tableview_name(0).getText();
        } else {
            tableName = null;
        }
    }

    public boolean isTableIndex() {
        return tableName != null;
    }
}
