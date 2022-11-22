package graphql.linter.exception

class RuleValidationError extends RuntimeException {
    RuleValidationError(failedRules) {
        super(String.valueOf(failedRules) + ".\n You can disable rule validation using configuration Eg: `disable: true` or suppress exception by using using `level: WARN` .")
    }
}
