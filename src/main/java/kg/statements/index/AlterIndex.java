package kg.statements.index;

import kg.antlr.plsql.PlSqlParser;
import kg.statements.BaseStatement;
import kg.statements.CategorizedStatement;

import java.util.Comparator;

public class AlterIndex
        extends CategorizedStatement<PlSqlParser.Alter_indexContext>
        implements Comparable<AlterIndex> {

    public final String indexName;

    public String getIndexName() {
        return indexName;
    }

    public AlterIndex(PlSqlParser.Complete_statementContext statement) {
        super(statement);
        indexName = getSpecificStatement().index_name().getText();
    }

    @Override
    public int compareTo(AlterIndex o) {
        Comparator<AlterIndex> cmp = Comparator.comparing(AlterIndex::getIndexName).thenComparing(BaseStatement::getParsedText);
        return cmp.compare(this, o);
    }
}
