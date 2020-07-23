package kg;

import kg.antlr.CaseChangingCharStream;
import kg.antlr.ThrowingErrorListener;
import kg.antlr.plsql.PlSqlLexer;
import kg.antlr.plsql.PlSqlParser;
import kg.dump.DumpCreator;
import kg.dump.DumpToOrderedStatements;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class.getName());

    static String outFileName(String inFileName) {
        int lastDotIndex = inFileName.lastIndexOf('.');
        return inFileName.substring(0, lastDotIndex) + "_sorted" + inFileName.substring(lastDotIndex);
    }

    public static void main(String[] args) throws IOException {
        if (Arrays.asList(args).contains("--debug")) {
            kg.Logger.setLogLevel(Level.FINEST);
        }

        if (args.length == 0) {
            System.out.println("Usage: app <path-to-sql-file>");
            System.exit(1);
        }
        var inFile = args[0];
        var path = Paths.get(inFile);
        if (!Files.exists(path)) {
            System.err.println("Input file does not exist" + inFile);
        }
        CharStream cs = CharStreams.fromPath(Paths.get(inFile));
        cs = new CaseChangingCharStream(cs, true);
        PlSqlLexer lexer = new PlSqlLexer(cs);
        CommonTokenStream ts = new CommonTokenStream(lexer);
        PlSqlParser parser = new PlSqlParser(ts);
        parser.setVersion12(false);
        parser.removeErrorListeners();
        parser.addErrorListener(new ThrowingErrorListener());
        var sclScript = parser.sql_script();
        var parsedStatements = sclScript.children
                .stream()
                .filter(c -> !(c instanceof TerminalNode))
                .map(ParserRuleContext.class::cast)
                .collect(toList());
        log.info("There are " + parsedStatements.size() + " statements parsed");
        var groupedStatements = parsedStatements.stream()
                .map(PlSqlParser.Complete_statementContext.class::cast)
                .collect(groupingBy(StatementType::type));

        var dump = new DumpCreator(groupedStatements).getDump();
        var set = new HashSet<>(parsedStatements);
        List<ParserRuleContext> orderedStatementsList = DumpToOrderedStatements.convert(dump);
        set.removeAll(orderedStatementsList);
        if (!set.isEmpty()) {
            log.warning("There are " + set.size() + " elements lost: ");
            log.warning(set.stream().map(RuleContext::getText).collect(Collectors.joining(System.lineSeparator())));
        }
        log.info("There are " + orderedStatementsList.size() + " statements to print");
        new DumpWriter(cs, orderedStatementsList).write(outFileName(inFile));
    }
}
