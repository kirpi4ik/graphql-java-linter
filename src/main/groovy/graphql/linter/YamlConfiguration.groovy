package graphql.linter

import org.yaml.snakeyaml.Yaml

import java.nio.file.Files
import java.nio.file.Paths

class YamlConfiguration {
    private static def yaml = new Yaml();
    private LinterConfiguration config
    private schema = [:]


    YamlConfiguration(yamlURI) {
        if (yamlURI) {
            config = yaml.load(Files.readString(Paths.get(yamlURI)))
        } else {
            config = yaml.load(Files.readString(Paths.get("src", "main", "resources", "linter.yaml")))
        }
    }

    LinterConfiguration getConfig() {
        return config
    }
}
