package kg.dump;

import kg.statements.CreateSequence;
import kg.statements.GrantStatement;

import java.util.SortedSet;
import java.util.TreeSet;

public class Sequence {
    public final CreateSequence createStatement;
    public final SortedSet<GrantStatement> grants = new TreeSet<>();


    public Sequence(CreateSequence createStatement) {
        this.createStatement = createStatement;
    }
}
