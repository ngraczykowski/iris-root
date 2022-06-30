package com.silenteight.agent.common.dictionary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SingleValueInvertedDictionaryTest {

  private static final String PATH = "single_value_dictionary.dict";

  private SingleValueDictionary dictionary;

  @BeforeEach
  void givenSingleValueDictionary() {
    dictionary = SingleValueDictionary.fromSourceInverted(
        DictionarySource.fromResource(getClass(), PATH));
  }

  @Test
  void shouldReturnNormalizedNameForIsmail() {
    assertThat(dictionary.findByKey("ISMAIL")).hasValue("ESMAIL");
  }

  @Test
  void shouldReturnNormalizedNameForIsmailLowerCase() {
    assertThat(dictionary.findByKey("ismail")).hasValue("ESMAIL");
  }

  @Test
  void shouldReturnEmptyIfNameNotFound() {
    assertThat(dictionary.findByKey("not_existing_name")).isEmpty();
  }

  @Test
  void shouldHasMappingForReturnCorrectResult() {
    assertThat(dictionary.hasMappingFor("ismail", "esmail")).isTrue();
    assertThat(dictionary.hasMappingFor("esmail", "ismail")).isTrue();
    assertThat(dictionary.hasMappingFor("ismail", "abdul")).isFalse();
    assertThat(dictionary.hasMappingFor("abdul", "ismail")).isFalse();
  }

  @Test
  void shouldContainOnlyKeys() {
    assertThat(dictionary.getKeys()).containsExactly("ISHMAIL", "ISMAIL", "ISHMAEL");
  }
}
