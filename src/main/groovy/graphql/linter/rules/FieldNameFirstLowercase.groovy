package graphql.linter.rules

import graphql.linter.rules.types.FieldRule
import graphql.schema.GraphQLNamedSchemaElement

@FieldRule
class FieldNameFirstLowercase extends LintRule {

    @Override
    def validate(GraphQLNamedSchemaElement parent, GraphQLNamedSchemaElement node) {
        if (node.name ==~ /^[A-Z].*/) {
            fail(parent, node, "The fieldname `${parent.name}.${node.name}` starts with uppercase.")
        }
    }
}
