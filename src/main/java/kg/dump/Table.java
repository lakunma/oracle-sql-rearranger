package kg.dump;

import kg.statements.GrantStatement;
import kg.statements.table.AlterTable;
import kg.statements.table.CommentOnTable;
import kg.statements.table.CommentOnTableColumn;
import kg.statements.table.CreateTable;

import java.util.SortedSet;
import java.util.TreeSet;

public class Table {
    public final CreateTable createStatement;

    public CommentOnTable tableComment;
    public final SortedSet<CommentOnTableColumn> columnComments = new TreeSet<>();
    public final SortedSet<GrantStatement> grants = new TreeSet<>();
    public final SortedSet<AlterTable> constraints = new TreeSet<>();
    public final SortedSet<Index> indexes = new TreeSet<>();
    public final SortedSet<Trigger> triggers = new TreeSet<>();

    public Table(CreateTable createStatement) {
        this.createStatement = createStatement;
    }

}
