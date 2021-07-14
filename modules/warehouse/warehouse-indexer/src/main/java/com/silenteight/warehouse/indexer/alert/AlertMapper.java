package com.silenteight.warehouse.indexer.alert;

import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.sep.base.common.time.TimeSource;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.silenteight.warehouse.common.opendistro.roles.RolesMappedConstants.COUNTRY_KEY;
import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.ALERT_PREFIX;
import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.DISCRIMINATOR;
import static com.silenteight.warehouse.indexer.alert.AlertMapperConstants.INDEX_TIMESTAMP;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
class AlertMapper {

  private final TimeSource timeSource;
  private final AlertMappingProperties alertMappingProperties;

  Map<String, String> convertAlertToAttributes(Alert alert) {
    OffsetDateTime now = timeSource.now().atOffset(UTC);

    Map<String, String> documentAttributes = new LinkedHashMap<>();

    documentAttributes.put(INDEX_TIMESTAMP, now.format(ISO_DATE_TIME));
    documentAttributes.put(DISCRIMINATOR, alert.getDiscriminator());
    documentAttributes.put(
        COUNTRY_KEY, extractAlertField(alert, alertMappingProperties.getCountrySourceKey()));
    documentAttributes.putAll(convertPayloadToMap(alert.getPayload(), ALERT_PREFIX));

    return documentAttributes;
  }

  private Map<String, String> convertPayloadToMap(Struct struct, String prefix) {
    return struct.getFieldsMap()
        .keySet()
        .stream()
        .collect(toMap(
            key -> prefix + key,
            key -> struct.getFieldsMap().get(key).getStringValue()));
  }

  private String extractAlertField(Alert alert, String fieldname) {
    return ofNullable(alert.getPayload().getFieldsOrDefault(fieldname, null))
        .map(Value::getStringValue)
        .orElse(null);
  }
}
