package com.silenteight.warehouse.indexer.alert.mapping;

import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.sep.base.common.time.TimeSource;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import static com.silenteight.warehouse.common.opendistro.roles.RolesMappedConstants.COUNTRY_KEY;
import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.ALERT_NAME;
import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.ALERT_PREFIX;
import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.DISCRIMINATOR;
import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.INDEX_TIMESTAMP;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static java.util.Optional.ofNullable;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
public class AlertMapper {

  private final TimeSource timeSource;
  private final AlertMappingProperties alertMappingProperties;
  private final Predicate<String> allowedKeysPredicate;

  public Map<String, Object> convertAlertToAttributes(Alert alert) {
    OffsetDateTime now = timeSource.now().atOffset(UTC);

    Map<String, Object> documentAttributes = new LinkedHashMap<>();
    documentAttributes.putAll(convertPayloadToMap(alert.getPayload(), ALERT_PREFIX));
    documentAttributes.put(INDEX_TIMESTAMP, now.format(ISO_DATE_TIME));
    documentAttributes.put(DISCRIMINATOR, alert.getDiscriminator());

    extractAlertName(alert)
        .ifPresent(alertName -> documentAttributes.put(ALERT_NAME, alertName));
    extractAlertField(alert, alertMappingProperties.getCountrySourceKey())
        .ifPresent(countryValue -> documentAttributes.put(COUNTRY_KEY, countryValue));

    return documentAttributes;
  }

  private Map<String, String> convertPayloadToMap(Struct struct, String prefix) {
    return struct.getFieldsMap()
        .keySet()
        .stream()
        .filter(allowedKeysPredicate)
        .collect(toMap(
            key -> prefix + key,
            key -> struct.getFieldsMap().get(key).getStringValue()));
  }

  private Optional<String> extractAlertName(Alert alert) {
    return ofNullable(alert.getName())
        .filter(not(String::isEmpty));
  }

  private Optional<String> extractAlertField(Alert alert, String fieldName) {
    return ofNullable(alert.getPayload().getFieldsOrDefault(fieldName, null))
        .map(Value::getStringValue);
  }
}
