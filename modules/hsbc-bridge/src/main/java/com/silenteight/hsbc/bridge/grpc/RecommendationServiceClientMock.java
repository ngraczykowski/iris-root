package com.silenteight.hsbc.bridge.grpc;

import com.silenteight.hsbc.bridge.analysis.dto.GetRecommendationsDto;
import com.silenteight.hsbc.bridge.recommendation.RecommendationDto;
import com.silenteight.hsbc.bridge.recommendation.RecommendationServiceClient;

import org.springframework.retry.annotation.Retryable;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static java.lang.System.currentTimeMillis;

class RecommendationServiceClientMock implements RecommendationServiceClient {

  @Override
  @Retryable(value = CannotGetRecommendationsException.class)
  public List<RecommendationDto> getRecommendations(GetRecommendationsDto request) {
    throwRandomRuntimeException();

    // TODO here we could get proper alerts names from analysis

    return List.of(RecommendationDto.builder()
        .alert("alerts/" + currentTimeMillis())
        .name("recommendations/recommendation-" + UUID.randomUUID())
        .date(OffsetDateTime.now())
        .recommendedAction("MANUAL_INVESTIGATION")
        .recommendationComment("S8 Recommendation: Manual Investigation")
        .build());
  }

  private void throwRandomRuntimeException() throws CannotGetRecommendationsException {
    if (currentTimeMillis() % 5 == 0) {
      throw new CannotGetRecommendationsException();
    }
  }
}
