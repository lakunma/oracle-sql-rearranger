package kg.statements;

import kg.antlr.plsql.PlSqlParser;

public class CreateSequence extends CategorizedStatement<PlSqlParser.Create_sequenceContext> {

    public final String sequenceName;

    public CreateSequence(PlSqlParser.Complete_statementContext ctx) {
        super(ctx);
        this.sequenceName = getSpecificStatement().sequence_name().getText();
    }
}
