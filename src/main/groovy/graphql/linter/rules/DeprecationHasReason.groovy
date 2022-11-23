package graphql.linter.rules


import graphql.linter.registry.types.FieldRule
import graphql.linter.registry.types.TypeRule
import graphql.schema.GraphQLArgument
import graphql.schema.GraphQLDirective
import graphql.schema.GraphQLNamedSchemaElement

@FieldRule
class DeprecationHasReason extends LintRule {


    public static final String defaultReasonMsg = "No longer supported"

    @Override
    def validate(GraphQLNamedSchemaElement parent, GraphQLNamedSchemaElement node) {
        node.children.each { schemaElement ->
            if (schemaElement instanceof GraphQLDirective) {
                if (schemaElement.name == "deprecated" && !hasReason(schemaElement)) {
                    fail(parent, node, "Directive for `${parent.name}.${node.name}` missing deprecation reason.")
                }
            }
            if (schemaElement instanceof GraphQLArgument) {
                schemaElement.children.each { directive ->
                    if (directive instanceof GraphQLDirective) {
                        if (directive.name == "deprecated" && !hasReason(directive)) {
                            fail(parent, node, "Directive for `${parent.name}.${node.name}(${schemaElement.name})` missing deprecation reason.")
                        }
                    }
                }
            }
        }
    }

    def hasReason(directive) {
        return directive.arguments.find { it.name == "reason" && it.value.value.value != defaultReasonMsg } != null
    }
}
