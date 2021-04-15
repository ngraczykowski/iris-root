package com.silenteight.warehouse.indexer.alert;

import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.data.api.v1.Match;

import com.google.protobuf.Struct;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.KEY_ALERT;
import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.KEY_MATCH;
import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.KEY_NAME;
import static com.silenteight.warehouse.indexer.alert.NameResource.getId;

@RequiredArgsConstructor
class AlertMapper {

  private static final Integer FIRST_MATCH = 0;

  Map<String, Object> convertAlertToAttributes(Alert alert) {
    Map<String, Object> documentAttributes = new LinkedHashMap<>();
    documentAttributes.put(KEY_ALERT, convertAlertToMap(alert));
    int matchesCount = alert.getMatchesCount();
    if (matchesCount < 1) {
      return documentAttributes;
    }
    Match match = alert.getMatchesList().get(FIRST_MATCH);
    documentAttributes.put(KEY_MATCH, convertMatchToMap(match));
    return documentAttributes;
  }

  private Map<String, String> convertMatchToMap(Match match) {
    Map<String, String> matchAttributes = new LinkedHashMap<>();
    matchAttributes.putAll(convertPayloadToMap(match.getPayload()));
    matchAttributes.put(KEY_NAME, getId(match.getName()));
    return matchAttributes;
  }

  private Map<String, String> convertAlertToMap(Alert alert) {
    Map<String, String> alertAttributes = new LinkedHashMap<>();
    alertAttributes.putAll(convertPayloadToMap(alert.getPayload()));
    alertAttributes.put(KEY_NAME, getId(alert.getName()));
    return alertAttributes;
  }

  private Map<String, String> convertPayloadToMap(Struct struct) {
    return struct.getFieldsMap()
        .keySet()
        .stream()
        .collect(
            Collectors.toMap(key -> key, key -> struct.getFieldsMap().get(key).getStringValue()));
  }
}
