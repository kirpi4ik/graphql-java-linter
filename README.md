## License

[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

[![Version](https://badge.fury.io/gh/kirpi4ik%2Fgraphql-java-linter.svg)](https://badge.fury.io/gh/kirpi4ik%2Fgraphql-java-linter)
[![GitHub Release](https://img.shields.io/github/v/release/kirpi4ik/graphql-java-linter?include_prereleases)]()  
[![Maven Central](https://img.shields.io/maven-central/v/org.myhab.tools/graphql-java-linter)]()  
[![Nexus snapshot](https://img.shields.io/nexus/s/org.myhab.tools/graphql-java-linter?server=https%3A%2F%2Fs01.oss.sonatype.org%2F)](https://s01.oss.sonatype.org/content/repositories/snapshots/org/myhab/tools/graphql-java-linter/)

### Usage

1. Create yaml configuration file E.g: `linter.yaml`

```yaml
# GraphQL Linter Configuration
level: WARN
failWhenExceedWarningMax: true
schema:
  # local schema folder
  local:
    uri: /<schema folder>/
    extensions:
      - "graphql"
      - "graphqls"
  # OR you can use remote location(if local exist remote will be ignored)
  endpoint:
    url: "http://localhost:8181/graphql"
    headers:
      user-agent: "Linter"
      Authorization: "Bearer <token>"
rules:
  field_name_first_lowercase:
    level: WARN
    failWhenExceed: 2
  defined_types_are_used:
    level: WARN
    disable: true
    failWhenExceed: 10
```

### Command line
#### Run from command line:

```bash
java -jar graphql-java-linter-1.1.jar linter.yaml
```

### In CI/CD flow via unit tests

#### Add dependency

`build.gradle`

```groovy
dependencies {
    test 'org.myhab.tools:graphql-java-linter:1.1'
}
```

`pom.xml`

```xml

<dependency>
    <groupId>org.myhab.tools</groupId>
    <artifactId>graphql-java-linter</artifactId>
    <version>1.1</version>
    <scope>test</scope>
</dependency>
```

#### junit

```java
import graphql.linter.LintRunner;
import org.junit.Test;

import java.nio.file.Paths;

public class GraphQLLinterTest {
    @Test
    public void lint() {
        LintRunner runner = new LintRunner(Paths.get("linter.yaml").toUri());
        runner.run();
    }
}
```
