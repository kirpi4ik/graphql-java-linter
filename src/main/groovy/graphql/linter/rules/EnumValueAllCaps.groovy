package graphql.linter.rules


import graphql.linter.registry.types.TypeRule
import graphql.schema.GraphQLEnumType
import graphql.schema.GraphQLNamedSchemaElement

@TypeRule
class EnumValueAllCaps extends LintRule {

    @Override
    def validate(GraphQLNamedSchemaElement parent, GraphQLNamedSchemaElement node) {
        if (node instanceof GraphQLEnumType && node.children.find({ it.name ==~ /[a-z]*/ })) {
            fail(parent, node, "Enum value `${node.name}` contains lowercase")
        }
    }
}
