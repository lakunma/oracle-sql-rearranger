package kg.dump;

import kg.statements.BaseStatement;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class DumpToOrderedStatements {
    public static List<ParserRuleContext> convert(Dump dump) {
        var returnList = new ArrayList<ParserRuleContext>();

        returnList.addAll(dump.dbLinks.values().stream().map(BaseStatement::getAntlrCtx).collect(toList()));
        returnList.addAll(dump.synonyms.values().stream().map(BaseStatement::getAntlrCtx).collect(toList()));

        dump.sequences.values().forEach(s -> {
            returnList.add(s.createStatement.getAntlrCtx());
            returnList.addAll(s.grants.stream().map(BaseStatement::getAntlrCtx).collect(toList()));
        });

        dump.types.values().forEach(t -> {
            returnList.add(t.declaration.getAntlrCtx());
            if (t.body != null) {
                returnList.add(t.body.getAntlrCtx());
            }
            returnList.addAll(t.grants.stream().map(BaseStatement::getAntlrCtx).collect(toList()));
        });

        dump.functions.values().forEach(f -> {
            returnList.add(f.createStatement.getAntlrCtx());
            returnList.addAll(f.grants.stream().map(BaseStatement::getAntlrCtx).collect(toList()));
        });

        dump.procedures.values().forEach(p -> {
            returnList.add(p.createStatement.getAntlrCtx());
            returnList.addAll(p.grants.stream().map(BaseStatement::getAntlrCtx).collect(toList()));
        });

        dump.packages.values().forEach(p -> {
            returnList.add(p.definition.getAntlrCtx());
            if (p.body != null) {
                returnList.add(p.body.getAntlrCtx());
            }
            returnList.addAll(p.grants.stream().map(BaseStatement::getAntlrCtx).collect(toList()));
        });

        dump.tables.values().forEach(t -> {
            returnList.add(t.createStatement.getAntlrCtx());
            if (t.tableComment != null) {
                returnList.add(t.tableComment.getAntlrCtx());
            }
            returnList.addAll(t.columnComments.stream().map(BaseStatement::getAntlrCtx).collect(toList()));
            returnList.addAll(t.grants.stream().map(BaseStatement::getAntlrCtx).collect(toList()));
            returnList.addAll(t.constraints.stream().map(BaseStatement::getAntlrCtx).collect(toList()));
            t.indexes.forEach(i -> {
                returnList.add(i.createStatement.getAntlrCtx());
                returnList.addAll(i.alterations.stream().map(BaseStatement::getAntlrCtx).collect(toList()));
            });
            t.triggers.forEach(tr -> {
                returnList.add(tr.createStatement.getAntlrCtx());
                returnList.addAll(tr.alterations.stream().map(BaseStatement::getAntlrCtx).collect(toList()));
            });
        });

        dump.views.values().forEach(v -> {
            returnList.add(v.createStatement.getAntlrCtx());
            if (v.viewComment != null) {
                returnList.add(v.viewComment.getAntlrCtx());
            }
            returnList.addAll(v.columnComments.stream().map(BaseStatement::getAntlrCtx).collect(toList()));
            returnList.addAll(v.grants.stream().map(BaseStatement::getAntlrCtx).collect(toList()));
        });

        dump.mViews.values().forEach(mv -> {
            returnList.add(mv.createStatement.getAntlrCtx());
            if (mv.mViewComment != null) {
                returnList.add(mv.mViewComment.getAntlrCtx());
            }
            returnList.addAll(mv.grants.stream().map(BaseStatement::getAntlrCtx).collect(toList()));
        });

        returnList.addAll(dump.otherStatements);

        return returnList;
    }
}
