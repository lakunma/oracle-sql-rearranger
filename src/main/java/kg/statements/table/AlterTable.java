package kg.statements.table;

import kg.antlr.plsql.PlSqlParser;
import kg.statements.BaseStatement;
import kg.statements.CategorizedStatement;

import java.util.Comparator;

public class AlterTable extends CategorizedStatement<PlSqlParser.Alter_tableContext> implements Comparable<AlterTable> {

    public final String tableName;

    public String getTableName() {
        return tableName;
    }

    public AlterTable(PlSqlParser.Complete_statementContext ctx) {
        super(ctx);
        tableName = getSpecificStatement().tableview_name().getText();
    }

    @Override
    public int compareTo(AlterTable o) {
        Comparator<AlterTable> cmp = Comparator.comparing(AlterTable::getTableName)
                .thenComparing(BaseStatement::getParsedText);
        return cmp.compare(this, o);
    }
}
