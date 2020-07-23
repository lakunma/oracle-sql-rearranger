package kg.statements;

import kg.antlr.plsql.PlSqlParser;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class CategorizedStatement<T extends ParserRuleContext> extends BaseStatement {

    protected CategorizedStatement(PlSqlParser.Complete_statementContext ctx) {
        super(ctx);
    }

    @SuppressWarnings("unchecked")
    protected T getSpecificStatement() {
        return (T) (((PlSqlParser.Unit_statementContext) getAntlrCtx().children.get(0)).children.get(0));
    }
}
