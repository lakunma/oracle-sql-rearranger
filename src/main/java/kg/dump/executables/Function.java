package kg.dump.executables;

import kg.statements.GrantStatement;
import kg.statements.executables.CreateFunction;

import java.util.SortedSet;
import java.util.TreeSet;

public class Function {
    public final CreateFunction createStatement;
    public final SortedSet<GrantStatement> grants = new TreeSet<>();

    public Function(CreateFunction createStatement) {
        this.createStatement = createStatement;
    }
}
