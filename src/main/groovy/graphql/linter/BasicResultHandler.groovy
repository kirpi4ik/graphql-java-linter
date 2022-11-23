package graphql.linter

import graphql.linter.exception.RuleValidationError

class BasicResultHandler implements ResultHandler{
    def handle(executionEnvironment, context){
        boolean hasErrors = context.errors.any { error -> error.value.any { it.level == FailLevel.ERROR } }
        if (hasErrors) {
            def failedRules = context.errors.findAll { error -> error.value.any { it.level == FailLevel.ERROR } }
            throw new RuleValidationError(failedRules)
        }
        boolean hasWarnings = context.errors.any { error -> error.value.any { it.level == FailLevel.WARN } }
        if (hasWarnings) {
            printf("#######################################################################################################################%n")
            printf("###################################################    FAILURES   #####################################################%n")
            printf("#######################################################################################################################%n")
            def failedRules = context.errors.findAll { error -> error.value.any { it.level == FailLevel.WARN } }
            failedRules.each { ruleName, ruleWarn ->
                printf("#---------------------------------------------------------------------------------------------------------------------#%n")
                printf "# [ %-30s ] [ %-3d failures][%-6d runs] [%-4.3f sec/rule]                                    #%n", ruleName, ruleWarn.size(), context.execution[ruleName]['count'], context.execution[ruleName]['time']
                ruleWarn.each { error -> printf("#            +  %-100s  #%n", error.message)
                }
                def failWhenExceed = executionEnvironment.config['rules'][ruleName as String]?['failWhenExceed']
                if (executionEnvironment.config['failWhenExceedWarningMax'] && (failWhenExceed && ruleWarn.size() > failWhenExceed)) {
                    throw new RuleValidationError("[${ruleName}] Exceeded number of failures. ${ruleWarn.size()} > ${failWhenExceed} ")
                }
            }
            printf("#######################################################################################################################%n")
        }
    }
}
