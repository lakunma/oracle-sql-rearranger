package kg;

import kg.antlr.plsql.PlSqlParser;
import kg.statements.*;
import kg.statements.executables.CreateFunction;
import kg.statements.executables.CreateProcedure;
import kg.statements.executables.CreateType;
import kg.statements.executables.CreateTypeBody;
import kg.statements.index.AlterIndex;
import kg.statements.index.CreateIndex;
import kg.statements.mview.CommentOnMView;
import kg.statements.mview.CreateMView;
import kg.statements.pkg.CreatePackage;
import kg.statements.pkg.CreatePackageBody;
import kg.statements.table.AlterTable;
import kg.statements.table.CommentOnTable;
import kg.statements.table.CommentOnTableColumn;
import kg.statements.table.CreateTable;
import kg.statements.trigger.AlterTrigger;
import kg.statements.trigger.CreateTrigger;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.stream.Collectors.*;
import static kg.StatementType.*;

public class GroupedStatements {

    Logger log = Logger.getLogger(GroupedStatements.class.getName());

    public final List<NotCategorizedStatement> notCategorizedStatements;

    public final Map<String, List<GrantStatement>> grantsByObjName;
    public final Map<String, CreateSynonym> synonymsByName;
    public final Map<String, CreateSequence> sequencesByName;

    public final Map<String, CreateFunction> functionsByName;
    public final Map<String, CreateProcedure> proceduresByName;
    public final Map<String, CreateType> typesByName;
    public final Map<String, CreateTypeBody> typesBodiesByName;


    public final Map<String, CreateIndex> indexesByName;
    public final Map<String, List<AlterIndex>> indexAlterationsByName;

    public final Map<String, CreateTrigger> triggersByName;
    public final Map<String, List<AlterTrigger>> triggerAlterationsByName;


    public final Map<String, CreateMView> mViewsByName;
    public final Map<String, CommentOnMView> mViewCommentsByName;


    public final Map<String, CreatePackage> packagesByName;
    public final Map<String, CreatePackageBody> packageBodiesByName;

    public final Map<String, CreateTable> tablesByName;
    public final Map<String, List<AlterTable>> constraintsByTableName;
    public final Map<String, CommentOnTable> tableCommentsByTableName;
    public final Map<String, List<CommentOnTableColumn>> columnCommentsByTableName;


    public final Map<String, CreateDbLink> dbLinksByName;
    public final Map<String, CreateView> viewsByName;

