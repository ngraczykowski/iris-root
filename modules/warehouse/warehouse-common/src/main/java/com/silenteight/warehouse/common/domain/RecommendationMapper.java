package com.silenteight.warehouse.common.domain;

import lombok.NonNull;

import com.silenteight.warehouse.common.properties.RecommendationProperties;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class RecommendationMapper {

  @NonNull
  private final String recommendationFieldName;

  @NonNull
  private final Map<String, Recommendation> recommendationMap;


  public RecommendationMapper(RecommendationProperties properties) {
    recommendationFieldName = properties.getRecommendationFieldName();
    recommendationMap = properties
        .getValues()
        .entrySet()
        .stream()
        .map(RecommendationMapper::revertKeyValuesInMap)
        .flatMap(map -> map.entrySet().stream())
        .collect(Collectors.toMap(
            Entry::getKey,
            Entry::getValue));
  }

  private static Map<String, Recommendation> revertKeyValuesInMap(
      Entry<String, List<String>> entry) {
    return entry
        .getValue()
        .stream()
        .collect(
            Collectors.toMap(Function.identity(), v -> Recommendation.valueOf(entry.getKey())));
  }

  public Recommendation getRecommendationByValue(Map<String, String> payload) {
    var recommendationFromPayload =
        payload.get(recommendationFieldName);
    return recommendationMap.getOrDefault(
        recommendationFromPayload, Recommendation.UNSPECIFIED);

  }
}
