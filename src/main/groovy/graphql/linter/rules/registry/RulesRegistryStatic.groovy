package graphql.linter.rules.registry

import graphql.linter.rules.DefinedTypesAreUsed
import graphql.linter.rules.FieldNameFirstLowercase
import graphql.linter.rules.LintRule
import graphql.linter.rules.types.FieldRule
import graphql.linter.rules.types.TypeRule

class RulesRegistryStatic implements Registry {
    private Set<LintRule> fieldRules = new HashSet<>()
    private Set<LintRule> typeRules = new HashSet<>()

    RulesRegistryStatic() {
        register(FieldNameFirstLowercase.class)
        register(DefinedTypesAreUsed.class)
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
