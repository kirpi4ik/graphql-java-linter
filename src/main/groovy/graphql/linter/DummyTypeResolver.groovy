package graphql.linter

import graphql.TypeResolutionEnvironment
import graphql.schema.GraphQLObjectType
import graphql.schema.TypeResolver

class DummyTypeResolver implements TypeResolver {
    @Override
    GraphQLObjectType getType(TypeResolutionEnvironment env) {
        return null
    }
}
