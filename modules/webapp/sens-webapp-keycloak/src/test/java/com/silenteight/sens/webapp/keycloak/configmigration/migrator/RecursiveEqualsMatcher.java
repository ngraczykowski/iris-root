package com.silenteight.sens.webapp.keycloak.configmigration.migrator;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

import io.vavr.control.Try;
import org.mockito.ArgumentMatcher;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;

/**
 * An {@link org.mockito.ArgumentMatcher} that is suitable for classes not implementing {@link
 * T#equals(T)}} correctly. It recursively checks every field of class for equality.
 */
//TODO(bgulowaty): Move to some shared module
@RequiredArgsConstructor
@ToString
class RecursiveEqualsMatcher<T> implements ArgumentMatcher<T> {

  private final T expected;

  static <T> T eqRecursively(T expected) {
    return argThat(new RecursiveEqualsMatcher<>(expected));
  }

  @Override
  public boolean matches(T actual) {
    return Try.run(() ->
        assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(expected))
        .isSuccess();
  }
}
