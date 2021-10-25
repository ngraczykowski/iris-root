package com.silenteight.warehouse.indexer.support;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.*;

class SkipAnyMatchingKeysStrategyTest {

  private static final String TEST_VALUE = "1.TestValue.attribute";
  private static final String MATCHING_REGEXP_1 = "\\d\\.TestValue\\.attribute";
  private static final String MATCHING_REGEXP_2 = "\\d\\.TestValue\\..*";
  private static final String NOT_MATCHING_REGEXP_1 = "\\d\\.OtherValue\\.attribute";
  private static final String NOT_MATCHING_REGEXP_2 = "\\s\\.TestValue\\.attribute";

  @ParameterizedTest
  @MethodSource("getNoMatchRegExps")
  @DisplayName("Should return true if none reg exp matches")
  void shouldReturnTrueIfNoneRegExpMatches(List<String> regExps) {
    SkipAnyMatchingKeysStrategy underTest = new SkipAnyMatchingKeysStrategy(regExps);

    assertThat(underTest.test(TEST_VALUE)).isTrue();
  }

  private static Stream<Arguments> getNoMatchRegExps() {
    return Stream.of(
        Arguments.of(List.of(NOT_MATCHING_REGEXP_1)),
        Arguments.of(List.of(NOT_MATCHING_REGEXP_1, NOT_MATCHING_REGEXP_2))
    );
  }

  @ParameterizedTest
  @MethodSource("getAnyMatchingRegExps")
  @DisplayName("Should return false if any reg exp matches")
  void shouldReturnFalseIfAtLeastOneRegExpMatches(List<String> regExps) {
    SkipAnyMatchingKeysStrategy underTest = new SkipAnyMatchingKeysStrategy(regExps);

    assertThat(underTest.test(TEST_VALUE)).isFalse();
  }

  private static Stream<Arguments> getAnyMatchingRegExps() {
    return Stream.of(
        Arguments.of(List.of(MATCHING_REGEXP_1)),
        Arguments.of(List.of(MATCHING_REGEXP_1, MATCHING_REGEXP_2)),
        Arguments.of(List.of(MATCHING_REGEXP_1, NOT_MATCHING_REGEXP_2))
    );
  }

  @Test
  void shouldAllowAllKeysIfRegExpsListEmpty() {
    SkipAnyMatchingKeysStrategy underTest = new SkipAnyMatchingKeysStrategy(emptyList());

    assertThat(underTest.test(TEST_VALUE)).isTrue();
  }
}
