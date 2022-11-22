package graphql.linter

interface ResultHandler {
    def handle(executionEnvironment, context)
}