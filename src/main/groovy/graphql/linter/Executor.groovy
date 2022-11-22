package graphql.linter


import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeDefinitionRegistry

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Stream

class Executor {
    static void main(String[] args) {
        assert args.size() > 0
        def yamlConfig = new YamlConfiguration(args.size() > 1 ? args[1] : null).config

        SchemaParser schemaParser = new SchemaParser();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        TypeDefinitionRegistry tdr = new TypeDefinitionRegistry();
        try (Stream<Path> paths = Files.walk((Paths.get(args[0])))) {
            paths.filter(Files::isRegularFile).map(Files::readString).each {
                tdr.merge(schemaParser.parse(it))
            }
        }

        def schema = schemaGenerator.makeExecutableSchema(tdr, RuntimeWiring.newRuntimeWiring().wiringFactory(new DummyWiringFactory()).build())
        def executionEnvironment = new ExecutionEnvironment(config: yamlConfig, schema: schema)
        def result = new ValidationExecution().validate(executionEnvironment, new BasicResultHandler())
    }
}
