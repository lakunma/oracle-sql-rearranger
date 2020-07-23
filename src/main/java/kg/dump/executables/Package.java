package kg.dump.executables;

import kg.statements.GrantStatement;
import kg.statements.pkg.CreatePackage;
import kg.statements.pkg.CreatePackageBody;

import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

public class Package {
    public final CreatePackage definition;
    public CreatePackageBody body;
    public final SortedSet<GrantStatement> grants = new TreeSet<>();

    public Package(CreatePackage definition) {
        this.definition = Objects.requireNonNull(definition);
    }

}
