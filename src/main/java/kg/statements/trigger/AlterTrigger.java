package kg.statements.trigger;

import kg.antlr.plsql.PlSqlParser;
import kg.statements.BaseStatement;
import kg.statements.CategorizedStatement;

import java.util.Comparator;

public class AlterTrigger
        extends CategorizedStatement<PlSqlParser.Alter_triggerContext>
        implements Comparable<AlterTrigger> {

    private final String triggerName;

    public String getTriggerName() {
        return triggerName;
    }

    public AlterTrigger(PlSqlParser.Complete_statementContext createStatement) {
        super(createStatement);
        triggerName = getSpecificStatement().alter_trigger_name.getText();
    }

    @Override
    public int compareTo(AlterTrigger o) {
        return Comparator
                .comparing(AlterTrigger::getTriggerName)
                .thenComparing(BaseStatement::getParsedText).compare(this, o);
    }
}
