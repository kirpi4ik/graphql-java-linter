package graphql.linter

import spock.lang.Specification

import java.nio.file.Paths

class CLIRunnerTest extends Specification {
    def "Main"() {
        expect:
        CLIRunner.main(["src/test/resources/linter.yaml"] as String[])
    }
}
