package graphql.linter.registry

import graphql.linter.registry.types.FieldRule
import graphql.linter.registry.types.TypeRule
import graphql.linter.rules.*

class RulesRegistryStatic implements Registry {
    private Set<LintRule> fieldRules = new HashSet<>()
    private Set<LintRule> typeRules = new HashSet<>()

    RulesRegistryStatic() {
        register(FieldNameFirstLowercase.class)
        register(DefinedTypesAreUsed.class)
        register(ArgumentsHasDescriptions.class)
        register(DeprecationHasReason.class)
        register(EnumValueAllCaps.class)
        register(TypeNameFirstCaps.class)
    }

    def register(Class<?> rule) {
        rule.getAnnotations().each { typeAnnotation ->
            switch (typeAnnotation.annotationType()) {
                case FieldRule.class:
                    fieldRules.add((rule as Class<LintRule>).getDeclaredConstructor().newInstance())
                    break;
                case TypeRule.class:
                    typeRules.add((rule as Class<LintRule>).getDeclaredConstructor().newInstance())
                    break;
            }
        }
    }

    Set<LintRule> getFieldRuleSet() {
        fieldRules
    }

    Set<LintRule> getTypeRuleSet() {
        typeRules
    }
}
