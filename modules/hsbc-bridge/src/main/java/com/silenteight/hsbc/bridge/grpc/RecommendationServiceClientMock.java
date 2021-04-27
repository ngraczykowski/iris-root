package com.silenteight.hsbc.bridge.grpc;

import com.silenteight.hsbc.bridge.analysis.dto.GetRecommendationsDto;
import com.silenteight.hsbc.bridge.recommendation.RecommendationDto;
import com.silenteight.hsbc.bridge.recommendation.RecommendationServiceClient;

import java.time.OffsetDateTime;
import java.util.List;

class RecommendationServiceClientMock implements RecommendationServiceClient {

  @Override
  public List<RecommendationDto> getRecommendations(GetRecommendationsDto request) {
    return List.of(RecommendationDto.builder()
        .name("SomeName")
        .alert("SomeAlert")
        .recommendationComment("SomeComment")
        .recommendedAction("SomeAction")
        .date(OffsetDateTime.MAX)
        .build());
  }
}
