package graphql.linter

import graphql.linter.rules.DefinedTypesAreUsed
import graphql.linter.rules.FieldNameFirstLowercase
import graphql.linter.rules.LintRule
import graphql.linter.rules.types.FieldRule
import graphql.linter.rules.types.TypeRule

class RulesRegistry {
    private Set<Class<LintRule>> fieldRules = new HashSet<>()
    private Set<Class<LintRule>> typeRules = new HashSet<>()

    RulesRegistry() {
        register(FieldNameFirstLowercase.class)
        register(DefinedTypesAreUsed.class)
    }

    def register(Class<?> rule) {
        rule.getAnnotations().each { typeAnnotation ->
            switch (typeAnnotation.annotationType()) {
                case FieldRule.class:
                    fieldRules.add(rule as Class<LintRule>)
                    break;
                case TypeRule.class:
                    typeRules.add(rule as Class<LintRule>)
                    break;
            }
        }
    }

    Set<Class<LintRule>> getFieldRuleSet() {
        fieldRules
    }

    Set<Class<LintRule>> getTypeRuleSet() {
        typeRules
    }
}
