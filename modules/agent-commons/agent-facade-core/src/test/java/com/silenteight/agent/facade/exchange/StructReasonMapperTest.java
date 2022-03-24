package com.silenteight.agent.facade.exchange;

import lombok.Value;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.silenteight.agent.facade.exchange.StructReasonMapper.mapToStruct;
import static com.silenteight.agent.facade.exchange.StructReasonMapper.toStruct;
import static org.assertj.core.api.Assertions.*;

class StructReasonMapperTest {

  @Test
  void shouldMapStructableToStruct() {
    var testStructable = new TestStructable(null, "value", List.of(1, 2));
    var struct = toStruct(testStructable);
    assertThat(struct).hasToString(expectedStructValue());
  }

  @Test
  void shouldMapSingleFieldToStruct() {
    var singleFieldStruct = mapToStruct("customField", "customValue");
    assertThat(singleFieldStruct).hasToString(expectedSingleFieldStructValue());
  }

  private String expectedStructValue() {
    return "fields {\n"
        + "  key: \"listField\"\n"
        + "  value {\n"
        + "    list_value {\n"
        + "      values {\n"
        + "        number_value: 1.0\n"
        + "      }\n"
        + "      values {\n"
        + "        number_value: 2.0\n"
        + "      }\n"
        + "    }\n"
        + "  }\n"
        + "}\n"
        + "fields {\n"
        + "  key: \"valueField\"\n"
        + "  value {\n"
        + "    string_value: \"value\"\n"
        + "  }\n"
        + "}\n";
  }

  private String expectedSingleFieldStructValue() {
    return "fields {\n"
        + "  key: \"customField\"\n"
        + "  value {\n"
        + "    string_value: \"customValue\"\n"
        + "  }\n"
        + "}\n";
  }

  @Value
  static class TestStructable implements Structable {

    String nullField;
    String valueField;
    List<Integer> listField;
  }
}
