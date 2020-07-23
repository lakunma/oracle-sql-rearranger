package kg.dump;

import kg.statements.trigger.AlterTrigger;
import kg.statements.trigger.CreateTrigger;

import java.util.Comparator;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

class Trigger implements Comparable<Trigger> {
    public final CreateTrigger createStatement;

    public final SortedSet<AlterTrigger> alterations = new TreeSet<>();

    public Trigger(CreateTrigger createStatement) {
        this.createStatement = Objects.requireNonNull(createStatement);
    }

    @Override
    public int compareTo(Trigger o) {
        return Comparator
                .<Trigger, String>comparing(t -> t.createStatement.tableName)
                .thenComparing(t -> t.createStatement.triggerName)
                .compare(this, o);
    }
}
