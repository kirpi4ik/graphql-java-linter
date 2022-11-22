package graphql.linter

import graphql.schema.TypeResolver
import graphql.schema.idl.InterfaceWiringEnvironment
import graphql.schema.idl.UnionWiringEnvironment
import graphql.schema.idl.WiringFactory

class DummyWiringFactory implements WiringFactory {
    @Override
    boolean providesTypeResolver(InterfaceWiringEnvironment environment) {
        return true
    }
    default boolean providesTypeResolver(UnionWiringEnvironment environment) {
        return true;
    }
    default TypeResolver getTypeResolver(InterfaceWiringEnvironment environment) {
        return new DummyTypeResolver()
    }
    default TypeResolver getTypeResolver(UnionWiringEnvironment environment) {
        return new DummyTypeResolver()
    }
}
