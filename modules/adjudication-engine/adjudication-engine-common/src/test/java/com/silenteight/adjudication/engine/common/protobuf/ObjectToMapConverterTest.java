package com.silenteight.adjudication.engine.common.protobuf;

import lombok.Value;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.silenteight.adjudication.engine.common.protobuf.ObjectToMapConverter.convert;
import static org.assertj.core.api.Assertions.*;

class ObjectToMapConverterTest {

  @Test
  void givenNullInput_shouldConvertToEmptyMap() {
    assertThat(convert(null)).isEmpty();
  }

  @Test
  void givenSimpleClass_shouldConvertToMap() {
    assertThat(convert(new SimpleFields("test", 123, 123.456)))
        .containsOnlyKeys("string", "integer", "number")
        .containsValues("test", 123, 123.456);
  }

  @SuppressWarnings("java:S5853")
  @Test
  void givenComplexClass_shouldEmbedMapsInsideAMap() {
    var input = new ComplexFields(
        Map.of("key", "value", "otherkey", "othervalue"),
        new SimpleFields("string", 456, 456.789));

    var result = convert(input);
    assertThat(result)
        .containsOnlyKeys("map", "simple")
        .extractingByKey("simple", as(InstanceOfAssertFactories.map(String.class, Object.class)))
        .containsOnlyKeys("string", "integer", "number");
    assertThat(result)
        .extractingByKey("map", as(InstanceOfAssertFactories.map(String.class, Object.class)))
        .containsOnlyKeys("key", "otherkey")
        .containsValues("value", "othervalue");
  }

  @Value
  static class SimpleFields {

    String string;
    int integer;
    double number;
  }

  @Value
  static class ComplexFields {

    Map<String, Object> map;
    SimpleFields simple;
  }
}
