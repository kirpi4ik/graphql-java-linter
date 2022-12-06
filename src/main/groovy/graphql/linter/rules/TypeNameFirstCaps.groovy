package graphql.linter.rules

import graphql.linter.registry.types.TypeRule
import graphql.schema.GraphQLNamedSchemaElement

@TypeRule
class TypeNameFirstCaps extends LintRule {
    @Override
    def validate(GraphQLNamedSchemaElement parent, GraphQLNamedSchemaElement node) {
        if (!(node.name ==~ /^[A-Z].+/)) {
            fail(parent, node, "Type `${node.name}` has invalid name, it should start with caps ")
        }
    }
}
