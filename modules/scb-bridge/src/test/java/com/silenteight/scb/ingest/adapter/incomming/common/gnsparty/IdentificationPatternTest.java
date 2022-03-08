package com.silenteight.scb.ingest.adapter.incomming.common.gnsparty;

import org.assertj.core.api.AbstractBooleanAssert;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static com.silenteight.scb.ingest.adapter.incomming.common.gnsparty.IdentificationPattern.builder;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IdentificationPatternTest {

  @Test
  void validPatternTest() {
    var pattern = builder().pattern("some text").build().compile();

    assertPatternMatchResult(pattern, "blah some text blah").isTrue();
    assertPatternMatchResult(pattern, "some text").isTrue();
    assertPatternMatchResult(pattern, "SOME TEXT").isTrue();
    assertPatternMatchResult(pattern, "some invalid text").isFalse();
  }

  private static AbstractBooleanAssert<?> assertPatternMatchResult(Pattern pattern, String text) {
    return assertThat(pattern.matcher(text).matches());
  }

  @Test
  void validPatternCaseSensitiveTest() {
    var pattern = builder().pattern("tExT").caseSensitive(true).build().compile();

    assertPatternMatchResult(pattern, "blah tExT blah").isTrue();
    assertPatternMatchResult(pattern, "blah TeXt blah").isFalse();
  }

  @Test
  void invalidPatternTest() {
    var pattern = builder().pattern("[").build();

    assertThrows(PatternSyntaxException.class, pattern::compile);
  }
}
