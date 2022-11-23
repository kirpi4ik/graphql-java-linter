package graphql.linter.registry

import graphql.linter.rules.LintRule

interface Registry {
    Set<LintRule> getFieldRuleSet()

    Set<LintRule> getTypeRuleSet()
}