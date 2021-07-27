package com.silenteight.simulator.common.testing.rest.testwithrole;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import static java.util.stream.Stream.of;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.junit.platform.commons.util.AnnotationUtils.findAnnotation;
import static org.junit.platform.commons.util.AnnotationUtils.isAnnotated;

@RequiredArgsConstructor
public class TestWithRoleExtension implements TestTemplateInvocationContextProvider {

  private final SecurityContextRoleSetter securityContextRoleSetter;

  @Override
  public boolean supportsTestTemplate(ExtensionContext context) {
    return isAnnotated(context.getTestMethod(), TestWithRole.class);
  }

  @Override
  public Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(
      ExtensionContext context) {
    Method testMethod = context.getRequiredTestMethod();
    String displayName = context.getDisplayName();
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    TestWithRole repeatedTest = findAnnotation(testMethod, TestWithRole.class).orElseThrow();

    return getRoles(repeatedTest)
        .map(role -> new ForEachRoleTemplateInvocationContext(
            role, securityContextRoleSetter, displayName));
  }

  private static Stream<String> getRoles(TestWithRole repeatedTest) {
    String[] multipleRoles = repeatedTest.roles();
    String singleRole = repeatedTest.role();
    boolean singleRoleIsSet = isNotEmpty(singleRole);

    boolean bothParametersSet = multipleRoles.length != 0 && singleRoleIsSet;
    boolean noneParameterSet = multipleRoles.length == 0 && !singleRoleIsSet;
    if (bothParametersSet || noneParameterSet)
      throw new TestForRoleExtensionException(
          "Provide either single role or multiple roles argument");

    return singleRoleIsSet ? of(singleRole) : of(multipleRoles);
  }

  static final class TestForRoleExtensionException extends RuntimeException {

    private static final long serialVersionUID = 1903551576940341642L;

    TestForRoleExtensionException(String message) {
      super(message);
    }
  }
}
