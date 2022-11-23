package graphql.linter.rules.registry

import graphql.linter.YamlConfiguration
import graphql.linter.registry.RulesRegistryDynamic
import spock.lang.*

import java.nio.file.Paths


class RulesRegistryDynamicSpec extends Specification {
    def "Load dynamic DSL rules"() {
        when:
        RulesRegistryDynamic registryDynamic = new RulesRegistryDynamic(new YamlConfiguration(Paths.get("src", "test", "resources", "linter.yaml").toUri()).config)
        then:
        registryDynamic.fieldRuleSet.size() == 2
        registryDynamic.typeRuleSet.size() == 1
    }
}
