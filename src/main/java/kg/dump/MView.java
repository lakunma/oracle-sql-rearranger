package kg.dump;

import kg.statements.GrantStatement;
import kg.statements.mview.CommentOnMView;
import kg.statements.mview.CreateMView;

import java.util.SortedSet;
import java.util.TreeSet;

public class MView {
    public final CreateMView createStatement;

    public CommentOnMView mViewComment;

    public final SortedSet<GrantStatement> grants = new TreeSet<>();

    public MView(CreateMView createStatement) {
        this.createStatement = createStatement;
    }
}
