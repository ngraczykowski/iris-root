package com.silenteight.hsbc.bridge.recommendation.event;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

@Value
@Builder
public class RecommendationsGeneratedEvent {

  @NonNull String analysis;
  List<AlertRecommendationInfo> alertRecommendationInfos;

  public List<String> getAlerts() {
    return alertRecommendationInfos.stream()
        .map(AlertRecommendationInfo::getAlert)
        .collect(Collectors.toList());
  }
}
