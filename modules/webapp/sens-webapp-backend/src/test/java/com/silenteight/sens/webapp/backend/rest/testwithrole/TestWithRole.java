package com.silenteight.sens.webapp.backend.rest.testwithrole;

import org.junit.jupiter.api.TestTemplate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@TestTemplate
public @interface TestWithRole {

  String[] roles() default {};

  String role() default "";
}
