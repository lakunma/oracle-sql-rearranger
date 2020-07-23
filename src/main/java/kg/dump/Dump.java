package kg.dump;

import kg.dump.executables.Function;
import kg.dump.executables.Package;
import kg.dump.executables.Procedure;
import kg.dump.executables.Type;
import kg.statements.CreateDbLink;
import kg.statements.CreateSynonym;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.*;

public class Dump {
    private static final Comparator<ParserRuleContext> NATURAL_ORDER_STATEMENTS_COMPARATOR =
            Comparator.comparing(ParserRuleContext::getText);

    public final SortedMap<String, CreateDbLink> dbLinks = new TreeMap<>();
    public final SortedMap<String, CreateSynonym> synonyms = new TreeMap<>();
    public final SortedMap<String, Sequence> sequences = new TreeMap<>();

    public final SortedMap<String, Type> types = new TreeMap<>();
    public final SortedMap<String, Function> functions = new TreeMap<>();
    public final SortedMap<String, Procedure> procedures = new TreeMap<>();
    public final SortedMap<String, Package> packages = new TreeMap<>();

    public final SortedMap<String, Table> tables = new TreeMap<>();
    public final SortedMap<String, View> views = new TreeMap<>();
    public final SortedMap<String, MView> mViews = new TreeMap<>();

    public final SortedSet<ParserRuleContext> otherStatements = new TreeSet<>(NATURAL_ORDER_STATEMENTS_COMPARATOR);
}
