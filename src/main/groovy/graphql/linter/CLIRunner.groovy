package graphql.linter


import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeDefinitionRegistry

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Stream

class CLIRunner {
    static void main(String[] args) {
        assert args.size() > 0
        def yamlConfig = new YamlConfiguration(args[0]).config

        SchemaParser schemaParser = new SchemaParser();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        TypeDefinitionRegistry tdr = new TypeDefinitionRegistry();
        try (Stream<Path> paths = Files.walk((Paths.get(yamlConfig.schema['local']['uri'])))) {
            paths.filter(Files::isRegularFile).map(Files::readString).each {
                tdr.merge(schemaParser.parse(it))
            }
        }

        def schema = schemaGenerator.makeExecutableSchema(tdr, RuntimeWiring.newRuntimeWiring().wiringFactory(new DummyWiringFactory()).build())
        def executionEnvironment = new ExecutionEnvironment(config: yamlConfig, schema: schema)
        def result = new ValidationExecution().validate(executionEnvironment, new BasicResultHandler())
    }
}
