package graphql.linter.registry.types

import java.lang.annotation.*

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface FieldRule {

}