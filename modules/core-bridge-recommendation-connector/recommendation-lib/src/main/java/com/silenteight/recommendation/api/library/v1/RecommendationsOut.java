package com.silenteight.recommendation.api.library.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.proto.recommendation.api.v1.RecommendationsResponse;

import java.util.List;
import java.util.stream.Collectors;

@Value
@Builder
public class RecommendationsOut {

  @Builder.Default
  List<RecommendationOut> recommendations = List.of();
  StatisticsOut statistics;

  static RecommendationsOut createFrom(RecommendationsResponse response) {
    return RecommendationsOut.builder()
        .recommendations(response.getRecommendationsList()
            .stream()
            .map(RecommendationOut::createFrom)
            .collect(Collectors.toList()))
        .statistics(StatisticsOut.createFrom(response.getStatistics()))
        .build();
  }
}
