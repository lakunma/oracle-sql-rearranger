package kg.statements.mview;

import kg.antlr.plsql.PlSqlParser;
import kg.statements.CategorizedStatement;

public class CommentOnMView extends CategorizedStatement<PlSqlParser.Comment_on_mviewContext> {

    public final String mViewName;

    public CommentOnMView(PlSqlParser.Complete_statementContext ctx) {
        super(ctx);
        mViewName = getSpecificStatement().tableview_name().getText();
    }
}
