package com.silenteight.sens.webapp.common.testing.matcher;

import org.hamcrest.Matcher;
import org.mockito.ArgumentMatcher;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.join;

public class StreamMatcher<T> implements ArgumentMatcher<Stream<T>> {

  Matcher<Iterable<T>> toMatch;
  List<T> input = null;

  public StreamMatcher(Matcher<Iterable<T>> toMatch) {
    this.toMatch = toMatch;
  }

  public static <T> ArgumentMatcher<Stream<T>> streamThat(Matcher<Iterable<T>> toMatch) {
    return new StreamMatcher<>(toMatch);
  }

  @Override
  public boolean matches(Stream<T> argument) {
    // This is to protect against JUnit calling this more than once
    input = input == null ? argument.collect(toList()) : input;
    return toMatch.matches(input);
  }

  @Override
  public String toString() {
    return "expected " + toMatch + ",\n   provided:\n" + join(input, "\n");
  }
}
