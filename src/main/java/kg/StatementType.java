package kg;

import kg.antlr.plsql.PlSqlParser;
import kg.statements.*;
import kg.statements.executables.CreateFunction;
import kg.statements.executables.CreateProcedure;
import kg.statements.executables.CreateType;
import kg.statements.executables.CreateTypeBody;
import kg.statements.index.AlterIndex;
import kg.statements.index.CreateIndex;
import kg.statements.mview.CommentOnMView;
import kg.statements.pkg.CreatePackage;
import kg.statements.pkg.CreatePackageBody;
import kg.statements.table.AlterTable;
import kg.statements.table.CommentOnTable;
import kg.statements.table.CommentOnTableColumn;
import kg.statements.table.CreateTable;
import kg.statements.trigger.AlterTrigger;
import kg.statements.trigger.CreateTrigger;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Arrays;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static kg.antlr.plsql.PlSqlParser.*;

public enum StatementType {

    CREATE_DB_LINK(RULE_create_database_link) {
        @Override
        public BaseStatement createDomainObject(PlSqlParser.Complete_statementContext s) {
            return new CreateDbLink(s);
        }
    },

    CREATE_TABLE(RULE_create_table) {
        @Override
        public BaseStatement createDomainObject(PlSqlParser.Complete_statementContext s) {
            return new CreateTable(s);
        }
    }, ALTER_TABLE(RULE_alter_table) {
        @Override
        public BaseStatement createDomainObject(Complete_statementContext s) {
            return new AlterTable(s);
        }
    },
    COMMENT_ON_TABLE(RULE_comment_on_table) {
        @Override
        public BaseStatement createDomainObject(Complete_statementContext s) {
            return new CommentOnTable(s);
        }
    }, COMMENT_ON_TABLE_COLUMN(RULE_comment_on_column) {
        @Override
        public BaseStatement createDomainObject(Complete_statementContext s) {
            return new CommentOnTableColumn(s);
        }
    },

    CREATE_INDEX(RULE_create_index) {
        @Override
        public BaseStatement createDomainObject(Complete_statementContext s) {
            return new CreateIndex(s);
        }
    }, ALTER_INDEX(RULE_alter_index) {
        @Override
        public BaseStatement createDomainObject(Complete_statementContext s) {
            return new AlterIndex(s);
        }
    },
    CREATE_TRIGGER(RULE_create_trigger) {
        @Override
        public BaseStatement createDomainObject(Complete_statementContext s) {
            return new CreateTrigger(s);
        }
    }, ALTER_TRIGGER(RULE_alter_trigger) {
        @Override
        public BaseStatement createDomainObject(Complete_statementContext s) {
            return new AlterTrigger(s);
        }
    },

    CREATE_VIEW(RULE_create_view) {
        @Override
        public BaseStatement createDomainObject(Complete_statementContext s) {
            return new CreateView(s);
        }
    }, CREATE_MVIEW(RULE_create_materialized_view) {
        @Override
        public BaseStatement createDomainObject(Complete_statementContext s) {
            return new CreateView(s);
        }
    }, COMMENT_ON_MVIEW(RULE_comment_on_mview) {
        @Override
        public BaseStatement createDomainObject(Complete_statementContext s) {
            return new CommentOnMView(s);
        }
    },

    CREATE_PACKAGE_DEFINITION(RULE_create_package) {
        @Override
        public BaseStatement createDomainObject(Complete_statementContext s) {
            return new CreatePackage(s);
        }
    }, CREATE_PACKAGE_BODY(RULE_create_package_body) {
        @Override
        public BaseStatement createDomainObject(Complete_statementContext s) {
            return new CreatePackageBody(s);
        }
    },
    CREATE_FUNCTION_BODY(RULE_create_function_body) {
        @Override
        public BaseStatement createDomainObject(Complete_statementContext s) {
            return new CreateFunction(s);
        }
    }, CREATE_PROCEDURE_BODY(RULE_create_procedure_body) {
        @Override
        public BaseStatement createDomainObject(Complete_statementContext s) {
            return new CreateProcedure(s);
        }
    },
    CREATE_TYPE(RULE_create_type) {
        @Override
        public BaseStatement createDomainObject(Complete_statementContext s) {
            return new CreateType(s);
        }
    }, CREATE_TYPE_BODY(RULE_create_type_body) {
        @Override
        public BaseStatement createDomainObject(Complete_statementContext s) {
            return new CreateTypeBody(s);
        }
    },

    GRANT(RULE_grant_statement) {
        @Override
        public BaseStatement createDomainObject(Complete_statementContext s) {
            return new GrantStatement(s);
        }
    }, CREATE_SYNONYM(RULE_create_synonym) {
        @Override
        public BaseStatement createDomainObject(Complete_statementContext s) {
            return new CreateSynonym(s);
        }
    }, CREATE_SEQUENCE(RULE_create_sequence) {
        @Override
        public BaseStatement createDomainObject(Complete_statementContext s) {
            return new CreateSequence(s);
        }
    }, OTHER(-1) {
        @Override
        public BaseStatement createDomainObject(Complete_statementContext s) {
            return new NotCategorizedStatement(s);
        }
    };

    private static final Map<Integer, StatementType> map = Arrays
            .stream(StatementType.values())
            .collect(toMap(StatementType::getRuleIndex, t -> t));

    private final int ruleIndex;

    StatementType(int ruleIndex) {
        this.ruleIndex = ruleIndex;
    }

    public int getRuleIndex() {
        return ruleIndex;
    }

    public static StatementType type(PlSqlParser.Complete_statementContext cs) {
        if (cs.children.get(0) instanceof Unit_statementContext) {
            int specificUnitStatementRuleIndex = ((ParserRuleContext) ((Unit_statementContext) cs.children.get(0)).children.get(0)).getRuleIndex();
            return map.getOrDefault(specificUnitStatementRuleIndex, OTHER);
        }
        return OTHER;
    }

    public abstract BaseStatement createDomainObject(PlSqlParser.Complete_statementContext s);
}
