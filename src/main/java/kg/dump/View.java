package kg.dump;

import kg.statements.CreateView;
import kg.statements.GrantStatement;
import kg.statements.table.CommentOnTable;
import kg.statements.table.CommentOnTableColumn;

import java.util.SortedSet;
import java.util.TreeSet;

public class View {
    public final CreateView createStatement;

    public CommentOnTable viewComment;
    public final SortedSet<CommentOnTableColumn> columnComments = new TreeSet<>();
    public final SortedSet<GrantStatement> grants = new TreeSet<>();

    public View(CreateView createStatement) {
        this.createStatement = createStatement;
    }
}
