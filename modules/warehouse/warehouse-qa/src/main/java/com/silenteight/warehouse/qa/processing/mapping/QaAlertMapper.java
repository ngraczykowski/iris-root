package com.silenteight.warehouse.qa.processing.mapping;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import com.silenteight.data.api.v2.QaAlert;
import com.silenteight.data.api.v2.QaAlert.State;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.silenteight.warehouse.common.time.Timestamps.toOffsetDateTime;
import static java.lang.String.format;

@RequiredArgsConstructor
public class QaAlertMapper {

  public static final String STATE = "state";
  public static final String COMMENT = "comment";
  public static final String TIMESTAMP = "timestamp";

  private final ObjectMapper objectMapper;

  @SneakyThrows
  public AlertDefinition convertAlertToAttributes(QaAlert alert) {
    Map<String, Object> documentAttributes = new LinkedHashMap<>();

    documentAttributes.put(getFieldName(alert.getLevel(), STATE), alert.getState());
    documentAttributes.put(getFieldName(alert.getLevel(), COMMENT), alert.getComment());
    documentAttributes.put(
        getTimestamp(alert.getLevel(), alert.getState()),
        toOffsetDateTime(alert.getTimestamp()));

    return AlertDefinition.builder()
        .name(alert.getName())
        .payload(objectMapper.writeValueAsString(documentAttributes))
        .build();
  }

  private static String getFieldName(int level, String fieldName) {
    return format("qa.level-%d.%s", level, fieldName);
  }

  private static String getTimestamp(int level, State state) {
    return format("qa.level-%d.%s.%s", level, state, TIMESTAMP);
  }
}
