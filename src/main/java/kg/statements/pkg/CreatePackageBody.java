package kg.statements.pkg;

import kg.antlr.plsql.PlSqlParser;
import kg.statements.CategorizedStatement;

public class CreatePackageBody extends CategorizedStatement<PlSqlParser.Create_package_bodyContext> {
    public final String packageName;

    public CreatePackageBody(PlSqlParser.Complete_statementContext body) {
        super(body);
        var statement = getSpecificStatement();
        packageName = statement.schema_object_name().getText() + "." + statement.package_name(0).getText();
    }
}
