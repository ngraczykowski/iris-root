package com.silenteight.agent.common.dictionary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SingleValueDictionaryTest {

  private static final String PATH = "single_value_dictionary.dict";

  private SingleValueDictionary dictionary;

  @BeforeEach
  void givenSingleValueDictionary() {
    dictionary = SingleValueDictionary.fromSource(
        DictionarySource.fromResource(getClass(), PATH));
  }

  @Test
  void shouldReturnNormalizedNameForIsmail() {
    assertThat(dictionary.findByKey("ESMAIL")).hasValue("ISHMAEL;ISHMAIL;ISMAIL");
  }

  @Test
  void shouldReturnNormalizedNameForIsmailLowerCase() {
    assertThat(dictionary.findByKey("esmail")).hasValue("ISHMAEL;ISHMAIL;ISMAIL");
  }

  @Test
  void shouldReturnEmptyIfNameNotFound() {
    assertThat(dictionary.findByKey("not_existing_name")).isEmpty();
  }

  @Test
  void shouldHasMappingForReturnCorrectResult() {
    assertThat(dictionary.hasMappingFor("ismail", "ISHMAEL;ISHMAIL;ISMAIL")).isFalse();
    assertThat(dictionary.hasMappingFor("esmail", "ISHMAEL;ISHMAIL;ISMAIL")).isTrue();
  }

  @Test
  void shouldContainOnlyKeys() {
    assertThat(dictionary.getKeys()).containsExactly("ESMAIL");
  }
}
