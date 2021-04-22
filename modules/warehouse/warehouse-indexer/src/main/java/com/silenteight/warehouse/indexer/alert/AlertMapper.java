package com.silenteight.warehouse.indexer.alert;

import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.data.api.v1.Match;
import com.silenteight.sep.base.common.time.TimeSource;

import com.google.protobuf.Struct;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.*;
import static com.silenteight.warehouse.indexer.alert.NameResource.getId;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
class AlertMapper {

  private static final Integer FIRST_MATCH = 0;

  private final TimeSource timeSource;

  Map<String, Object> convertAlertToAttributes(Alert alert) {
    OffsetDateTime now = timeSource.offsetDateTime();
    Map<String, Object> documentAttributes = new LinkedHashMap<>();
    documentAttributes.put(KEY_ALERT, convertAlertToMap(alert));
    documentAttributes.put(INDEX_TIMESTAMP, now.format(ISO_DATE_TIME));
    int matchesCount = alert.getMatchesCount();
    if (matchesCount < 1) {
      return documentAttributes;
    }
    Match match = alert.getMatchesList().get(FIRST_MATCH);
    documentAttributes.put(KEY_MATCH, convertMatchToMap(match));
    return documentAttributes;
  }

  private Map<String, String> convertMatchToMap(Match match) {
    Map<String, String> matchAttributes =
        new LinkedHashMap<>(convertPayloadToMap(match.getPayload()));
    matchAttributes.put(KEY_NAME, getId(match.getName()));
    return matchAttributes;
  }

  private Map<String, String> convertAlertToMap(Alert alert) {
    Map<String, String> alertAttributes =
        new LinkedHashMap<>(convertPayloadToMap(alert.getPayload()));
    alertAttributes.put(KEY_NAME, getId(alert.getName()));
    return alertAttributes;
  }

  private Map<String, String> convertPayloadToMap(Struct struct) {
    return struct.getFieldsMap()
        .keySet()
        .stream()
        .collect(toMap(identity(), key -> struct.getFieldsMap().get(key).getStringValue()));
  }
}
