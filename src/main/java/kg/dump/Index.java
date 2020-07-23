package kg.dump;

import kg.statements.index.AlterIndex;
import kg.statements.index.CreateIndex;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class Index implements Comparable<Index> {
    public final String indexName;
    public final CreateIndex createStatement;
    public final SortedSet<AlterIndex> alterations = new TreeSet<>();

    public Index(CreateIndex createStatement) {
        this.createStatement = createStatement;
        this.indexName = createStatement.indexName;
    }

    @Override
    public int compareTo(Index o) {
        return Comparator
                .<Index, String>comparing(i -> i.createStatement.tableName)
                .thenComparing(i -> i.createStatement.indexName)
                .compare(this, o);
    }
}
