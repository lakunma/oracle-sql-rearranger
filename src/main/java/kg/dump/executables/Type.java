package kg.dump.executables;

import kg.statements.GrantStatement;
import kg.statements.executables.CreateType;
import kg.statements.executables.CreateTypeBody;

import java.util.SortedSet;
import java.util.TreeSet;

public class Type {
    public final CreateType declaration;
    public CreateTypeBody body;
    public final SortedSet<GrantStatement> grants = new TreeSet<>();

    public Type(CreateType declaration) {
        this.declaration = declaration;
    }
}
