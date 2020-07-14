package kg.antlr.plsql;

import org.antlr.v4.runtime.ParserRuleContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class Tests {
    @ParameterizedTest
    @MethodSource("testFilesPaths")
    public void tryToPare(String filePath) {
        ParserRuleContext us = TestUtils.parseSqlScript(filePath);
        Optional.ofNullable(us.exception).ifPresent(e -> {
            throw e;
        });
        System.out.println("Found " + us.children.size() + " statements in " + System.lineSeparator() + filePath);
    }

    static Stream<String> testFilesPaths() {
        return Arrays.stream(Objects.requireNonNull(new File("src/test/resources").listFiles(File::isDirectory)))
                .flatMap(d -> Arrays.stream(Objects.requireNonNull(d.listFiles((dir, name) -> name.endsWith(".sql")))))
                .map(File::getPath);
    }
}
