package com.silenteight.warehouse.indexer.alert;

import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.data.api.v1.Match;
import com.silenteight.sep.base.common.time.TimeSource;

import com.google.protobuf.Struct;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.*;
import static com.silenteight.warehouse.indexer.alert.NameResource.getSplitName;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

@RequiredArgsConstructor
class AlertMapper {

  private final TimeSource timeSource;

  Map<String, String> convertAlertAndMatchToAttributes(Alert alert, Match match) {
    OffsetDateTime now = timeSource.now().atOffset(UTC);

    Map<String, String> documentAttributes = new LinkedHashMap<>();

    documentAttributes.put(INDEX_TIMESTAMP, now.format(ISO_DATE_TIME));
    documentAttributes.put(ALERT_ID_KEY, getSplitName(alert.getName()));
    documentAttributes.putAll(convertPayloadToMap(alert.getPayload(), ALERT_PREFIX));
    documentAttributes.put(MATCH_ID_KEY, getSplitName(match.getName()));
    documentAttributes.putAll(convertPayloadToMap(match.getPayload(), MATCH_PREFIX));

    return documentAttributes;
  }

  private Map<String, String> convertPayloadToMap(Struct struct, String prefix) {
    return struct.getFieldsMap()
        .keySet()
        .stream()
        .collect(Collectors.toMap(
            key -> prefix + key,
            key -> struct.getFieldsMap().get(key).getStringValue()));
  }
}
