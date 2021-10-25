package com.silenteight.warehouse.indexer.alert.mapping;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.support.PayloadConverter;

import com.google.protobuf.Value;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static com.silenteight.warehouse.common.opendistro.roles.RolesMappedConstants.COUNTRY_KEY;
import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.ALERT_NAME;
import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.ALERT_PREFIX;
import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.DISCRIMINATOR;
import static com.silenteight.warehouse.indexer.alert.mapping.AlertMapperConstants.INDEX_TIMESTAMP;
import static java.time.ZoneOffset.UTC;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static java.util.Optional.ofNullable;
import static java.util.function.Predicate.not;

@RequiredArgsConstructor
public class AlertMapper {

  @NonNull
  private final TimeSource timeSource;
  @NonNull
  private final AlertMappingProperties alertMappingProperties;
  @NonNull
  private final PayloadConverter payloadConverter;

  public Map<String, Object> convertAlertToAttributes(AlertDefinition alert) {
    OffsetDateTime now = timeSource.now().atOffset(UTC);

    Map<String, Object> documentAttributes = new LinkedHashMap<>();
    documentAttributes.putAll(
        payloadConverter.convertPayloadToMap(alert.getPayload(), ALERT_PREFIX));
    documentAttributes.put(INDEX_TIMESTAMP, now.format(ISO_DATE_TIME));
    documentAttributes.put(DISCRIMINATOR, alert.getDiscriminator());

    extractAlertName(alert)
        .ifPresent(alertName -> documentAttributes.put(ALERT_NAME, alertName));
    extractAlertField(alert, alertMappingProperties.getCountrySourceKey())
        .ifPresent(countryValue -> documentAttributes.put(COUNTRY_KEY, countryValue));

    return documentAttributes;
  }

  private Optional<String> extractAlertName(AlertDefinition alert) {
    return ofNullable(alert.getName())
        .filter(not(String::isEmpty));
  }

  private Optional<String> extractAlertField(AlertDefinition alert, String fieldName) {
    return ofNullable(alert.getPayload().getFieldsOrDefault(fieldName, null))
        .map(Value::getStringValue);
  }
}
