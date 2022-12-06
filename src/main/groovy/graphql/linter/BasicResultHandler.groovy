package graphql.linter

import graphql.linter.exception.RuleValidationError

class BasicResultHandler implements ResultHandler {
    def handle(executionEnvironment, context) {
        boolean hasErrors = context.errors.any { error -> error.value.any { it.level == FailLevel.ERROR } }
        if (hasErrors) {
            def failedRules = context.errors.findAll { error -> error.value.any { it.level == FailLevel.ERROR } }
            throw new RuleValidationError(failedRules)
        }
        boolean hasWarnings = context.errors.any { error -> error.value.any { it.level == FailLevel.WARN } }
        int rowLength = 160
        if (hasWarnings) {
            printf "${'#' * rowLength}%n"
            printf "${'#' * (rowLength / 2 - 5)} FAILURES ${'#' * (rowLength / 2 - 5)}%n"
            printf "${'#' * rowLength}%n"
            def failedRules = context.errors.findAll { error -> error.value.any { it.level == FailLevel.WARN } }
            failedRules.each { ruleName, ruleWarn ->
                printf "#${'=' * (rowLength-2)}#%n"
                printf "# * %-35s  [ %-3d failures][%-6d runs] [%-3.3f sec/rule]${' ' * (rowLength - 87)}#%n", ruleName, ruleWarn.size(), context.execution[ruleName]['count'], context.execution[ruleName]['time']
                ruleWarn.each { error -> printf("#            +  %-${rowLength - 19}s  #%n", error.message)
                }
                def failWhenExceed = executionEnvironment.config['rules'][ruleName as String] ?['failWhenExceed']
                if (executionEnvironment.config['failWhenExceedWarningMax'] && (failWhenExceed && ruleWarn.size() > failWhenExceed)) {
                    throw new RuleValidationError("[${ruleName}] Exceeded number of failures. ${ruleWarn.size()} > ${failWhenExceed} ")
                }
            }
            printf "${'#' * rowLength}%n"
        }
    }
}
