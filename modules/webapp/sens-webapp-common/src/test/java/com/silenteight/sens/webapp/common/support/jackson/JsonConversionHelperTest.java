package com.silenteight.sens.webapp.common.support.jackson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;

class JsonConversionHelperTest {

  private static final JsonConversionHelper UNDER_TEST = JsonConversionHelper.INSTANCE;

  @Test
  public void givenStreamOfStrings_serializesToJsonArray() {
    String jsonArray = UNDER_TEST.serializeToString(
        UNDER_TEST.serializeStream(Stream.of("A", "B", "C")));
    assertThat(jsonArray).isEqualTo("[\"A\",\"B\",\"C\"]");
  }

  @Test
  public void givenStringJsonArray_deserializeToListOfStrings() {
    List<String> list = UNDER_TEST.deserializeCollection("[\"pol\", \"pl\"]", String.class);
    assertThat(list).isEqualTo(asList("pol", "pl"));
  }

  @Test
  public void givenMapOfStrings_serializesToJson() {
    Map<String, String> given = ImmutableMap.of(
        "key1", "value1",
        "key2", "value2"
    );

    String actual = UNDER_TEST.serializeToString(UNDER_TEST.serializeMap(given));

    assertThat(actual).isEqualTo(
        "{"
            + "\"key1\":\"value1\","
            + "\"key2\":\"value2\""
            + "}"
    );
  }

  @Test
  public void givenMapOfSimpleObjects_serializesToJson() {
    Map<String, SimpleObject> given = ImmutableMap.of(
        "key1", new SimpleObject("title1", "subtitle1"),
        "key2", new SimpleObject("title2", "subtitle2")
    );

    String actual = UNDER_TEST.serializeToString(UNDER_TEST.serializeMap(given));

    assertThat(actual).isEqualTo(
        "{"
            + "\"key1\":{"
            + "\"title\":\"title1\""
            + ",\"subtitle\":\"subtitle1\""
            + "},"
            + "\"key2\":{"
            + "\"title\":\"title2\""
            + ",\"subtitle\":\"subtitle2\""
            + "}"
            + "}");
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  private class SimpleObject {

    private String title;
    private String subtitle;
  }
}
