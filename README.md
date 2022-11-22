## License
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## Version
[![Version](https://badge.fury.io/gh/kirpi4ik%2Fgraphql-java-linter.svg)](https://badge.fury.io/gh/kirpi4ik%2Fgraphql-java-linter)
[![GitHub Release](https://img.shields.io/github/release/kirpi4ik/graphql-java-linter.svg?style=flat)]()  

### Usage command line

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
2. Run from command line E.g:
```bash
java -jar graphql-java-linter-1.0.jar linter.yaml
```

### In CI/CD flow via unit tests

1. Add dependency

`build.gradle`
```groovy
dependencies {
    test 'org.myhab.tools:graphql-java-linter:1.0'
}
```
`pom.xml`
```xml
<dependency>
    <groupId>org.myhab.tools</groupId>
    <artifactId>graphql-java-linter</artifactId>
    <version>1.0</version>
    <scope>test</scope>
</dependency>
```

junit
```java
import graphql.linter.LintRunner;
import org.junit.Test;

import java.nio.file.Paths;

public class GraphQLLinterTest {
    @Test
    public void lint(){
        LintRunner runner = new LintRunner(Paths.get("/linter.yaml").toUri());
        runner.run();
    }
}
```
