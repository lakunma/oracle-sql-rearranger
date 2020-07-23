package kg.statements.trigger;

import kg.antlr.plsql.PlSqlParser;
import kg.statements.CategorizedStatement;

import java.util.Comparator;

public class CreateTrigger
        extends CategorizedStatement<PlSqlParser.Create_triggerContext>
        implements Comparable<CreateTrigger> {
    public final String tableName;
    public final String triggerName;
    public final boolean isDmlTrigger;

    public CreateTrigger(PlSqlParser.Complete_statementContext createStatement) {
        super(createStatement);
        PlSqlParser.Dml_event_clauseContext dmlEventClause = null;
        if (getSpecificStatement().simple_dml_trigger() != null) {
            dmlEventClause = getSpecificStatement().simple_dml_trigger().dml_event_clause();
        } else if (getSpecificStatement().compound_dml_trigger() != null) {
            dmlEventClause = getSpecificStatement().compound_dml_trigger().dml_event_clause();
        }
        if (dmlEventClause != null) {
            this.isDmlTrigger = true;
            this.tableName = formatTableName(dmlEventClause.tableview_name().getText());
        } else {
            this.isDmlTrigger = false;
            this.tableName = null;
        }
        this.triggerName = getSpecificStatement().trigger_name().getText();
    }

    private String formatTableName(String tableName) {
        tableName = tableName.toUpperCase();
        if (!tableName.startsWith("\"")) {
            tableName = '"' + tableName.replaceAll("\\.", "\".\"") + '"';
        }
        return tableName;
    }

    public String getTableName() {
        return tableName;
    }

    @Override
    public int compareTo(CreateTrigger o) {
        Comparator<CreateTrigger> cmp = Comparator.comparing(CreateTrigger::getTableName).thenComparing(c -> c.triggerName);
        return cmp.compare(this, o);
    }
}
