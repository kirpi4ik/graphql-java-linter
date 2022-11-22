package graphql.linter

class ExecutionContext {
    def public errors = [:]
    def public execution = [:]


    def reportError(String ruleName, FailLevel level, String errorMessage) {
        if (!errors[ruleName]) {
            errors[ruleName] = []
        }
        errors[ruleName] << [level: level, message: errorMessage]
    }

    def execution(ruleName, field, value) {
        if (!execution[ruleName]) {
            execution[ruleName] = [:]
        }
        execution[ruleName][field] = value
    }

    def inc(ruleName, field) {
        if (!execution[ruleName]) {
            execution[ruleName] = [:]
            execution[ruleName][field] = 0
        } else if (!execution[ruleName][field]) {
            execution[ruleName][field] = 0
        }
        execution[ruleName][field]++
    }
}
