package graphql.linter.rules.registry

import graphql.linter.LinterConfiguration
import graphql.linter.rules.LintRule
import graphql.linter.rules.RuleDSL
import graphql.linter.rules.RuleType
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.ImportCustomizer

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Stream

class RulesRegistryDynamic implements Registry {
    private LinterConfiguration config
    private Set<LintRule> fieldRules = new HashSet<>()
    private Set<LintRule> typeRules = new HashSet<>()

    RulesRegistryDynamic(LinterConfiguration config) {
        this.config = config

        def classloader = this.class.getClassLoader();
        def compilerConfiguration = new CompilerConfiguration();
        compilerConfiguration.setScriptBaseClass(DelegatingScript.class.getName());

        def ic = new ImportCustomizer();
        ic.addImports(ServiceUtils.class.getName());
        compilerConfiguration.addCompilationCustomizers(ic);

        Binding binding = new Binding();
        binding.setVariable("context", new RuleContext());
        for (def prop : properties.entrySet()) {
            binding.setVariable(prop.getKey() as String, prop.getValue());
        }

        def gs = new GroovyShell(classloader, binding, compilerConfiguration);
        try (Stream<Path> paths = Files.walk(Paths.get(config.registry['dsl']['uri']))) {
            paths.filter(Files::isRegularFile).map(p -> new MapEntry((p.getFileName()), Files.readString(p))).each { scriptFile ->
                DelegatingScript ruleScript = (DelegatingScript) gs.parse(scriptFile.value as String)
                ruleScript.setDelegate(new RuleDSL(name: (scriptFile.key as String).replaceAll("(?<!^)[.][^.]*\$","")));
                ruleScript.run();
                if (ruleScript.delegate.types.contains(RuleType.FIELD)) {
                    fieldRules << (ruleScript.delegate as LintRule)
                } else if (ruleScript.delegate.contains(RuleType.TYPE)) {
                    typeRules << (ruleScript.delegate as LintRule)
                }
            }
        }
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
