package kg.statements.mview;

import kg.antlr.plsql.PlSqlParser;
import kg.statements.CategorizedStatement;

public class CreateMView extends CategorizedStatement<PlSqlParser.Create_materialized_viewContext> {

    public final String mViewName;

    public CreateMView(PlSqlParser.Complete_statementContext ctx) {
        super(ctx);
        mViewName = getSpecificStatement().tableview_name().getText();
    }
}
