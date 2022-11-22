package graphql.linter

import graphql.linter.exception.RuleValidationError
import graphql.schema.*
import groovy.time.TimeCategory

class ValidationExecution {
    RulesRegistry rulesRegistry

    ValidationExecution() {
        this.rulesRegistry = loadRegistry()
    }

    ExecutionContext validate(ExecutionEnvironment executionEnvironment, ResultHandler resultHandler) {
        assert executionEnvironment.config != null
        assert executionEnvironment.schema != null
        ExecutionContext context = new ExecutionContext()
        executionEnvironment.schema.typeMap.entrySet().each {
            if (!it.key.startsWith("_")) {
                def type = it.value
                if (type instanceof GraphQLObjectType || type instanceof GraphQLInputObjectType || type instanceof GraphQLEnumType) {
                    validateType(executionEnvironment, context, type)
                }
                if (type instanceof GraphQLObjectType || type instanceof GraphQLInputObjectType) {
                    validateFields(executionEnvironment, context, type)
                }
            }
        }
        resultHandler.handle(executionEnvironment, context)

        return context
    }

    static def handleResult(executionEnvironment, context) {

    }

    def validateType(ExecutionEnvironment executionEnvironment, ExecutionContext context, GraphQLNamedType graphQLNamedType) {
        rulesRegistry.getTypeRuleSet().each {
            def rule = it.getDeclaredConstructor().newInstance()
            rule.setContext(context)
            rule.setExecutionEnvironment(executionEnvironment)
            if (!executionEnvironment.config['rules'][rule.name()] ?['disable']) {
                def start = new Date()
                rule.validate(null, graphQLNamedType)
                def stop = new Date()
                context.execution(rule.name(), 'time', TimeCategory.minus(stop, start).millis / 1000)
                context.inc(rule.name(), 'count')
            }
        }
    }

    def validateFields(ExecutionEnvironment executionEnvironment, ExecutionContext context, type) {
        type.fields.each { field ->
            rulesRegistry.getFieldRuleSet().each {
                if (field instanceof GraphQLFieldDefinition) {
                    def rule = it.getDeclaredConstructor().newInstance()
                    rule.setContext(context)
                    rule.setExecutionEnvironment(executionEnvironment)
                    if (!executionEnvironment.config['rules'][rule.name()] ?['disable']) {
                        def start = new Date()
                        rule.validate(type, field)
                        def stop = new Date()
                        context.execution(rule.name(), 'time', TimeCategory.minus(stop, start).millis / 1000)
                        context.inc(rule.name(), 'count')
                    }
                }
            }
        }
    }

    static RulesRegistry loadRegistry() {
        new RulesRegistry()
    }

}