    public GroupedStatements(Map<StatementType, List<PlSqlParser.Complete_statementContext>> statements) {
        if (log.isLoggable(Level.FINE)) {
            var statementsCount = statements.values().stream().mapToLong(Collection::size).sum();
            log.fine("Received " + statementsCount + " statements");
        }

        this.notCategorizedStatements = statements.getOrDefault(OTHER, Collections.emptyList())
                .stream()
                .map(OTHER::createDomainObject)
                .map(NotCategorizedStatement.class::cast)
                .collect(toList());

        this.grantsByObjName = statements.getOrDefault(GRANT, Collections.emptyList())
                .stream()
                .map(GRANT::createDomainObject)
                .map(GrantStatement.class::cast)
                .collect(groupingBy(s -> s.grantObjName));

        this.synonymsByName = statements.getOrDefault(CREATE_SYNONYM, Collections.emptyList())
                .stream()
                .map(CREATE_SYNONYM::createDomainObject)
                .map(CreateSynonym.class::cast)
                .collect(toMap(s -> s.name, s -> s));

        this.sequencesByName = statements.getOrDefault(CREATE_SEQUENCE, Collections.emptyList())
                .stream()
                .map(CREATE_SEQUENCE::createDomainObject)
                .map(CreateSequence.class::cast)
                .collect(toMap(s -> s.sequenceName, s -> s));

        this.functionsByName = statements.getOrDefault(CREATE_FUNCTION_BODY, Collections.emptyList())
                .stream()
                .map(CREATE_FUNCTION_BODY::createDomainObject)
                .map(CreateFunction.class::cast)
                .collect(toMap(s -> s.functionName, s -> s));

        this.proceduresByName = statements.getOrDefault(CREATE_PROCEDURE_BODY, Collections.emptyList())
                .stream()
                .map(CREATE_PROCEDURE_BODY::createDomainObject)
                .map(CreateProcedure.class::cast)
                .collect(toMap(s -> s.procedureName, s -> s));

        this.typesByName = statements.getOrDefault(CREATE_TYPE, Collections.emptyList())
                .stream()
                .map(CREATE_TYPE::createDomainObject)
                .map(CreateType.class::cast)
                .collect(toMap(s -> s.typeName, s -> s));

        this.typesBodiesByName = statements.getOrDefault(CREATE_TYPE_BODY, Collections.emptyList())
                .stream()
                .map(CREATE_TYPE_BODY::createDomainObject)
                .map(CreateTypeBody.class::cast)
                .collect(toMap(s -> s.typeName, s -> s));


        this.indexesByName = statements.getOrDefault(CREATE_INDEX, Collections.emptyList())
                .stream()
                .map(CREATE_INDEX::createDomainObject)
                .map(CreateIndex.class::cast)
                .collect(toMap(s -> s.indexName, s -> s));

        this.indexAlterationsByName = statements.getOrDefault(ALTER_INDEX, Collections.emptyList())
                .stream()
                .map(ALTER_INDEX::createDomainObject)
                .map(AlterIndex.class::cast)
                .collect(groupingBy(s -> s.indexName));


        this.triggersByName = statements.getOrDefault(CREATE_TRIGGER, Collections.emptyList())
                .stream()
                .map(CREATE_TRIGGER::createDomainObject)
                .map(CreateTrigger.class::cast)
                .collect(toMap(s -> s.triggerName, s -> s));


        this.triggerAlterationsByName = statements.getOrDefault(ALTER_TRIGGER, Collections.emptyList())
                .stream()
                .map(ALTER_TRIGGER::createDomainObject)
                .map(AlterTrigger.class::cast)
                .collect(groupingBy(AlterTrigger::getTriggerName));


        this.mViewsByName = statements.getOrDefault(CREATE_MVIEW, Collections.emptyList())
                .stream()
                .map(CREATE_MVIEW::createDomainObject)
                .map(CreateMView.class::cast)
                .collect(toMap(s -> s.mViewName, s -> s));


        this.mViewCommentsByName = statements.getOrDefault(COMMENT_ON_MVIEW, Collections.emptyList())
                .stream()
                .map(COMMENT_ON_MVIEW::createDomainObject)
                .map(CommentOnMView.class::cast)
                .collect(toMap(s -> s.mViewName, s -> s));


        this.packagesByName = statements.getOrDefault(CREATE_PACKAGE_DEFINITION, Collections.emptyList())
                .stream()
                .map(CREATE_PACKAGE_DEFINITION::createDomainObject)
                .map(CreatePackage.class::cast)
                .collect(toMap(s -> s.packageName, s -> s));

        this.packageBodiesByName = statements.getOrDefault(CREATE_PACKAGE_BODY, Collections.emptyList())
                .stream()
                .map(CREATE_PACKAGE_BODY::createDomainObject)
                .map(CreatePackageBody.class::cast)
                .collect(toMap(s -> s.packageName, s -> s));


        this.tablesByName = statements.getOrDefault(CREATE_TABLE, Collections.emptyList())
                .stream()
                .map(CREATE_TABLE::createDomainObject)
                .map(CreateTable.class::cast)
                .collect(toMap(s -> s.tableName, s -> s));

        this.constraintsByTableName = statements.getOrDefault(ALTER_TABLE, Collections.emptyList())
                .stream()
                .map(ALTER_TABLE::createDomainObject)
                .map(AlterTable.class::cast)
                .collect(groupingBy(s -> s.tableName));


        this.tableCommentsByTableName = statements.getOrDefault(COMMENT_ON_TABLE, Collections.emptyList())
                .stream()
                .map(COMMENT_ON_TABLE::createDomainObject)
                .map(CommentOnTable.class::cast)
                .collect(toMap(s -> s.tableName, s -> s));


        this.columnCommentsByTableName = statements.getOrDefault(COMMENT_ON_TABLE_COLUMN, Collections.emptyList())
                .stream()
                .map(COMMENT_ON_TABLE_COLUMN::createDomainObject)
                .map(CommentOnTableColumn.class::cast)
                .collect(groupingBy(s -> s.tableName));


        this.dbLinksByName = statements.getOrDefault(CREATE_DB_LINK, Collections.emptyList())
                .stream()
                .map(CREATE_DB_LINK::createDomainObject)
                .map(CreateDbLink.class::cast)
                .collect(toMap(s -> s.dbLink, s -> s));


        this.viewsByName = statements.getOrDefault(CREATE_VIEW, Collections.emptyList())
                .stream()
                .map(CREATE_VIEW::createDomainObject)
                .map(CreateView.class::cast)
                .collect(toMap(s -> s.tableName, s -> s));

        if (log.isLoggable(Level.FINE)) {
            printGroupedStats();
        }
    }

    private void printGroupedStats() {
        long groupedCount = 0;
        groupedCount += notCategorizedStatements.size();
        groupedCount += grantsByObjName.values().stream().mapToLong(List::size).sum();
        groupedCount += synonymsByName.size();
        groupedCount += sequencesByName.size();
        groupedCount += functionsByName.size();
        groupedCount += proceduresByName.size();
        groupedCount += typesByName.size();
        groupedCount += typesBodiesByName.size();
        groupedCount += indexesByName.size();
        groupedCount += indexAlterationsByName.values().stream().mapToLong(List::size).sum();
        groupedCount += triggersByName.size();
        groupedCount += triggerAlterationsByName.values().stream().mapToLong(List::size).sum();
        groupedCount += mViewsByName.size();
        groupedCount += mViewCommentsByName.size();
        groupedCount += packagesByName.size();
        groupedCount += packageBodiesByName.size();
        groupedCount += tablesByName.size();
        groupedCount += constraintsByTableName.values().stream().mapToLong(List::size).sum();
        groupedCount += tableCommentsByTableName.size();
        groupedCount += columnCommentsByTableName.values().stream().mapToLong(List::size).sum();
        groupedCount += dbLinksByName.size();
        groupedCount += viewsByName.size();
        log.fine("Grouped statements count: " + groupedCount);
    }

    public <T> List<T> getList(java.util.function.Function<GroupedStatements, List<T>> fn) {
        var result = fn.apply(this);
        if (result == null) {
            return Collections.emptyList();
        }
        return result;
    }
}
