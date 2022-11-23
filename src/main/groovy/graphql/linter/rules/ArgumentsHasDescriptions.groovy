package graphql.linter.rules


import graphql.linter.registry.types.FieldRule
import graphql.schema.GraphQLArgument
import graphql.schema.GraphQLNamedSchemaElement

@FieldRule
class ArgumentsHasDescriptions extends LintRule {

    @Override
    def validate(GraphQLNamedSchemaElement parent, GraphQLNamedSchemaElement node) {
        node.children.each { schemaElement ->
            if (schemaElement instanceof GraphQLArgument) {
                if (schemaElement.description == null) {
                    fail(parent, node, "Argument `${parent.name}.${node.name}(${schemaElement.name})` missing description.")
                }
            }

        }
    }
}
