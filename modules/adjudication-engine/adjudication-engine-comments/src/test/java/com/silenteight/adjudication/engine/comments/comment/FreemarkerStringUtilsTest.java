package com.silenteight.adjudication.engine.comments.comment;

import org.assertj.core.api.AbstractBooleanAssert;
import org.assertj.core.api.AbstractStringAssert;
import org.junit.jupiter.api.Test;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;

class FreemarkerStringUtilsTest {

  @Test
  void joinTest() {
    assertJoined(null).isEqualTo("");
    assertJoined(asList(null, "", "   ")).isEqualTo("");
    assertJoined(asList("A", "B")).isEqualTo("A, B");
    assertJoined(asList("A", "B", null, "", "  ", "C")).isEqualTo("A, B, C");
  }

  @Test
  void joinWithSeparatorTest() {
    assertJoinedWithSeparator(null, ";").isEqualTo("");
    assertJoinedWithSeparator(asList(null, "", "   "), ";").isEqualTo("");
    assertJoinedWithSeparator(asList("A", "B"), ";").isEqualTo("A;B");
    assertJoinedWithSeparator(asList("A", "B", null, "", "  ", "C"), ";").isEqualTo("A;B;C");
  }

  @Test
  void containsIgnoreCaseTest() {
    assertContainedIgnoreCase(null, "a").isFalse();
    assertContainedIgnoreCase(List.of(), "a").isFalse();
    assertContainedIgnoreCase(asList(null, ""), "a").isFalse();
    assertContainedIgnoreCase(asList(null, "", "A"), "a").isTrue();
    assertContainedIgnoreCase(asList("a", "b", "c"), "a").isTrue();
    assertContainedIgnoreCase(asList("a", "b", "c"), "A").isTrue();
    assertContainedIgnoreCase(asList("A", "B", "C"), "b").isTrue();
    assertContainedIgnoreCase(asList("A", "B", "C"), "D").isFalse();
  }

  private static AbstractStringAssert<?> assertJoinedWithSeparator(
      @Nullable List<String> values, String separator) {

    return assertThat(FreemarkerStringUtils.join(values, separator));
  }

  private static AbstractStringAssert<?> assertJoined(@Nullable List<String> values) {
    return assertThat(FreemarkerStringUtils.join(values));
  }

  @Nonnull
  private static AbstractBooleanAssert<?> assertContainedIgnoreCase(
      @Nullable List<String> values, String value) {

    return assertThat(FreemarkerStringUtils.containsIgnoreCase(values, value));
  }

}
