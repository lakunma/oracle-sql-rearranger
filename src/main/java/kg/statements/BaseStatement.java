package kg.statements;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Objects;

public abstract class BaseStatement {

    private final ParserRuleContext antlrCtx;

    protected BaseStatement(ParserRuleContext antlrCtx) {
        this.antlrCtx = Objects.requireNonNull(antlrCtx);
    }

    public ParserRuleContext getAntlrCtx() {
        return antlrCtx;
    }

    public String getParsedText() {
        return antlrCtx.getText();
    }
}
