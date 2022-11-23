package graphql.linter.registry

import graphql.linter.registry.types.RuleType
import graphql.linter.rules.LintRule
import graphql.schema.GraphQLNamedSchemaElement

class RuleDSL extends LintRule {

    private RuleType[] types
    private Closure validateClosure

    public void rule(Collection<String> types, Closure validateClosure) {
        this.validateClosure = validateClosure
        this.types = types.collect {
            it as RuleType
        }
    }

    @Override
    def validate(GraphQLNamedSchemaElement parent, GraphQLNamedSchemaElement node) {
        validateClosure.setProperty("node", node)
        validateClosure.setProperty("parent", parent)
        validateClosure.run()
    }
}
