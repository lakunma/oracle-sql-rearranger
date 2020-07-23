package kg.antlr.plsql;

import kg.antlr.CaseChangingCharStream;
import kg.antlr.ThrowingErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;

import java.io.IOException;
import java.nio.file.Paths;

public class TestUtils {
    static ParserRuleContext parseSqlScript(String filePath) {
        CharStream cs;
        try {
            cs = CharStreams.fromPath(Paths.get(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PlSqlLexer lexer = new PlSqlLexer(new CaseChangingCharStream(cs, true));
        CommonTokenStream ts = new CommonTokenStream(lexer);
        PlSqlParser parser = new PlSqlParser(ts);
        parser.removeErrorListeners();
        parser.addErrorListener(new ThrowingErrorListener());
        return parser.sql_script();
    }
}
