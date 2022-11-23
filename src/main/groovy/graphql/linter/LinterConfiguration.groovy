package graphql.linter

class LinterConfiguration {
    FailLevel level = FailLevel.ERROR
    boolean failWhenExceedWarningMax = false
    def schema
    def rules = [String: [:]]
    private registry = [:]

}
