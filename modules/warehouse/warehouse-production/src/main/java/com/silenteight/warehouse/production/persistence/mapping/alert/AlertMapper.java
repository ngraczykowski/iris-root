package com.silenteight.warehouse.production.persistence.mapping.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v2.Alert;
import com.silenteight.warehouse.production.persistence.common.PayloadConverter;
import com.silenteight.warehouse.production.persistence.mapping.match.MatchMapper;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static java.util.Optional.ofNullable;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
public class AlertMapper {

  @NonNull
  private final PayloadConverter payloadConverter;
  @NonNull
  private final MatchMapper matchMapper;
  @NonNull
  private final List<String> labels;
  @NonNull
  private final String recommendationDateField;

  public AlertDefinition toAlertDefinition(Alert alert) {
    Map<String, String> payload = payloadConverter.convertPayloadToMap(alert.getPayload());
    return AlertDefinition.builder()
        .discriminator(alert.getDiscriminator())
        .name(parseName(alert.getName()))
        .payload(payloadConverter.convertPayloadToJson(payload))
        .recommendationDate(getRecommendationDate(payload))
        .labels(extractLabels(payload))
        .matchDefinitions(matchMapper.toMatchDefinitions(alert.getMatchesList()))
        .build();
  }

  public AlertDefinition toAlertDefinition(com.silenteight.data.api.v1.Alert alert) {
    Map<String, String> payload = payloadConverter.convertPayloadToMap(alert.getPayload());
    return AlertDefinition.builder()
        .discriminator(alert.getDiscriminator())
        .name(parseName(alert.getName()))
        .payload(payloadConverter.convertPayloadToJson(payload))
        .recommendationDate(getRecommendationDate(payload))
        .labels(extractLabels(payload))
        .build();
  }

  private static String parseName(String name) {
    return name.isBlank() ? null : name;
  }

  private OffsetDateTime getRecommendationDate(Map<String, String> payload) {
    return ofNullable(payload.get(recommendationDateField))
        .filter(not(String::isBlank))
        .map(OffsetDateTime::parse)
        .orElse(null);
  }

  private Map<String, String> extractLabels(Map<String, String> payload) {
    return payload.entrySet()
        .stream()
        .filter(map -> labels.contains(map.getKey()))
        .collect(toMap(Entry::getKey, Entry::getValue));
  }
}
