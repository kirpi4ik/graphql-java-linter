package graphql.linter.rules

import graphql.linter.rules.types.TypeRule
import graphql.schema.GraphQLInputObjectType
import graphql.schema.GraphQLNamedSchemaElement
import graphql.schema.GraphQLObjectType
import graphql.schema.GraphQLScalarType

@TypeRule
class DefinedTypesAreUsed extends LintRule {
    @Override
    def validate(GraphQLNamedSchemaElement parent, GraphQLNamedSchemaElement node) {
        def ignoredTypes = ['Query', 'Mutation', 'Subscription'];
        def referencedTypes = [String: []]
        executionEnvironment.schema.typeMap.each { typeEntry ->
            def type = typeEntry.value
            if (!type.name.startsWith("_")) {
                if (type instanceof GraphQLObjectType || type instanceof GraphQLInputObjectType) {
                    type.getFields().each { fieldDef ->
                        def fieldType = fieldDef.type
                        while (fieldType.hasProperty('originalWrappedType')) {
                            fieldType = fieldType.originalWrappedType
                        }
                        if (!referencedTypes[fieldType.name]) {
                            referencedTypes[fieldType.name] = []
                        }
                        referencedTypes[fieldType.name] << fieldDef

                        if (type instanceof GraphQLObjectType && (fieldType instanceof GraphQLObjectType || fieldType instanceof GraphQLScalarType)) {
                            fieldDef.arguments.each { argument ->
                                def argType = argument.type
                                while (argType.hasProperty('originalWrappedType')) {
                                    argType = argType.originalWrappedType
                                }

                                if (!referencedTypes[argType.name]) {
                                    referencedTypes[argType.name] = []
                                }
                                referencedTypes[argType.name] << fieldDef

                            }
                        }
                    }
                }
            }
        }
        if (!ignoredTypes.contains(node.name) && !referencedTypes.containsKey(node.name)) {
            fail(parent, node, "GraphQL type `${node.name}` was decalred but not used.")
        }
    }
}
