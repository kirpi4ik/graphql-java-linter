package graphql.linter.registry

import graphql.linter.LinterConfiguration
import graphql.linter.rules.LintRule

class RulesRegistry implements Registry {
    private Set<LintRule> fieldRules = new HashSet<>()
    private Set<LintRule> typeRules = new HashSet<>()

    RulesRegistry(LinterConfiguration config) {
        def registryDynamic = new RulesRegistryDynamic(config)
        fieldRules.addAll registryDynamic.fieldRuleSet
        typeRules.addAll registryDynamic.typeRuleSet
        def registryStatic = new RulesRegistryStatic()
        fieldRules.addAll registryStatic.fieldRuleSet
        typeRules.addAll registryStatic.typeRuleSet
    }

    @Override
    Set<LintRule> getFieldRuleSet() {
        return fieldRules
    }

    @Override
    Set<LintRule> getTypeRuleSet() {
        return typeRules
    }
}
