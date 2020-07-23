package kg.dump.executables;

import kg.statements.GrantStatement;
import kg.statements.executables.CreateProcedure;

import java.util.SortedSet;
import java.util.TreeSet;

public class Procedure {
    public final CreateProcedure createStatement;
    public final SortedSet<GrantStatement> grants = new TreeSet<>();

    public Procedure(CreateProcedure createStatement) {
        this.createStatement = createStatement;
    }
}
