package graphql.linter

import graphql.linter.registry.RulesRegistry
import graphql.schema.*
import groovy.time.TimeCategory

class ValidationExecution {
    RulesRegistry rulesRegistry

    ExecutionContext validate(ExecutionEnvironment executionEnvironment, ResultHandler resultHandler) {
        assert executionEnvironment.config != null
        assert executionEnvironment.schema != null
        rulesRegistry = new RulesRegistry(executionEnvironment.config)
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

    def validateType(ExecutionEnvironment executionEnvironment, ExecutionContext context, GraphQLNamedType graphQLNamedType) {
        rulesRegistry.getTypeRuleSet().each { rule ->
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
            rulesRegistry.getFieldRuleSet().each { rule ->
                if (field instanceof GraphQLFieldDefinition) {
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

}
