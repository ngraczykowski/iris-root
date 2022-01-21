package com.silenteight.agent.common.dictionary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class UniqueValuesDictionaryTest {

  private UniqueValuesDictionary dictionary;

  @BeforeEach
  void givenUniqueValuesDictionary() {
    var path = "unique_values_dictionary.txt";

    dictionary = UniqueValuesDictionary.fromSource(DictionarySource.fromResource(getClass(), path));
  }

  @Test
  void getAllValuesTest() {
    assertThat(dictionary.getAllValues())
        .containsExactlyInAnyOrder("TOKEN1", "TOKEN2", "TOKEN3", "TOKEN4");
  }

  @Test
  void containsTest() {
    assertThat(dictionary.contains("TOKEN1")).isTrue();
    assertThat(dictionary.contains("token1")).isTrue();
    assertThat(dictionary.contains("TOKEN6")).isFalse();
  }

  @Test
  void containsAllTest() {
    assertThat(dictionary.containsAll(List.of("TOKEN1", "TOKEN2", "TOKEN3"))).isTrue();
    assertThat(dictionary.containsAll(List.of("TOKEN1", "TOKEN2", "token3"))).isTrue();
    assertThat(dictionary.containsAll(List.of("TOKEN1", "TOKEN2", "TOKEN6"))).isFalse();
  }
}
