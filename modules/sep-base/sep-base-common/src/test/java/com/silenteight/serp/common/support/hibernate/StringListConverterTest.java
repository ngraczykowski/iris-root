package com.silenteight.serp.common.support.hibernate;

import com.silenteight.serp.common.support.jackson.JsonConversionHelper.FailedToParseJsonException;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class StringListConverterTest {

  private static final String JSON_ARRAY_OF_STRINGS = "[\"value1\",\"value2\"]";

  private StringListConverter underTest = new StringListConverter();

  @Test
  void emptyListOfStringsShouldBeConvertedToJson() {
    List<String> values = Collections.emptyList();

    String actual = underTest.convertToDatabaseColumn(values);

    assertThat(actual).isEqualTo("[]");
  }

  @Test
  void listOfStringsShouldBeConvertedToJson() {
    List<String> values = Arrays.asList("value1", "value2");

    String actual = underTest.convertToDatabaseColumn(values);

    assertThat(actual).isEqualTo(JSON_ARRAY_OF_STRINGS);
  }

  @Test
  void jsonArrayOfStringsShouldBeConvertedToListOfStrings() {
    List<String> actual = underTest.convertToEntityAttribute(JSON_ARRAY_OF_STRINGS);

    assertThat(actual).isNotNull();
    assertThat(actual).containsExactly("value1", "value2");
  }

  @Test
  void shouldThrowFailedToParseJsonExceptionWhenTryConvertJsonObject() {
    assertThatThrownBy(() -> underTest.convertToEntityAttribute("{}"))
        .isInstanceOf(FailedToParseJsonException.class);
  }
}
