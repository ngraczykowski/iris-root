package com.silenteight.agent.facade.exchange;

import lombok.Getter;

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
        + "  key: \"defaultValue\"\n"
        + "  value {\n"
        + "    number_value: 0.0\n"
        + "  }\n"
        + "}\n"
        + "fields {\n"
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

  @Getter
  static class TestStructable implements Structable {

    TestStructable(String nullField, String valueField, List<Integer> listField) {
      this.nullField = nullField;
      this.valueField = valueField;
      this.listField = listField;
    }

    int defaultValue;
    String nullField;
    String valueField;
    List<Integer> listField;
  }
}
