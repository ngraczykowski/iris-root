package com.silenteight.agent.common.dictionary;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.UnaryOperator;
import java.util.stream.Stream;

class BaseNormalizedValuesDictionaryTest {

  @ParameterizedTest
  @MethodSource
  void shouldContainsValue(String value, boolean result) {
    //given
    var data = Stream.of("aaa", "bbb");
    UnaryOperator<String> normalizer = String::toUpperCase;
    var dict = new BaseNormalizedValuesDictionaryImpl(data, normalizer);

    //when
    var r = dict.contains(value);

    //then
    Assertions.assertThat(r).isEqualTo(result);
  }

  static Stream<Arguments> shouldContainsValue() {
    return Stream.of(
        Arguments.of("aaa", true),
        Arguments.of("AAA", true),
        Arguments.of("bbb", true),
        Arguments.of("BBB", true),
        Arguments.of("", false),
        Arguments.of(" ", false),
        Arguments.of("ccc", false)
    );
  }

  private static class BaseNormalizedValuesDictionaryImpl extends BaseNormalizedValuesDictionary {

    BaseNormalizedValuesDictionaryImpl(Stream<String> stream, UnaryOperator<String> normalizer) {
      super(stream, normalizer);
    }
  }
}
