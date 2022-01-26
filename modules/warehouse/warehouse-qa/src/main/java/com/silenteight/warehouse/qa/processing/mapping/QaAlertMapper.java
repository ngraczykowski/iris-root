package com.silenteight.warehouse.qa.processing.mapping;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import com.silenteight.data.api.v2.QaAlert;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.String.format;

@RequiredArgsConstructor
public class QaAlertMapper {

  public static final String STATE = "state";
  public static final String COMMENT = "comment";

  private final ObjectMapper objectMapper;

  @SneakyThrows
  public AlertDefinition convertAlertToAttributes(QaAlert alert) {
    Map<String, Object> documentAttributes = new LinkedHashMap<>();
    documentAttributes.put(getFieldName(alert.getLevel(), STATE), alert.getState());
    documentAttributes.put(getFieldName(alert.getLevel(), COMMENT), alert.getComment());

    return AlertDefinition.builder()
        .name(alert.getName())
        .payload(objectMapper.writeValueAsString(documentAttributes))
        .build();
  }

  private static String getFieldName(int level, String fieldName) {
    return format("qa.level-%d.%s", level, fieldName);
  }
}
