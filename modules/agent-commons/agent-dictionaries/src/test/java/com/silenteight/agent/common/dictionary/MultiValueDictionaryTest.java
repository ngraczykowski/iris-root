package com.silenteight.agent.common.dictionary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MultiValueDictionaryTest {

  private static final String PATH = "multi_value_dictionary.dict";

  MultiValueDictionary dictionary;

  @BeforeEach
  void givenMultiValuesDictionary() {
    dictionary = MultiValueDictionary.fromSourceInverted(
        DictionarySource.fromResource(getClass(), PATH));
  }

  @Test
  void shouldReturnBaseNameForKnownDiminutiveName() {
    List<String> normalizedNames = dictionary.findByKey("Mike");
    assertThat(normalizedNames)
        .hasSize(2)
        .contains("MICHAEL", "MICHELLE");
  }

  @Test
  void shouldNotReturnAnyBaseNameForUnknownDiminutiveName() {
    List<String> normalizedNames = dictionary.findByKey("x");
    assertThat(normalizedNames).isEmpty();
  }

  @Test
  void shouldReturnManyBaseNamesForAnotherKnownDiminutiveName() {
    List<String> normalizedNames = dictionary.findByKey("johnie");
    assertThat(normalizedNames)
        .hasSize(7)
        .contains("JOHN")
        .contains("IAN")
        .doesNotContain("MICHAEL");
  }

  @Test
  void shouldReturnTheSameBaseNamesForMultipleDiminutiveNames() {
    assertThat(dictionary.findByKey("johnie"))
        .isEqualTo(dictionary.findByKey("johnnie"))
        .isEqualTo(dictionary.findByKey("johnny"))
        .isEqualTo(dictionary.findByKey("van"));
  }

  @Test
  void shouldHasMappingForReturnCorrectResult() {
    assertThat(dictionary.hasMappingFor("johnie", "john")).isTrue();
    assertThat(dictionary.hasMappingFor("johnie", "JOHN")).isTrue();
    assertThat(dictionary.hasMappingFor("john", "johnie")).isTrue();
    assertThat(dictionary.hasMappingFor("JOHN", "johnie")).isTrue();
    assertThat(dictionary.hasMappingFor("johnie", "michael")).isFalse();
    assertThat(dictionary.hasMappingFor("michael", "johnie")).isFalse();
  }
}
