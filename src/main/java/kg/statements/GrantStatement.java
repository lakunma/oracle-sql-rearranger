package kg.statements;

import kg.antlr.plsql.PlSqlParser;


public class GrantStatement
        extends CategorizedStatement<PlSqlParser.Grant_statementContext>
        implements Comparable<GrantStatement> {

    public final String grantObjName;

    public GrantStatement(PlSqlParser.Complete_statementContext createStatement) {
        super(createStatement);
        grantObjName = getSpecificStatement().grant_object_name().getText();
    }

    @Override
    public int compareTo(GrantStatement o) {
        return this.getAntlrCtx().getText().compareTo(o.getParsedText());
    }
}
