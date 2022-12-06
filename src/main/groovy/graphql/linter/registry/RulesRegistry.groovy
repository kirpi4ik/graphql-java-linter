package graphql.linter.registry

import graphql.linter.LinterConfiguration
import graphql.linter.rules.LintRule

import static java.util.stream.Collectors.toList

class RulesRegistry implements Registry {
    private Set<LintRule> fieldRules = new HashSet<>()
    private Set<LintRule> typeRules = new HashSet<>()

    RulesRegistry(LinterConfiguration config) {
        def registryDynamic = new RulesRegistryDynamic(config)
        fieldRules.addAll registryDynamic.fieldRuleSet
        typeRules.addAll registryDynamic.typeRuleSet
        //Allow only one rule with the same name,
        //If there is already defined a DSL rule with same name, static rule will be ignored(overwritten)
        def registryStatic = new RulesRegistryStatic()
        if (registryStatic.fieldRuleSet.stream().anyMatch(fieldRules::contains)) {
            println("WARNING: Duplicated field rules found: ${registryStatic.fieldRuleSet.stream().filter(fieldRules::contains).map(rule -> rule.name()).collect(toList())}")
        }
        fieldRules.addAll registryStatic.fieldRuleSet
        if (registryStatic.typeRuleSet.stream().anyMatch(typeRules::contains)) {
            println("WARNING: Duplicated type rules found: ${registryStatic.typeRuleSet.stream().filter(typeRules::contains).map(rule -> rule.name()).collect(toList())}")
        }
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
