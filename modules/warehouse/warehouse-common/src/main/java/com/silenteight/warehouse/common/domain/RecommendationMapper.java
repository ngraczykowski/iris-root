package com.silenteight.warehouse.common.domain;

import lombok.Getter;
import lombok.NonNull;

import com.silenteight.warehouse.common.EnumPropertiesUtil;
import com.silenteight.warehouse.common.properties.RecommendationProperties;

import java.util.Map;

public final class RecommendationMapper {

  @NonNull
  @Getter
  public final String recommendationFieldName;

  @NonNull
  private final Map<String, Recommendation> recommendationMap;


  public RecommendationMapper(RecommendationProperties properties) {
    recommendationFieldName = properties.getFieldName();
    recommendationMap =
        EnumPropertiesUtil.mapPropertiesToEnum(Recommendation.class, properties.getValues());
  }

  public Recommendation getRecommendationByValue(Map<String, String> payload) {
    var recommendationFromPayload =
        payload.get(recommendationFieldName);
    return recommendationMap.getOrDefault(
        recommendationFromPayload, Recommendation.UNSPECIFIED);
  }
}
