package graphql.linter.rules

import graphql.linter.ExecutionContext
import graphql.linter.ExecutionEnvironment
import graphql.linter.FailLevel
import graphql.schema.GraphQLNamedSchemaElement

abstract class LintRule {
    protected ExecutionContext context
    protected ExecutionEnvironment executionEnvironment

    LintRule() {

    }

    LintRule(ExecutionContext context) {
        this.context = context
    }

    def abstract validate(GraphQLNamedSchemaElement parent, GraphQLNamedSchemaElement node)

    String name() {
        return this.getClass().getSimpleName().replaceAll(/([A-Z]+)([A-Z][a-z])/, "\$1_\$2").replaceAll(/([a-z])([A-Z])/, "\$1_\$2").toLowerCase()
    }

    void setContext(ExecutionContext context) {
        this.context = context
    }

    void setExecutionEnvironment(ExecutionEnvironment executionEnvironment) {
        this.executionEnvironment = executionEnvironment
    }

    def fail(GraphQLNamedSchemaElement parent, GraphQLNamedSchemaElement node, String message) {
        context.reportError(name(),
                executionEnvironment.config.rules[name()] ?['level'] as FailLevel ?: executionEnvironment.config.level,
                " ${message}")
    }
}
