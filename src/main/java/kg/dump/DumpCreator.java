package kg.dump;

import kg.GroupedStatements;
import kg.StatementType;
import kg.antlr.plsql.PlSqlParser;
import kg.dump.executables.Function;
import kg.dump.executables.Package;
import kg.dump.executables.Procedure;
import kg.dump.executables.Type;
import kg.statements.BaseStatement;
import kg.statements.CreateDbLink;
import kg.statements.CreateSynonym;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class DumpCreator {
    public static final Logger log = Logger.getLogger(DumpCreator.class.getName());

    private final GroupedStatements groupedStatements;
    private Map<String, Trigger> triggersByName;
    private Map<String, Index> indexesByName;

    private Dump dump = null;

    public DumpCreator(Map<StatementType, List<PlSqlParser.Complete_statementContext>> statements) {
        this.groupedStatements = new GroupedStatements(statements);
    }

    public Dump getDump() {
        if (dump == null) {
            dump = buildDump();
        }
        return dump;
    }

    private Dump buildDump() {
        var dump = new Dump();

        collectTriggers();
        collectIndexes();

        dump.tables.putAll(collectTables());
        dump.views.putAll(collectViews());
        dump.mViews.putAll(collectMViews());

        dump.dbLinks.putAll(collectDbLinks());
        dump.synonyms.putAll(collectSynonyms());
        dump.sequences.putAll(collectSequences());

        dump.types.putAll(collectTypes());
        dump.functions.putAll(collectFunctions());
        dump.procedures.putAll(collectProcedures());
        dump.packages.putAll(collectPackages());

        dump.otherStatements.addAll(collectOtherStatements());

        return dump;
    }

    private void collectTriggers() {
        triggersByName = groupedStatements.triggersByName.entrySet().stream()
                .collect(toMap(Map.Entry::getKey, e -> new Trigger(e.getValue())));
        triggersByName.forEach((name, tr) ->
                tr.alterations.addAll(groupedStatements.getList(gs -> gs.triggerAlterationsByName.remove(name))));
    }

    private void collectIndexes() {
        indexesByName = groupedStatements.indexesByName.entrySet().stream()
                .collect(toMap(Map.Entry::getKey, e -> new Index(e.getValue())));
        indexesByName.forEach((name, i) ->
                i.alterations.addAll(groupedStatements.getList(gs -> gs.indexAlterationsByName.remove(name))));
    }

    private List<ParserRuleContext> collectOtherStatements() {
        List<Stream<ParserRuleContext>> other = new ArrayList<>();

        if (!groupedStatements.grantsByObjName.isEmpty()) {
            log.info("Grants for the following objects were not associated with object's DDL: "
                    + groupedStatements.grantsByObjName.keySet() + ". They will be merged with uncategorized statements.");
            groupedStatements.grantsByObjName.values().forEach(v -> other.add(v.stream().map(BaseStatement::getAntlrCtx)));
        }
        if (!indexesByName.isEmpty()) {
            log.info("The following indexes were not associated with any table: "
                    + indexesByName.keySet() + ". They will be merged with uncategorized statements.");
            other.add(indexesByName.values().stream().map(i -> i.createStatement).map(BaseStatement::getAntlrCtx));
            other.add(indexesByName.values().stream().flatMap(i -> i.alterations.stream()).map(BaseStatement::getAntlrCtx));
        }
        if (!groupedStatements.indexAlterationsByName.isEmpty()) {
            log.info("The following indexes alterations were not associated with any index DDL: "
                    + groupedStatements.indexAlterationsByName.keySet() + ". They will be merged with uncategorized statements.");
            groupedStatements.indexAlterationsByName.values().forEach(v -> other.add(v.stream().map(BaseStatement::getAntlrCtx)));
        }
        if (!triggersByName.isEmpty()) {
            log.info("The following triggers were not associated with any table: "
                    + triggersByName.keySet() + ". They will be merged with uncategorized statements.");
            other.add(triggersByName.values().stream().map(t -> t.createStatement).map(BaseStatement::getAntlrCtx));
            other.add(triggersByName.values().stream().flatMap(t -> t.alterations.stream()).map(BaseStatement::getAntlrCtx));
        }
        if (!groupedStatements.triggerAlterationsByName.isEmpty()) {
            log.info("The following triggers alterations were not associated with any trigger DDL: "
                    + groupedStatements.triggerAlterationsByName.keySet() + ". They will be merged with uncategorized statements.");
            groupedStatements.triggerAlterationsByName.values().forEach(v -> other.add(v.stream().map(BaseStatement::getAntlrCtx)));
        }
        if (!groupedStatements.mViewCommentsByName.isEmpty()) {
            log.info("The following mview comments were not associated with any mview DDL: "
                    + groupedStatements.mViewCommentsByName.keySet() + ". They will be merged with uncategorized statements.");
            other.add(groupedStatements.mViewCommentsByName.values().stream().map(BaseStatement::getAntlrCtx));
        }
        if (!groupedStatements.typesBodiesByName.isEmpty()) {
            log.info("The following type bodies were not associated with any type definition statement: "
                    + groupedStatements.typesBodiesByName.keySet() + ". They will be merged with uncategorized statements.");
            other.add(groupedStatements.typesBodiesByName.values().stream().map(BaseStatement::getAntlrCtx));
        }
        if (!groupedStatements.packageBodiesByName.isEmpty()) {
            log.info("The following package bodies were not associated with any package definition statement: "
                    + groupedStatements.packageBodiesByName.keySet() + ". They will be merged with uncategorized statements.");
            other.add(groupedStatements.packageBodiesByName.values().stream().map(BaseStatement::getAntlrCtx));
        }
        if (!groupedStatements.constraintsByTableName.isEmpty()) {
            log.info("Constraints for the following tables were not associated with any table DDL statement: "
                    + groupedStatements.constraintsByTableName.keySet() + ". They will be merged with uncategorized statements.");
            groupedStatements.constraintsByTableName.values().forEach(v -> other.add(v.stream().map(BaseStatement::getAntlrCtx)));
        }
        if (!groupedStatements.tableCommentsByTableName.isEmpty()) {
            log.info("Table comments for the following tables were not associated with any table DDL statement: "
                    + groupedStatements.tableCommentsByTableName.keySet() + ". They will be merged with uncategorized statements.");
            other.add(groupedStatements.tableCommentsByTableName.values().stream().map(BaseStatement::getAntlrCtx));
        }
        if (!groupedStatements.columnCommentsByTableName.isEmpty()) {
            log.info("Column comments for the following tables were not associated with any table DDL statement: "
                    + groupedStatements.columnCommentsByTableName.keySet() + ". They will be merged with uncategorized statements.");
            groupedStatements.columnCommentsByTableName.values().forEach(v -> other.add(v.stream().map(BaseStatement::getAntlrCtx)));
        }
        other.add(groupedStatements.notCategorizedStatements.stream().map(BaseStatement::getAntlrCtx));

        return Stream.of(other.toArray()).flatMap(s -> ((Stream<ParserRuleContext>) s)).collect(toList());
    }

    private Map<String, MView> collectMViews() {
        var mViewsMap = groupedStatements.mViewsByName
                .entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, e -> new MView(e.getValue())));
        mViewsMap.forEach((name, mView) -> {
            mView.grants.addAll(groupedStatements.getList(gs -> gs.grantsByObjName.remove(name)));
            mView.mViewComment = groupedStatements.mViewCommentsByName.remove(name);
        });
        return mViewsMap;
    }

    private Map<String, Package> collectPackages() {
        var packagesMap = groupedStatements.packagesByName
                .entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, e -> new Package(e.getValue())));

        packagesMap.values().forEach(p -> p.body = groupedStatements.packageBodiesByName.remove(p.definition.packageName));

        packagesMap.forEach((name, pkg) ->
                pkg.grants.addAll(groupedStatements.getList(gs -> gs.grantsByObjName.remove(name))));
        return packagesMap;
    }

    private Map<String, Procedure> collectProcedures() {
        var proceduresMap = groupedStatements.proceduresByName
                .entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, e -> new Procedure(e.getValue())));
        proceduresMap.forEach((name, procedure) ->
                procedure.grants.addAll(groupedStatements.getList(gs -> gs.grantsByObjName.remove(name))));
        return proceduresMap;
    }

    private Map<String, Function> collectFunctions() {
        var functionMap = groupedStatements.functionsByName
                .entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, e -> new Function(e.getValue())));
        functionMap.forEach((name, function) ->
                function.grants.addAll(groupedStatements.getList(gs -> gs.grantsByObjName.remove(name))));
        return functionMap;
    }

    private Map<String, Type> collectTypes() {
        var typesMap = groupedStatements.typesByName
                .entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, e -> new Type(e.getValue())));
        typesMap.values().forEach(t -> t.body = groupedStatements.typesBodiesByName.remove(t.declaration.typeName));
        typesMap.forEach((name, type) ->
                type.grants.addAll(groupedStatements.getList(gs -> gs.grantsByObjName.remove(name))));
        return typesMap;
    }

    private Map<String, CreateSynonym> collectSynonyms() {
        return groupedStatements.synonymsByName;
    }

    private Map<String, Table> collectTables() {
        var tablesMap = groupedStatements.tablesByName
                .entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, e -> new Table(e.getValue())));
        tablesMap.forEach((name, table) -> {
            table.grants.addAll(groupedStatements.getList(gs -> gs.grantsByObjName.remove(name)));
            table.tableComment = groupedStatements.tableCommentsByTableName.remove(name);
            table.columnComments.addAll(groupedStatements.getList(gs -> gs.columnCommentsByTableName.remove(name)));

            table.constraints.addAll(groupedStatements.getList(gs -> gs.constraintsByTableName.remove(name)));
            table.indexes.addAll(collectTableIndexes(name));
            table.triggers.addAll(collectTableTriggers(name));
        });
        return tablesMap;
    }

    private List<Index> collectTableIndexes(String tableName) {
        List<Index> indexes = new ArrayList<>();
        var iterator = indexesByName.entrySet().iterator();
        while (iterator.hasNext()) {
            var index = iterator.next().getValue();
            if (Objects.equals(index.createStatement.tableName, tableName)) {
                indexes.add(index);
                iterator.remove();
            }
        }
        return indexes;
    }

    private List<Trigger> collectTableTriggers(String tableName) {
        List<Trigger> triggers = new ArrayList<>();
        var iterator = triggersByName.entrySet().iterator();
        while (iterator.hasNext()) {
            var trigger = iterator.next().getValue();
            if (trigger.createStatement.tableName != null &&
                    (tableName.contains(trigger.createStatement.tableName) || trigger.createStatement.tableName.contains(tableName))) {
                triggers.add(trigger);
                iterator.remove();
            }
        }
        return triggers;
    }

    private Map<String, CreateDbLink> collectDbLinks() {
        return groupedStatements.dbLinksByName;
    }

    private Map<String, Sequence> collectSequences() {
        var sequencesMap = groupedStatements.sequencesByName
                .entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, e -> new Sequence(e.getValue())));
        sequencesMap.forEach((name, sequence) ->
                sequence.grants.addAll(groupedStatements.getList(gs -> gs.grantsByObjName.remove(name))));
        return sequencesMap;
    }

    private Map<String, View> collectViews() {
        var viewsMap = groupedStatements.viewsByName
                .entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, e -> new View(e.getValue())));
        viewsMap.forEach((name, view) -> {
            view.grants.addAll(groupedStatements.getList(gs -> gs.grantsByObjName.remove(name)));
            view.viewComment = groupedStatements.tableCommentsByTableName.remove(name);
            view.columnComments.addAll(groupedStatements.getList(gs -> gs.columnCommentsByTableName.remove(name)));
        });
        return viewsMap;
    }
}
