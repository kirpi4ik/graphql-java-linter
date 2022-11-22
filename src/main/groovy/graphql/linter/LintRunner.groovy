package graphql.linter

import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeDefinitionRegistry

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Stream

public class LintRunner {
    def yamlConfig

    public LintRunner(URI yamlConfiguration) {
        assert yamlConfiguration != null
        yamlConfig = new YamlConfiguration(yamlConfiguration).config
    }

    void run() {
        run(null)
    }

    void run(ResultHandler resultHandler) {
        SchemaParser schemaParser = new SchemaParser();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        TypeDefinitionRegistry tdr = new TypeDefinitionRegistry();
        try (Stream<Path> paths = Files.walk(Paths.get(yamlConfig.schema['local']['uri']))) {
            paths.filter(Files::isRegularFile).map(Files::readString).each {
                tdr.merge(schemaParser.parse(it))
            }
        }

        def schema = schemaGenerator.makeExecutableSchema(tdr, RuntimeWiring.newRuntimeWiring().wiringFactory(new DummyWiringFactory()).build())
        def executionEnvironment = new ExecutionEnvironment(config: yamlConfig, schema: schema)
        new ValidationExecution().validate(executionEnvironment, resultHandler ?: new BasicResultHandler())
    }
}
