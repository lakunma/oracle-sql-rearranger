package kg;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

public class DumpWriter {

    public static final String LINE_SEPARATOR = System.lineSeparator();

    private final CharStream originalCs;
    private final List<ParserRuleContext> statementsList;

    public DumpWriter(CharStream originalCs, List<ParserRuleContext> statementsList) {
        this.originalCs = originalCs;
        this.statementsList = statementsList;
    }

    public void write(String location) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(location), CREATE_NEW)) {
            for (ParserRuleContext c : statementsList) {
                writer.write(originalCs.getText(new Interval(c.start.getStartIndex(), c.stop.getStopIndex())) + ';');
                writer.write(LINE_SEPARATOR + LINE_SEPARATOR);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
