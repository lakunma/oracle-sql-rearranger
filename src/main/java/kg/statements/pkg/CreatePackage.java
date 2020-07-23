package kg.statements.pkg;

import kg.antlr.plsql.PlSqlParser;
import kg.statements.CategorizedStatement;

public class CreatePackage extends CategorizedStatement<PlSqlParser.Create_packageContext> {
    public final String packageName;

    public CreatePackage(PlSqlParser.Complete_statementContext body) {
        super(body);
        var statement = getSpecificStatement();
        packageName = statement.schema_object_name().getText() + "." + statement.package_name(0).getText();
    }
}
