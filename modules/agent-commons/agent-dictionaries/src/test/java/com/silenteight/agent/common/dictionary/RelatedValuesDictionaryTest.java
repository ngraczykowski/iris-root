package com.silenteight.agent.common.dictionary;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class RelatedValuesDictionaryTest {

  private static final String PATH = "related_values_dictionary.dict";

  private static final List<String> EXPECTED_VALUES = List.of("ARJUN", "TARUN", "ARUNA");
  public static final String NOT_EXISTING_VALUE = "not_existing_value";

  RelatedValuesDictionary dictionary = RelatedValuesDictionary.fromSource(
      DictionarySource.fromResource(getClass(), PATH));

  @Test
  void shouldReturnRelatedValuesForKnownValue() {
    List<String> foundValues = dictionary.findByValue("ARUN");

    assertThat(foundValues)
        .hasSize(3)
        .containsAll(EXPECTED_VALUES)
        .doesNotContain("ARUN");
  }

  @Test
  void shouldNormalizeSearchValue() {
    List<String> foundValues = dictionary.findByValue("arun");

    assertThat(foundValues)
        .hasSize(3)
        .containsAll(EXPECTED_VALUES)
        .doesNotContain("ARUN");
  }

  @Test
  void shouldReturnEmptyIfValueNotFound() {
    List<String> foundValues = dictionary.findByValue(NOT_EXISTING_VALUE);

    assertThat(foundValues)
        .isEmpty();
  }

  @Test
  void shouldConfirmHasValueForExistingValue() {
    assertThat(dictionary.hasValue("ARUN"))
        .isTrue();
  }

  @Test
  void shouldDeclineHasValueForNonExistingValue() {
    assertThat(dictionary.hasValue(NOT_EXISTING_VALUE))
        .isFalse();
  }

  @Test
  void shouldNormalizeHasValueInput() {
    assertThat(dictionary.hasValue("arun"))
        .isTrue();
  }
}
