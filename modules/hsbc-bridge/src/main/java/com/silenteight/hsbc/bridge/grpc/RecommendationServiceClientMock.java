package com.silenteight.hsbc.bridge.grpc;

import com.silenteight.hsbc.bridge.analysis.dto.GetRecommendationsDto;
import com.silenteight.hsbc.bridge.recommendation.RecommendationDto;
import com.silenteight.hsbc.bridge.recommendation.RecommendationServiceClient;

import org.springframework.retry.annotation.Retryable;

import java.time.OffsetDateTime;
import java.util.List;

class RecommendationServiceClientMock implements RecommendationServiceClient {

  @Override
  @Retryable(value = CannotGetRecommendationsException.class)
  public List<RecommendationDto> getRecommendations(GetRecommendationsDto request) {
    throwRandomRuntimeException();

    return List.of(RecommendationDto.builder()
        .name("SomeName")
        .alert("SomeAlert")
        .recommendationComment("SomeComment")
        .recommendedAction("SomeAction")
        .date(OffsetDateTime.now())
        .build());
  }

  private void throwRandomRuntimeException() throws CannotGetRecommendationsException {
    if (System.currentTimeMillis() % 2 == 0) {
      throw new CannotGetRecommendationsException();
    }
  }
}
